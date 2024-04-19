package ru.abramovkirill.languageapp.profile.impl.data

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.abramovkirill.languageapp.profile.impl.domain.AvatarCrop
import ru.abramovkirill.languageapp.profile.impl.domain.AvatarCropper
import java.io.ByteArrayOutputStream


class AvatarCropperImpl(
    private val cropperDispatcher: CoroutineDispatcher,
    private val applicationContext: Application,
    private val imageLoader: ImageLoader,
) : AvatarCropper {
    override suspend fun crop(avatarUri: String, avatarCrop: AvatarCrop): AvatarCropper.Result =
        withContext(cropperDispatcher) {
            val imageLoadRequest = ImageRequest.Builder(applicationContext)
                .data(Uri.parse(avatarUri))
                .allowHardware(false)
                .size(Size.ORIGINAL)
                .build()

            val result = (imageLoader.execute(imageLoadRequest) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap

            check(bitmap != null) { "Avatar Uri read failed" }

            val croppedBitmap = when (avatarCrop) {
                is AvatarCrop.Exactly -> {
                    Bitmap.createBitmap(
                        bitmap,
                        avatarCrop.left,
                        avatarCrop.top,
                        avatarCrop.width,
                        avatarCrop.height
                    )
                }
                AvatarCrop.Full -> {
                    bitmap
                }
            }

            val byteArray = ByteArrayOutputStream().use { stream ->
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
                stream.toByteArray()
            }

            croppedBitmap.recycle()

            AvatarCropper.Result(
                bytes = byteArray,
                extension = "jpeg"
            )
        }

    companion object {
        private const val COMPRESS_QUALITY = 100
    }
}
