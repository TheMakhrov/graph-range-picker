package com.makhrov.filteringview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet

/**
 * Created on 21.03.17.
 */
class DragCircle : HorizontalDraggableView {
  lateinit var circleSide: CircleSide

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    parseArguments(attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
      attributeSet, defStyleAttr) {
    parseArguments(attributeSet)
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
      : super(context, attrs, defStyleAttr, defStyleRes) {
    parseArguments(attrs)
  }

  enum class CircleSide(val number: Int) {
    LEFT(0), RIGHT(1);

    companion object {
      fun getCircleSideByNumber(number: Int): CircleSide {
        CircleSide.values().forEach {
          if (it.number == number) {
            return it
          }
        }
        //default value
        return LEFT
      }
    }
  }

  private fun parseArguments(attributeSet: AttributeSet) {
    val a = context.theme.obtainStyledAttributes(
        attributeSet,
        R.styleable.HorizontalDraggableView,
        0, 0)

    try {
      circleSide = CircleSide.getCircleSideByNumber(a.getInt(R.styleable.DragCircle_circleState, 0))
    } finally {
      a.recycle()
    }
  }
}