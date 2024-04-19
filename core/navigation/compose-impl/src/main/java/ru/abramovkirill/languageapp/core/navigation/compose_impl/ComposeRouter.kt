package ru.abramovkirill.languageapp.core.navigation.compose_impl

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import ru.abramovkirill.languageapp.common.utils.currentRoute
import ru.abramovkirill.languageapp.common.utils.parentNavGraph
import ru.abramovkirill.languageapp.common.utils.previousRoute
import ru.abramovkirill.languageapp.core.navigation.api.ParentRoute
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingBackReceiver
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption

class ComposeRouter : Router, RoutingBackReceiver {

    private val _actions = Channel<RoutingAction>(Channel.BUFFERED)
    private val actions = _actions.receiveAsFlow()

    private var routingBackReceivers = listOf<RoutingBackReceiver>()

    private var navController: NavController? = null

    override val currentRoute: String?
        get() = navController?.currentRoute

    override val previousRoute: String?
        get() = navController?.previousRoute

    override val parentRoute: ParentRoute?
        get() = navController?.parentNavGraph?.let { parent ->
            ParentRoute(
                path = parent.route,
                startDestination = parent.startDestinationRoute,
            )
        }

    override fun registerRoutingBackReceiver(receiver: RoutingBackReceiver) {
        routingBackReceivers += receiver
    }

    override fun unregisterRoutingBackReceiver(receiver: RoutingBackReceiver) {
        routingBackReceivers -= receiver
    }

    override fun onRoutingBackReceived(): Boolean {
        var isRoutingBackHandled = false

        routingBackReceivers.forEach { routingBackReceiver ->
            if (routingBackReceiver.onRoutingBackReceived()) {
                isRoutingBackHandled = true
            }
        }

        return isRoutingBackHandled
    }

    override suspend fun navigate(
        routePath: String,
        options: List<RoutingOption>,
        arguments: Map<String, Any>,
    ) {
        action(RoutingAction.NavigateTo(routePath, options, arguments))
    }

    override suspend fun navigateBack() {
        action(RoutingAction.NavigateBack)
    }

    fun attachNavController(
        navController: NavController,
        coroutineScope: CoroutineScope,
    ) {
        this.navController = navController

        actions
            .onEach { action -> handleRoutingAction(navController, action) }
            .flowOn(Dispatchers.Main)
            .launchIn(coroutineScope)
    }

    fun detachNavController() {
        this.navController = null
    }

    private fun handleRoutingAction(
        navController: NavController,
        action: RoutingAction
    ) {
        when (action) {
            is RoutingAction.NavigateTo ->
                handleNavigateToRouting(
                    navController = navController,
                    routePath = action.routePath,
                    options = action.options,
                    arguments = action.arguments
                )
            RoutingAction.NavigateBack ->
                handleNavigateBackRouting(navController)
        }
    }

    private fun handleNavigateToRouting(
        navController: NavController,
        routePath: String,
        options: List<RoutingOption>,
        arguments: Map<String, Any>,
    ) {
        navController.navigate(getPathWithArguments(routePath, arguments)) {
            options.forEach { option ->
                when (option) {
                    is RoutingOption.LaunchSingleTop -> {
                        launchSingleTop = option.launchSingleTop
                    }
                    is RoutingOption.PopUpTo -> {
                        popUpTo(option.routePath) {
                            inclusive = option.inclusive
                        }
                    }
                    is RoutingOption.ClearStack -> {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    private fun handleNavigateBackRouting(
        navController: NavController,
    ) {
        navController.popBackStack()
    }

    private suspend fun action(action: RoutingAction) {
        _actions.send(action)
    }

    private fun getPathWithArguments(
        path: String,
        arguments: Map<String, Any>
    ) = buildString {
        append(path)

        if (arguments.isNotEmpty()) {
            append('?')

            arguments.forEach { (name, value) ->
                append("$name=$value")
            }
        }
    }
}
