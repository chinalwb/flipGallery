package com.chinalwb.flipgallery.library

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import com.chinalwb.flipgallery.R
import kotlin.math.*

class FlipGallery (context: Context, attributeSet: AttributeSet) : View (context, attributeSet) {

    companion object {
        private const val NINETY_DEGREE = 89.99F
    }

    var flipDuration = 1000L
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var camera = Camera()
    private var cx = 0F
    private var cy = 0F

    private var velocityTracker = VelocityTracker.obtain()
    private var viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
    // Default
    private var upDegree0 = 0F
        set(value) {
            field = value
            invalidate()
        }
    private var upDegree1 = 0F
        set(value) {
            field = value
            invalidate()
        }
    private var upDegree2 = -180F
        set(value) {
            field = value
            invalidate()
        }

    private var downDegree0 = 180F
        set(value) {
            field = value
            invalidate()
        }
    private var downDegree1 = 0F
        set(value) {
            field = value
            invalidate()
        }
    private var downDegree2 = 0F
        set(value) {
            field = value
            invalidate()
        }


//    private var upAlpha = arrayOf(0, 0, 255)
//    private var downAlpha = arrayOf(255, 0, 255)

    private var upAlpha0 = 0
        set(value) {
            field = value
            invalidate()
        }
    private var upAlpha1 = 255
        set(value) {
            field = value
            invalidate()
        }
    private var upAlpha2 = 255
        set(value) {
            field = value
            invalidate()
        }

    private var downAlpha0 = 255
        set(value) {
            field = value
            invalidate()
        }
    private var downAlpha1 = 255
        set(value) {
            field = value
            invalidate()
        }
    private var downAlpha2 = 255
        set(value) {
            field = value
            invalidate()
        }

    private var index = 0
    private var max = 3
    private var bitmaps: Array<Bitmap?> = arrayOfNulls(3)

    init {
        // -200 这个z轴高度实在尴尬
        // 因为在计算bitmap rotateX 之后在 X 轴的投影距离遇到了困难
        // 目前用的 cos(θ) 代替
        // 较大的数字可以减少投影和 cos(θ)的差距
        camera.setLocation(0F, 0F, -200 * context.resources.displayMetrics.density)
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.strokeWidth = Utils.dp2px(2)
        paint.textSize = Utils.dp2px(16)

//        postDelayed({ animationPrev() }, 2000)
//        postDelayed({ animationNext() }, 5000)
//        postDelayed({ animationNext() }, 8000)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmaps[0] = Utils.getBitmap(context.resources, R.drawable.y, width)
        bitmaps[1] = Utils.getBitmap(context.resources, R.drawable.x, width)
        bitmaps[2] = Utils.getBitmap(context.resources, R.drawable.z, width)

        cx = width / 2F
        cy = height / 2F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (index == 0 && upDegree1 < 0) {
            paint.alpha = 255
            canvas!!.drawText("当前是第一张图片", cx, cy / 2, paint)
        }

        drawPreviousBitmap(canvas)

        drawCurrentBitmap(canvas)

        drawNextBitmap(canvas)

        if (index == max - 1 && downDegree1 > 45) {
            paint.alpha = 255
            canvas!!.drawText("当前是最后一张图片", cx, cy + cy / 2, paint)
        }
    }

    private fun drawPreviousBitmap(canvas: Canvas?) {
        if (index == 0) {
            return
        }
        // bitmap previous
        val previousBitmap = bitmaps[index - 1]
        var top1 = (height - previousBitmap!!.height) / 2F
        if (abs(upDegree1) > 0 || abs(downDegree1) > 90) {
            paint.alpha = upAlpha0
            canvas!!.save()
            canvas.translate(cx, cy)
            camera.save()
            camera.rotateX(upDegree0)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-cx, -cy, cx, 0F)
            canvas.translate(-cx, -cy)
            canvas.drawBitmap(previousBitmap, 0F, top1, paint)
            canvas.restore()
        }

