package com.example.road.to.effective.snapshot.testing.lazycolumnscreen.crosslibrary.utils

import androidx.test.platform.app.InstrumentationRegistry.*

import com.dropbox.dropshots.ThresholdValidator
import com.example.road.to.effective.snapshot.testing.lazycolumnscreen.crosslibrary.BuildConfig
import sergio.sastre.uitesting.android_testify.AndroidTestifyConfig
import sergio.sastre.uitesting.dropshots.DropshotsConfig
import sergio.sastre.uitesting.mapper.paparazzi.PaparazziConfig
import sergio.sastre.uitesting.mapper.paparazzi.wrapper.DeviceConfig
import sergio.sastre.uitesting.mapper.paparazzi.wrapper.RenderingMode
import sergio.sastre.uitesting.mapper.roborazzi.RoborazziConfig
import sergio.sastre.uitesting.mapper.roborazzi.wrapper.screen.DeviceScreen
import sergio.sastre.uitesting.shot.ShotConfig
import sergio.sastre.uitesting.utils.crosslibrary.config.BitmapCaptureMethod.PixelCopy
import sergio.sastre.uitesting.utils.crosslibrary.config.ScreenshotConfigForComposable
import sergio.sastre.uitesting.utils.crosslibrary.testrules.ScreenshotTestRuleForComposable
import sergio.sastre.uitesting.utils.crosslibrary.testrules.implementations.shared.SharedScreenshotLibraryTestRuleForComposable
import java.io.File

/**
 * A [SharedScreenshotLibraryTestRuleForComposable] that decides which library runs the instrumented screenshot tests
 * based on the -PscreenshotLibrary argument passed via command line.
 *
 * It required some extra configuration in the gradle file
 * Take a look at the :lazycolumnscreen:crosslibrary gradle file to see how it is configured
 */

class CrossLibraryScreenshotTestRule(
    override val config: ScreenshotConfigForComposable,
) : SharedScreenshotLibraryTestRuleForComposable(config) {

    override fun getInstrumentedScreenshotLibraryTestRule(
        config: ScreenshotConfigForComposable,
    ): ScreenshotTestRuleForComposable =
        when (instrumentationScreenshotLibraryName) {
            BuildConfig.SHOT -> shotScreenshotTestRule
            BuildConfig.DROPSHOTS -> dropshotsScreenshotTestRule
            BuildConfig.ANDROID_TESTIFY -> androidTestifyScreenshotTestRule
            else -> androidTestifyScreenshotTestRule
        }

    override fun getJvmScreenshotLibraryTestRule(
        config: ScreenshotConfigForComposable,
    ): ScreenshotTestRuleForComposable =
        when (jvmScreenshotLibraryName) {
            BuildConfig.PAPARAZZI -> paparazziScreenshotTestRule
            BuildConfig.ROBORAZZI -> roborazziScreenshotTestRule
            else -> throw ScreenshotLibraryArgumentMissingException()
        }

    private val instrumentationScreenshotLibraryName
        get() = getArguments().getString(BuildConfig.SCREENSHOT_LIBRARY)

    private val jvmScreenshotLibraryName
        get() = System.getProperty(BuildConfig.SCREENSHOT_LIBRARY)

    private class ScreenshotLibraryArgumentMissingException :
        RuntimeException(
            "You must specify a screenshot library when executing screenshot tests " +
                    "via gradle property. For instance -PscreenshotLibrary=shot"
        )
}

 fun defaultCrossLibraryScreenshotTestRule(
     config: ScreenshotConfigForComposable = ScreenshotConfigForComposable(),
 ): ScreenshotTestRuleForComposable =
    CrossLibraryScreenshotTestRule(config)
        // Optional: configure for the libraries you use
        .configure(
            ShotConfig(
                bitmapCaptureMethod = PixelCopy(),
            )
        ).configure(
            DropshotsConfig(
                bitmapCaptureMethod = PixelCopy(),
                resultValidator = ThresholdValidator(0.15f),
                filePath = "dropshots",
            )
        ).configure(
            AndroidTestifyConfig(
                bitmapCaptureMethod = PixelCopy(),
            )
        ).configure(
            PaparazziConfig(
                deviceConfig = DeviceConfig.NEXUS_4,
                renderingMode = RenderingMode.SHRINK,
            )
        ).configure(
            RoborazziConfig(
                deviceScreen = DeviceScreen.Phone.NEXUS_4,
                filePath = userTestFilePath()
            )
        )

fun userTestFilePath(): String {
    val path = System.getProperty("user.dir")
    val file = File("$path/src/test/")
    return file.path
}
