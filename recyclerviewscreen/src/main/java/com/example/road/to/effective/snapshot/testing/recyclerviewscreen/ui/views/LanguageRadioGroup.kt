package com.example.road.to.effective.snapshot.testing.recyclerviewscreen.ui.views

import android.animation.Animator
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.RadioGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.get
import androidx.core.view.isInvisible
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.LanguageFilterClickedListener
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.data.Language
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.data.Translation
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.ui.rows.training.TrainingItem
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.utils.bubbleSortDescending
import com.example.road.to.effective.snapshot.testing.recyclerviewscreen.utils.sortByValueSize
import java.util.*

class LanguageRadioGroup : RadioGroup {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    @SuppressLint("ObjectAnimatorBinding")
    private fun addViewWithAnimation(
        view: LanguageFilterView,
        startAnimDelay: Long = 500,
        pos: Int = -1
    ) {

        val appear: Animator = ObjectAnimator.ofPropertyValuesHolder(
            null as Any?,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        ).apply {
            duration = 500
            startDelay = startAnimDelay
            interpolator = OvershootInterpolator()
        }

        val itemLayoutTransition = layoutTransition
            .apply { setAnimator(LayoutTransition.APPEARING, appear) }
        layoutTransition = itemLayoutTransition

        addView(view, pos)
    }

    fun addNewLangs(
        context: Context,
        allLangsSorted: SortedSet<Language>,
        langsToAdd: Map<Language, Collection<Translation>>,
        activeLangs: Set<Language>,
        listener: LanguageFilterClickedListener? = null
    ) {
        langsToAdd.mapKeys { entry ->
            val amountTexts = langsToAdd[entry.key]?.size ?: 0
            val checkMark =
                LanguageFilterView.createLanguageRadioButtonLayout(
                    ctx = context,
                    checked = activeLangs.contains(entry.key),
                    viewTag = entry.key,
                    amountTexts = amountTexts,
                    listener = listener
                )

            val langPos = allLangsSorted.indexOf(entry.key)
            addViewWithAnimation(checkMark, pos = langPos)
        }
    }

    fun getLangRadioButtonByTag(langTag: Language): LanguageFilterView? =
        findViewWithTag<View>(langTag) as? LanguageFilterView?

    fun removeLangs(langsToRemove: Set<Language>) {
        langsToRemove.forEach { lang ->
            removeView(findViewWithTag(lang))
        }
    }

    fun bindTrainingItem(
        contextWrapper: Context,
        item: TrainingItem,
        languageClickedListener: LanguageFilterClickedListener?,
    ) {
        removeAllViews()
        ensureMinimumHeight(contextWrapper, item)
        initFilterView(contextWrapper, item, languageClickedListener)
    }

    private fun ensureMinimumHeight(
        context: Context,
        item: TrainingItem,
    ) {
        if (item.trainingByLang.isEmpty()) {
            // add invisible button, which determines the height of the view
            val checkMark =
                LanguageFilterView.createLanguageRadioButtonLayout(
                    ctx = context,
                    checked = false,
                    viewTag = Language.Spanish,
                    amountTexts = 0,
                    listener = null,
                ).apply {
                    isInvisible = true
                }

            addView(checkMark)
        } else {
            doOnNextLayout { minimumHeight = height }
        }
    }

    private fun initFilterView(
        context: Context, item: TrainingItem, listener: com.example.road.to.effective.snapshot.testing.recyclerviewscreen.LanguageFilterClickedListener?,
    ) {
        item.trainingByLang.sortByValueSize().keys
            .forEach { lang ->

                val checkMark =
                    LanguageFilterView.createLanguageRadioButtonLayout(
                        ctx = context,
                        checked = item.activeLangs.contains(lang),
                        viewTag = lang,
                        amountTexts = item.trainingByLang[lang]?.size ?: 0,
                        listener = listener,
                    )

                addView(checkMark)
            }
    }

    fun animateLanguageFilters(
        contextWrapper: Context,
        trainingItem: TrainingItem,
        filterClickedListener: LanguageFilterClickedListener?,
        oldLangsSorted: Map<Language, Collection<Translation>>,
        newLangsSorted: Map<Language, Collection<Translation>>,
    ) {
        animateFilterAmounts(trainingItem, oldLangsSorted.keys, newLangsSorted.keys)
        animateFilters(
            contextWrapper,
            trainingItem,
            filterClickedListener,
            oldLangsSorted,
            newLangsSorted,
        )
    }

    private fun animateFilterAmounts(
        newTrainingItem: TrainingItem, oldLangsKeys: Set<com.example.road.to.effective.snapshot.testing.recyclerviewscreen.data.Language>, newLangsKeys: Set<com.example.road.to.effective.snapshot.testing.recyclerviewscreen.data.Language>
    ) {
        newLangsKeys.intersect(oldLangsKeys).forEach {
            val newValue = newTrainingItem.trainingByLang[it]?.size ?: 0
            getLangRadioButtonByTag(it)?.setValue(newValue)
        }
    }

    private fun animateFilters(
        context: Context,
        item: TrainingItem,
        listener: LanguageFilterClickedListener?,
        oldLangsSorted: Map<Language, Collection<Translation>>,
        newLangsSorted: Map<Language, Collection<Translation>>
    ) {
        val oldLangsKeys = oldLangsSorted.keys
        val newLangsKeys = newLangsSorted.keys

        // lists also compare item positions for equals()
        if (oldLangsKeys.toList() != newLangsKeys.toList()) {
            if (oldLangsKeys.size > newLangsKeys.size) {
                val langsToRemove = oldLangsKeys.subtract(newLangsKeys)
                removeLangs(langsToRemove)
            }

            if (oldLangsSorted.size < newLangsKeys.size) {
                val langsToAdd = newLangsKeys.subtract(oldLangsKeys)
                val langsToAddWithTranslations =
                    oldLangsSorted.filterKeys { langsToAdd.contains(it) }

                addNewLangs(
                    context = context,
                    allLangsSorted = newLangsKeys.toSortedSet(),
                    langsToAdd = langsToAddWithTranslations,
                    activeLangs = item.activeLangs,
                    listener = listener
                )

            }

            if (oldLangsSorted.size == newLangsSorted.size)
                reorderLangs(
                    oldLangsOrder = linkedSetOf(*oldLangsKeys.toTypedArray()),
                    newLangsOrder = linkedSetOf(*newLangsKeys.toTypedArray())
                )
        }
    }

    private fun reorderLangs(
        oldLangsOrder: LinkedHashSet<Language>,
        newLangsOrder: LinkedHashSet<Language>
    ) {
        val newLangWeights = newLangsOrder
            .mapIndexed { index, language ->
                Pair(language, newLangsOrder.size - index)
            }.toMap()

        val oldLangWeights = oldLangsOrder
            .mapIndexed { _, language ->
                Pair(language, newLangWeights[language])
            }.toMap()

        oldLangWeights
            .values.filterNotNull().toIntArray()
            .bubbleSortDescending(doOnSwap = { initPos, finalPos ->
                val view = get(initPos) as LanguageFilterView
                removeViewAt(initPos)
                addViewWithAnimation(view, 0, finalPos)
            })
    }
}