        // bitmap previous
        // bitmap previous lower
        if (abs(upDegree1) > NINETY_DEGREE) {
            paint.alpha = downAlpha0
            canvas!!.save()
            canvas.translate(cx, cy)
            camera.save()
            camera.rotateX(downDegree0)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-cx, 0F, cx, cy)
            canvas.translate(-cx, -cy)
            canvas.drawBitmap(previousBitmap, 0F, top1, paint)
            canvas.restore()
        }
    }

    private fun drawCurrentBitmap(canvas: Canvas?) {
        if (index < 0) {
            return
        }
        // bitmap current
        // bitmap current upper
        paint.alpha = upAlpha1
        val currentBitmap = bitmaps[index]
        var top = (height - currentBitmap!!.height) / 2F


        if (upDegree1 < -NINETY_DEGREE) {
            upDegree1 = -90F
        }

        canvas!!.save()
        canvas.translate(cx, cy)
        camera.save()
        camera.rotateX(upDegree1)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-cx, -cy, cx, 0F)
        canvas.translate(-cx, -cy)
        canvas.drawBitmap(currentBitmap!!, 0F, top, paint)
        canvas.restore()

        // currentBitmap lower
        if (downDegree1 < 90) {
            paint.alpha = downAlpha1
            var bottomTopOffset = 0F
            if (downDegree0 < 90) {
                var halfHeight = if (index > 0) { bitmaps[index - 1]!!.height / 2F } else { cy }
                bottomTopOffset = halfHeight * (cos(Math.toRadians(downDegree0.toDouble())).toFloat())
            }
            canvas!!.save()
            canvas.translate(cx, cy)
            camera.save()
            camera.rotateX(downDegree1)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-cx, bottomTopOffset, cx, cy)
            canvas.translate(-cx, -cy)
            canvas.drawBitmap(currentBitmap!!, 0F, top, paint)
            canvas.restore()
        }
    }

    private fun drawNextBitmap(canvas: Canvas?) {
        if (index == max - 1) {
            return
        }
        // bitmap next
        val nextBitmap = bitmaps[index + 1]
        var top = (height - nextBitmap!!.height) / 2F
        if (downDegree1 > NINETY_DEGREE) {
            paint.alpha = upAlpha2
            canvas!!.save()
            canvas.translate(cx, cy)
            camera.save()
            camera.rotateX(upDegree2)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-cx, -cy, cx, 0F)
            canvas.translate(-cx, -cy)
            canvas.drawBitmap(nextBitmap, 0F, top, paint)
            canvas.restore()
        }

        // bitmap next
        // bitmap next lower
        if (downDegree1 > 0) {
            var halfHeight = bitmaps[index]!!.height / 2F
            var bottomTopOffset = halfHeight * cos(Math.toRadians(abs(downDegree1.toDouble()))).toFloat()
            if (bottomTopOffset < 0) {
                bottomTopOffset = 0F
            }
            paint.alpha = downAlpha2
            canvas!!.save()
            canvas.translate(cx, cy)
            camera.save()
            camera.rotateX(downDegree2)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-cx, bottomTopOffset, cx, cy)
            canvas.translate(-cx, -cy)
            canvas.drawBitmap(nextBitmap, 0F, top, paint)
            canvas.restore()
        }
    }

    private var downY = 0F
    private var dy = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)

        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetY = event.y - downY
                if (dy == 0F) {
                    dy = offsetY
                }
                if (abs(offsetY) > viewConfiguration.scaledPagingTouchSlop) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }

                if (offsetY > 0 && dy > 0) {
                    // 从上往下翻 -- 上一页
                    upDegree1 = - offsetY / cy * 180
                    if (index > 0) {
                        var absDegree = abs(upDegree1)
                        if (absDegree > 90) {
                            downAlpha1 = ((180 - absDegree) / 90 * 255).toInt() // 255 -> 0
                            absDegree = 90F
                        }
                        upAlpha0 = (absDegree / 90 * 255).toInt() // 0 -> 255
                        downDegree0 = upDegree1 + 180
                        if (downDegree0 < 0) {
                            downDegree0= 0F
                        }
                    }
                    if (index == 0) {
                        upDegree1 = max(upDegree1, -60F)
                    }
                    if (upDegree1 < -90) {
                        upDegree1 = -90F
                    }
                }

                if (offsetY < 0 && dy < 0) {
                    // 从下往上翻 -- 下一页
                    downDegree1 = - offsetY / cy * 180
                    if (index < max - 1) {
                        var absDegree = abs(downDegree1)
                        if (absDegree > 90) {
                            upAlpha1 = 255 - ((absDegree - 90) / 90 * 255).toInt() // 255 -> 0
                            absDegree = 90F
                        }
                        downAlpha2 = abs((absDegree / 90 * 255).toInt()) // 0 -> 255
                        upDegree2 = downDegree1 - 180
                        if (upDegree2 > 0) {
                            upDegree2 = 0F
                        }
                    }
                    if (index == max - 1) {
                        downDegree1 = min(downDegree1, 60F)
                    }
                    if (downDegree1 > 90) {
                        downDegree1 = 90F
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000, viewConfiguration
                    .scaledMaximumFlingVelocity.toFloat())
                var yVelocity = velocityTracker.yVelocity
                if (abs(yVelocity) < viewConfiguration.scaledMinimumFlingVelocity) {
                    if (abs(upDegree1) > NINETY_DEGREE) {
                        animationPrev()
                    } else if (abs(upDegree1) > 0) {
                        animationPrevReset()
                    }

                    if (abs(downDegree1) > NINETY_DEGREE) {
                        animationNext()
                    } else if (abs(downDegree1) > 0) {
                        animationNextReset()
                    }
                } else {
                    if (yVelocity > 0) {
                        if (index == 0) {
                            animationPrevReset()
                        } else {
                            if (dy > 0) {
                                animationPrev()
                            } else {
                                // 用户先从下往上滑动 (翻到下一页的动作)
                                // 然后手指不离开屏幕, 快速从上往下滑动
                                // 此时 dy (最初移动方向) 是小于 0 的(即从下往上滑动的情况)
                                // 应该恢复滑动到下一页的状态
                                animationNextReset()
                            }
                        }
                    } else {
                        if (index == max - 1) {
                            animationNextReset()
                        } else {
                            if (dy < 0) {
                                animationNext()
                            } else {
                                // 用户先从上往下滑动 (翻到上一页的动作)
                                // 然后手指不离开屏幕, 快速从下往上滑动
                                // 此时 dy (最初移动方向) 是大于 0 的(即从上往下滑动的情况)
                                // 应该恢复滑动到上一页的状态
                                animationPrevReset()
                            }
                        }
                    }
                }

                dy = 0F
            }
        }
        return true
    }

    private var prevAnimator: ObjectAnimator? = null
    private var prevAnimatorReset: ObjectAnimator? = null
    private var nextAnimator: ObjectAnimator? = null
    private var nextAnimatorReset: ObjectAnimator? = null

    private var prevAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            if (index > 0) {
                index--
            }
            reset()
        }
    }

    private var prevAnimatorResetListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            reset()
        }
    }

    private var nextAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            if (index < max - 1) {
                index++
            }
            reset()
        }
    }

    private var nextAnimatorListenerReset = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            reset()
        }
    }


    /**
     * 翻到上一页, 动画
     */
    private fun animationPrev() {
        if (prevAnimator == null) {
            val upDegree1ValueHolder = PropertyValuesHolder.ofFloat("upDegree1", upDegree1, -180F)
            val downDegree0ValueHolder = PropertyValuesHolder.ofFloat("downDegree0", downDegree0, 0F)

            val upAlpha0ValueHolder = PropertyValuesHolder.ofInt("upAlpha0", upAlpha0, 255)
            val downAlpha1ValueHolder = PropertyValuesHolder.ofInt("downAlpha1", downAlpha1, 0)

            prevAnimator = ObjectAnimator.ofPropertyValuesHolder(
                this,
                upDegree1ValueHolder,
                downDegree0ValueHolder,
                upAlpha0ValueHolder,
                downAlpha1ValueHolder
            )
            prevAnimator!!.addListener(prevAnimatorListener)
        }

        prevAnimator!!.duration = flipDuration
        prevAnimator!!.start()
    }

    /**
     * 翻到上一页, 复原动画
     */
    private fun animationPrevReset() {
        if (prevAnimatorReset == null) {
            val upDegree1ValueHolder = PropertyValuesHolder.ofFloat("upDegree1", upDegree1, 0F)
            val downDegree0ValueHolder = PropertyValuesHolder.ofFloat("downDegree0", downDegree0, 180F)

            val upAlpha0ValueHolder = PropertyValuesHolder.ofInt("upAlpha0", upAlpha0, 0)

            prevAnimatorReset = ObjectAnimator.ofPropertyValuesHolder(
                this,
                upDegree1ValueHolder,
                downDegree0ValueHolder,
                upAlpha0ValueHolder
            )
            prevAnimatorReset!!.addListener(prevAnimatorResetListener)
        }

        prevAnimatorReset!!.duration = flipDuration
        prevAnimatorReset!!.start()
    }

    /**
     * 翻到下一页, 动画
     */
    private fun animationNext() {
        if (nextAnimator == null) {
            val downDegree1ValueHolder = PropertyValuesHolder.ofFloat("downDegree1", downDegree1, 180F)
            val upDegree2ValueHolder = PropertyValuesHolder.ofFloat("upDegree2", upDegree2, 0F)

            val upAlpha1ValueHolder = PropertyValuesHolder.ofInt("upAlpha1", upAlpha1, 0)

            nextAnimator = ObjectAnimator.ofPropertyValuesHolder(
                this,
                downDegree1ValueHolder,
                upDegree2ValueHolder,
                upAlpha1ValueHolder
            )
            nextAnimator!!.addListener(nextAnimatorListener)
        }

        nextAnimator!!.duration = flipDuration
        nextAnimator!!.start()
    }

    /**
     * 翻到下一页, 复原动画
     */
    private fun animationNextReset() {
        if (nextAnimatorReset == null) {
            val downDegree1ValueHolder = PropertyValuesHolder.ofFloat("downDegree1", downDegree1, 0F)
            val upDegree2ValueHolder = PropertyValuesHolder.ofFloat("upDegree2", upDegree2, -180F)

            val downAlpha2ValueHolder = PropertyValuesHolder.ofInt("downAlpha2", downAlpha2, 0)

            nextAnimatorReset = ObjectAnimator.ofPropertyValuesHolder(
                this,
                downDegree1ValueHolder,
                upDegree2ValueHolder,
                downAlpha2ValueHolder
            )
            nextAnimatorReset!!.addListener(nextAnimatorListenerReset)
        }

        nextAnimatorReset!!.duration = flipDuration
        nextAnimatorReset!!.start()
    }

    private fun reset() {
        // Reset degree
        upDegree0 = 0F
        upDegree1 = 0F
        upDegree2 = -180F

        downDegree0 = 180F
        downDegree1 = 0F
        downDegree2 = 0F


        // Reset alpha
        upAlpha0 = 0
        upAlpha1 = 255
        upAlpha2 = 255

        downAlpha0 = 255
        downAlpha1 = 255
        downAlpha2 = 255
    }
}