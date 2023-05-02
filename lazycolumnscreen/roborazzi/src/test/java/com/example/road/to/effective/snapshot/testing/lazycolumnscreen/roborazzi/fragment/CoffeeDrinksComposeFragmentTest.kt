package com.example.road.to.effective.snapshot.testing.lazycolumnscreen.roborazzi.fragment

import androidx.core.os.bundleOf
import com.example.road.to.effective.snapshot.testing.lazycolumnscreen.CoffeeDrinksFragment
import com.example.road.to.effective.snapshot.testing.lazycolumnscreen.roborazzi.utils.filePath
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.GraphicsMode.Mode.NATIVE
import sergio.sastre.uitesting.robolectric.fragmentscenario.robolectricFragmentScenarioConfiguratorRule
import sergio.sastre.uitesting.utils.common.DisplaySize
import sergio.sastre.uitesting.utils.common.FontSize
import sergio.sastre.uitesting.utils.common.Orientation
import sergio.sastre.uitesting.utils.common.UiMode
import sergio.sastre.uitesting.utils.fragmentscenario.FragmentConfigItem
import sergio.sastre.uitesting.robolectric.fragmentscenario.RobolectricFragmentScenarioConfigurator
import sergio.sastre.uitesting.robolectric.fragmentscenario.waitForFragment

/**
 * Roborazzi requires Robolectric Native Graphics (RNG) to generate screenshots.
 *
 * Therefore, RNG must be active. In these tests, we do it by annotating tests with @GraphicsMode(NATIVE).
 * Alternatively one could drop the annotation and enable RNG for all Robolectric tests in a module,
 * adding the following in the module's build.gradle:
 *
 *  testOptions {
 *      unitTests {
 *          includeAndroidResources = true
 *          all {
 *              systemProperty 'robolectric.graphicsMode', 'NATIVE' // this
 *          }
 *      }
 *  }
 */
@RunWith(RobolectricTestRunner::class)
class CoffeeDrinksComposeFragmentHappyPathTest {

    @get:Rule
    val fragmentScenarioConfiguratorRule =
        robolectricFragmentScenarioConfiguratorRule<CoffeeDrinksFragment>(
            fragmentArgs = bundleOf("coffee_shop_name" to "MyCoffeeShop"),
            config = FragmentConfigItem(
                locale = "en",
                uiMode = UiMode.DAY,
                orientation = Orientation.PORTRAIT,
                fontSize = FontSize.NORMAL,
                displaySize = DisplaySize.NORMAL,
            ),
        )

    @GraphicsMode(NATIVE)
    @Test
    fun snapFragment() {
        fragmentScenarioConfiguratorRule.fragment
            .view!!
            .captureRoboImage(
                filePath("CoffeeDrinksFragment_HappyPath")
            )
    }
}

/**
 * Example with RobolectricFragmentScenarioConfigurator of AndroidUiTestingUtils:robolectric
 */
@RunWith(RobolectricTestRunner::class)
class CoffeeDrinksComposeFragmentUnhappyPathTest {

    @GraphicsMode(NATIVE)
    @Test
    fun snapFragment() {
        val fragmentScenario =
            RobolectricFragmentScenarioConfigurator.ForFragment()
                .setLocale("ar_XB")
                .setUiMode(UiMode.NIGHT)
                .setInitialOrientation(Orientation.LANDSCAPE)
                .setFontSize(FontSize.SMALL)
                .setDisplaySize(DisplaySize.SMALL)
                .launchInContainer(
                    fragmentArgs = bundleOf("coffee_shop_name" to "MyCoffeeShop"),
                    fragmentClass = CoffeeDrinksFragment::class.java,
                )

        fragmentScenario.waitForFragment()
            .view!!
            .captureRoboImage(filePath("CoffeeDrinksFragment_UnhappyPath"))

        fragmentScenario.close()
    }
}
