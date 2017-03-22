package com.makhrov.filteringview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created on 21.03.17.
 */
class PlotView : FrameLayout {
  //Number of pixels to draw path element
  private val N = 2

  //Function that returns y of plot based on x
  lateinit var plotFunction: (Double) -> Float

  private val selectedRangePaint = Paint()
  private val selectedPath = Path()
  private val unselectedPath = Path()
  private val unselectedRangePaint = Paint()

  private var leftSelectedBound = 0f
  private var rightSelectedBound = 0f

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
      attributeSet, defStyleAttr)

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  fun showSelectedRange(left: Double, right: Double) {
    leftSelectedBound = Math.floor(left * measuredWidth).toFloat()
    rightSelectedBound = Math.ceil(right * measuredWidth).toFloat()
    invalidate()
  }

  init {
    selectedRangePaint.color = ContextCompat.getColor(context, R.color.selected_plot_color)
    selectedRangePaint.style = Paint.Style.FILL
    selectedRangePaint.strokeWidth = 1f
    selectedRangePaint.isAntiAlias = true

    unselectedRangePaint.color = ContextCompat.getColor(context, R.color.unselected_plot_color)
    unselectedRangePaint.style = Paint.Style.FILL
    unselectedRangePaint.strokeWidth = 1f
    unselectedRangePaint.isAntiAlias = true
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    //Drawing left part of unselected path
    unselectedPath.moveTo(0f, canvas.height.toFloat())
    for (i in 0..leftSelectedBound.toInt() / N) {
      unselectedPath.lineTo(i.toFloat() * N, plotFunction(i * N * Math.PI / measuredWidth))
    }
    unselectedPath.lineTo(leftSelectedBound, canvas.height.toFloat())
    unselectedPath.close()
    canvas.drawPath(unselectedPath, unselectedRangePaint)
    unselectedPath.rewind()

    //Drawing selected path
    selectedPath.moveTo(leftSelectedBound, canvas.height.toFloat())
    for (i in leftSelectedBound.toInt() / N..rightSelectedBound.toInt() / N) {
      selectedPath.lineTo(i.toFloat() * N, plotFunction(i * N * Math.PI / measuredWidth))
    }
    selectedPath.lineTo(rightSelectedBound, canvas.height.toFloat())
    selectedPath.close()
    canvas.drawPath(selectedPath, selectedRangePaint)
    selectedPath.rewind()

    //Drawing right part of unselected path
    unselectedPath.moveTo(rightSelectedBound, canvas.height.toFloat())
    for (i in rightSelectedBound.toInt() / N..canvas.width / N) {
      unselectedPath.lineTo(i.toFloat() * N, plotFunction(i * N * Math.PI / measuredWidth))
    }
    unselectedPath.lineTo(canvas.width.toFloat(), canvas.height.toFloat())
    unselectedPath.close()
    canvas.drawPath(unselectedPath, unselectedRangePaint)
    unselectedPath.rewind()
  }
}