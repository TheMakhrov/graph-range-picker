package com.makhrov.filteringview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup


/**
 * Created on 21.03.17.
 */
open class HorizontalDraggableView : View {
  //Variable to handle dx
  private var dragX = 0f
  //Default value
  private var isOverdraggable = false

  var onDragListener: ((Float) -> Unit)? = null

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    parseArguments(attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
      attributeSet, defStyleAttr) {
    parseArguments(attributeSet)
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    parseArguments(attrs)
  }

  private fun parseArguments(attributeSet: AttributeSet) {
    val a = context.theme.obtainStyledAttributes(
        attributeSet,
        R.styleable.HorizontalDraggableView,
        0, 0)

    try {
      isOverdraggable = a.getBoolean(R.styleable.HorizontalDraggableView_overdraggable, false)
    } finally {
      a.recycle()
    }
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      dragX = event.rawX
    }
    if (event.action == MotionEvent.ACTION_MOVE) {
      val destination: Float

      if (!isOverdraggable) {
        val wantedDragValue = this.x + event.rawX - dragX
        val parentMaxX = (this.parent as ViewGroup).x + (this.parent as ViewGroup).measuredWidth - this.measuredWidth
        val parentMinX = (this.parent as ViewGroup).x

        if (wantedDragValue > parentMaxX) {
          destination = parentMaxX
        } else if (wantedDragValue < parentMinX) {
          destination = parentMinX
        } else {
          destination = this.x + event.rawX - dragX
        }
      } else {
        destination = this.x + event.rawX - dragX
      }

      this.animate()
          .x(destination)
          .setDuration(0)
          .withEndAction {
            onDragListener?.invoke(destination)
          }
          .start()

      dragX = event.rawX
    }
    return true
  }
}