package com.example.road.to.effective.snapshot.testing.recyclerviewscreen.paparazzi.viewholder.parameterized

import android.view.View
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.R
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.paparazzi.utils.setPhoneOrientation
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.ui.rows.training.TrainingViewHolder

/**
 * Execute the command below to run only ViewHolderTests
 * 1. Record:
 *    ./gradlew :recyclerviewscreen:paparazzi:recordPaparazziDebug --tests '*ViewHolder*'
 * 2. Verify:
 *    ./gradlew :recyclerviewscreen:paparazzi:verifyPaparazziDebug --tests '*ViewHolder*'
 *
 * WARNING 1: Running these tests with pseudolocales (i.e. locale = "en-rXA" or locale = "ar-rXB")
 *            throws an exception
 *
 * WARNING 2: Paparazzi cannot handle the simultaneous animations properly and renders wrong screenshots
 *            whereas Roborazzi, Shot, Dropshots and Android-Testify do render them correctly
 */

/**
 * Example of Parameterized test with Parameterized Runner.
 *
 * Unlike TestParameterInjector, the testItem is used in all @Tests (the test methods do not admit
 * arguments).
 */
@RunWith(Parameterized::class)
class TrainingViewHolderParameterizedHappyPathTest(
    private val testItem: HappyPathTestItem
) {
    private val deviceConfig
        get() = testItem.item.deviceConfig

    companion object {
        @JvmStatic
        @Parameters
        fun testItemProvider(): Array<HappyPathTestItem> = HappyPathTestItem.entries.toTypedArray()
    }

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig =
        DeviceConfig.PIXEL_5.copy(
            nightMode = deviceConfig.nightMode,
            locale = deviceConfig.locale,
            fontScale = deviceConfig.fontScale,
        ).setPhoneOrientation(deviceConfig.orientation),
        theme = deviceConfig.theme,
        supportsRtl = true, // needed for "ar" locale
        renderingMode = SessionParams.RenderingMode.V_SCROLL,
    )

    @Test
    fun snapViewHolder() {
        val layout = paparazzi.inflate<View>(R.layout.training_row)

        val viewHolder =
            TrainingViewHolder(layout).apply {
                bind(item = testItem.item.trainingItem, languageClickedListener = null)
            }

        paparazzi.snapshot(
            view = viewHolder.itemView,
            offsetMillis = 3_000,
            name = "${testItem.name}_Parameterized",
        )
    }
}

@RunWith(Parameterized::class)
class TrainingViewHolderParameterizedUnhappyPathTest(
    private val testItem: UnhappyPathTestItem
) {
    private val deviceConfig
        get() = testItem.item.deviceConfig

    companion object {
        @JvmStatic
        @Parameters
        fun testItemProvider(): Array<UnhappyPathTestItem> =
            UnhappyPathTestItem.entries.toTypedArray()
    }

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig =
        DeviceConfig.PIXEL_5.copy(
            nightMode = deviceConfig.nightMode,
            locale = deviceConfig.locale,
            fontScale = deviceConfig.fontScale,
        ).setPhoneOrientation(deviceConfig.orientation),
        supportsRtl = true, // needed for "ar" locale
        theme = deviceConfig.theme,
        renderingMode = SessionParams.RenderingMode.V_SCROLL,
    )

    @Test
    fun snapViewHolder() {
        val layout = paparazzi.inflate<View>(R.layout.training_row)

        val viewHolder =
            TrainingViewHolder(layout).apply {
                bind(item = testItem.item.trainingItem, languageClickedListener = null)
            }

        paparazzi.snapshot(
            view = viewHolder.itemView,
            offsetMillis = 3_000,
            name = "${testItem.name}_Parameterized",
        )
    }
}
