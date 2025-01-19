package snapshot.testing.lazycolumn_previews.roborazzi.utils

import androidx.compose.ui.tooling.preview.Devices
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziComposeOptions
import com.github.takahirom.roborazzi.background
import com.github.takahirom.roborazzi.fontScale
import com.github.takahirom.roborazzi.locale
import com.github.takahirom.roborazzi.previewDevice
import com.github.takahirom.roborazzi.size
import com.github.takahirom.roborazzi.uiMode
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

object RoborazziComposeOptionsMapper {
    @OptIn(ExperimentalRoborazziApi::class)
    fun createFor(preview: ComposablePreview<AndroidPreviewInfo>): RoborazziComposeOptions =
        RoborazziComposeOptions {
            val previewInfo = preview.previewInfo
            size(
                widthDp = previewInfo.widthDp,
                heightDp = previewInfo.heightDp
            )
            background(
                showBackground = previewInfo.showBackground,
                backgroundColor = previewInfo.backgroundColor
            )
            locale(previewInfo.locale)
            uiMode(previewInfo.uiMode)
            previewDevice(previewInfo.device.ifBlank { Devices.NEXUS_5 } )
            fontScale(previewInfo.fontScale)
        }
}