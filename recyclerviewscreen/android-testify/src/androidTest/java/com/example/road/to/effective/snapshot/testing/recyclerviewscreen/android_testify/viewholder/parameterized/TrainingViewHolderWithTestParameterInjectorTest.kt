package com.example.road.to.effective.snapshot.testing.recyclerviewscreen.android_testify.viewholder.parameterized

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.R
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.ui.rows.training.TrainingViewHolder
import com.example.road.to.effective.snapshot.testing.testannotations.HappyPath
import com.example.road.to.effective.snapshot.testing.testannotations.UnhappyPath
import com.example.road.to.effective.snapshot.testing.testannotations.ViewHolderTest
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import dev.testify.TestifyFeatures.GenerateDiffs
import dev.testify.annotation.ScreenshotInstrumentation
import sergio.sastre.uitesting.android_testify.ScreenshotRuleWithConfigurationForView
import sergio.sastre.uitesting.android_testify.assertSame
import sergio.sastre.uitesting.android_testify.setScreenshotFirstView
import sergio.sastre.uitesting.android_testify.waitForIdleSync
import sergio.sastre.uitesting.utils.testrules.animations.DisableAnimationsRule

/**
 * Execute the command below to run only ViewHolderTests
 * 1. Record:
 *    ./gradlew :recyclerviewscreen:android-testify:screenshotRecord -Pannotation=com.example.road.to.effective.snapshot.testing.testannotations.ViewHolderTest
 * 2. Verify:
 *    ./gradlew :recyclerviewscreen:android-testify:screenshotTest -Pandroid.testInstrumentationRunnerArguments.annotation=com.example.road.to.effective.snapshot.testing.testannotations.ViewHolderTest
 *
 * With Gradle Managed Devices (API 27+)
 * 1. Record (saved under this module's build/outputs/managed_device_android_test_additional_output/...):
 *    ./gradlew :recyclerviewscreen:android-testify:pixel3api30DebugAndroidTest -PuseTestStorage -PrecordModeGmd -Pandroid.testInstrumentationRunnerArguments.annotation=com.example.road.to.effective.snapshot.testing.testannotations.ViewHolderTest
 * 2. Verify (copy recorded screenshots + assert):
 *  - Copy recorded screenshots in androidTest/assets -> https://ndtp.github.io/android-testify/docs/recipes/gmd
 *    ./gradlew :recyclerviewscreen:android-testify:copyScreenshots -Pdevices=pixel3api30
 *  - Assert
 *    ./gradlew :recyclerviewscreen:android-testify:pixel3api30DebugAndroidTest -PuseTestStorage -Pandroid.testInstrumentationRunnerArguments.annotation=com.example.road.to.effective.snapshot.testing.testannotations.ViewHolderTest
 *
 * To run them using Android Orchestrator, add the following at the end of the command:
 * -PuseOrchestrator
 */

/**
 * Example of Parameterized test with Parameterized Runner.
 *
 * Unlike TestParameterInjector, the testItem is used in all @Tests (the test methods do not admit
 * arguments).
 *
 * On the other hand, ParameterizedRunner is compatible with instrumented test of any API level,
 * whereas TestParameterInjector requires API 24+, throwing
 * java.lang.NoClassDefFoundError: com.google.common.cache.CacheBuilder error in lower APIs
 */
@RunWith(TestParameterInjector::class)
class TrainingViewHolderTestParameterHappyPathTest(
    @TestParameter private val testItem: HappyPathTestItem
) {

    private val viewConfig
        get() = testItem.item.viewConfig

    @get:Rule(order = 0)
    var disableAnimationsRule = DisableAnimationsRule()

    @get:Rule(order = 1)
    var screenshotRule = ScreenshotRuleWithConfigurationForView(
        exactness = 0.85f,
        config = viewConfig
    )

    @ScreenshotInstrumentation
    @HappyPath
    @ViewHolderTest
    @Test
    fun snapViewHolder() {
        screenshotRule
            .setTargetLayoutId(R.layout.training_row)
            .setViewModifications { targetLayout ->
                TrainingViewHolder(targetLayout).apply {
                    bind(
                        item = testItem.item.trainingItem,
                        languageClickedListener = null,
                    )
                }
            }
            .setScreenshotFirstView()
            .withExperimentalFeatureEnabled(GenerateDiffs)
            .waitForIdleSync()
            .assertSame(
                name = "${testItem.name}_TestParameter"
            )
    }
}

@RunWith(TestParameterInjector::class)
class TrainingViewHolderTestParameterUnhappyPathTest(
    @TestParameter private val testItem: UnhappyPathTestItem
) {

    private val viewConfig
        get() = testItem.item.viewConfig

    @get:Rule(order = 0)
    var disableAnimationsRule = DisableAnimationsRule()

    @get:Rule(order = 1)
    var screenshotRule = ScreenshotRuleWithConfigurationForView(
        exactness = 0.85f,
        config = viewConfig
    )

    @ScreenshotInstrumentation
    @UnhappyPath
    @ViewHolderTest
    @Test
    fun snapViewHolder() {
        screenshotRule
            .setTargetLayoutId(R.layout.training_row)
            .setViewModifications { targetLayout ->
                TrainingViewHolder(targetLayout).apply {
                    bind(
                        item = testItem.item.trainingItem,
                        languageClickedListener = null,
                    )
                }
            }
            .setScreenshotFirstView()
            .withExperimentalFeatureEnabled(GenerateDiffs)
            .waitForIdleSync()
            .assertSame(
                name = "${testItem.name}_TestParameter"
            )
    }
}
