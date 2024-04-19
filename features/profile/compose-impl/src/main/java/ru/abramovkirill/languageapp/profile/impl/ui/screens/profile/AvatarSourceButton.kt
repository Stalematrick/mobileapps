package ru.abramovkirill.languageapp.profile.impl.ui.screens.profile

import androidx.annotation.StringRes
import ru.abramovkirill.languageapp.profile.impl.R

enum class AvatarSourceButton(@StringRes val textRes: Int) {
    Gallery(R.string.profile_change_image_gallery),
    Camera(R.string.profile_change_image_camera);
}
