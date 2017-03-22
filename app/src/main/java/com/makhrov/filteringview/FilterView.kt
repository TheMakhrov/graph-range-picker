package com.makhrov.filteringview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout

/**
 * Created on 21.03.17.
 */
class FilterView : FrameLayout {
  var onRangePickedListener: ((Pair<Int, Int>) -> Unit)? = null

  private val rangePicker: RangePickerView
  private val plotView: PlotView

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
      attributeSet, defStyleAttr)

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  init {
    View.inflate(context, R.layout.filter_view, this)
    rangePicker = findViewById(R.id.rangePicker) as RangePickerView
    plotView = findViewById(R.id.plotView) as PlotView

    plotView.plotFunction = { x ->
      plotView.measuredHeight - plotView.measuredHeight * Math.sin(x).toFloat()
    }

    //Adding padding to make range selector circles
    //in one line with selected range on plot
    val circleSize = resources.getDimension(R.dimen.range_picker_circle_size)
    val plotViewLayoutParams = plotView.layoutParams as LinearLayout.LayoutParams
    plotViewLayoutParams.leftMargin = (circleSize / 2).toInt()
    plotViewLayoutParams.rightMargin = (circleSize / 2).toInt()

    rangePicker.onRangePickedListener = { values ->
      plotView.showSelectedRange(values.first.toDouble(), values.second.toDouble())
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    rangePicker.setPickerWidth(plotView.measuredWidth)

    val pair = rangePicker.getLeftAndRightBorders()
    plotView.showSelectedRange(pair.first.toDouble(), pair.second.toDouble())
  }
}