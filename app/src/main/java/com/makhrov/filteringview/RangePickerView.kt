package com.makhrov.filteringview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * Created on 21.03.17.
 */
class RangePickerView : FrameLayout {
  var onRangePickedListener: ((Pair<Float, Float>) -> Unit)? = null

  private val firstCircle: DragCircle
  private val secondCircle: DragCircle
  private val selectedRangeIndicator: FrameLayout
  private val rangeIndicator: FrameLayout

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
      attributeSet, defStyleAttr)

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  init {
    View.inflate(context, R.layout.range_picker_view, this)
    firstCircle = findViewById(R.id.firstCircle) as DragCircle
    secondCircle = findViewById(R.id.secondCircle) as DragCircle
    selectedRangeIndicator = findViewById(R.id.selectedRangeIndicator) as FrameLayout
    rangeIndicator = findViewById(R.id.rangeIndicator) as FrameLayout

    firstCircle.onDragListener = {
      updateSelectedRange()
      onRangePickedListener?.invoke(getLeftAndRightBorders())
    }

    secondCircle.onDragListener = {
      updateSelectedRange()
      onRangePickedListener?.invoke(getLeftAndRightBorders())
    }
  }

  fun setPickerWidth(width: Int) {
    val layoutParams = rangeIndicator.layoutParams as FrameLayout.LayoutParams
    layoutParams.width = width
    rangeIndicator.layoutParams = layoutParams
  }

  fun getLeftAndRightBorders(): Pair<Float, Float> {
    if (firstCircle.circleSide == DragCircle.CircleSide.LEFT) {
      val left = firstCircle.x
      val right = secondCircle.x
      return left / rangeIndicator.measuredWidth to right / rangeIndicator.measuredWidth
    } else {
      val left = secondCircle.x
      val right = firstCircle.x
      return left / rangeIndicator.measuredWidth to right / rangeIndicator.measuredWidth
    }
  }

  private fun updateSelectedRange() {
    if (firstCircle.x > secondCircle.x) {
      firstCircle.circleSide = DragCircle.CircleSide.RIGHT
      secondCircle.circleSide = DragCircle.CircleSide.LEFT
    } else {
      firstCircle.circleSide = DragCircle.CircleSide.LEFT
      secondCircle.circleSide = DragCircle.CircleSide.RIGHT
    }

    val pair = getLeftAndRightBorders()
    selectedRangeIndicator.layout((pair.first * rangeIndicator.measuredWidth).toInt(), selectedRangeIndicator.top,
        (pair.second * rangeIndicator.measuredWidth).toInt(), selectedRangeIndicator.bottom)
  }
}