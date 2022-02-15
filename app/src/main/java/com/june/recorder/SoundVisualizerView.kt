package com.june.recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

//See: https://developer.android.com/training/custom-views/custom-drawing
class SoundVisualizerView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    } //음향의 진폭 시각화 ANTI_ALIAS_FLAG -> 도트 이미지가 부드럽게 그려짐

    var drawingWidth: Int = 0
    var drawingHeight: Int = 0
    var drawingAmplitudes: List<Int> = (0..10).map { Random.nextInt(Short.MAX_VALUE.toInt()) } //emptyList()

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat() //public const val MAX_VALUE: Short = 32767
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) {
            return
        }

        val centerY = drawingHeight / 2f

        //오른쪽에서 왼쪽으로 이동
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes.forEach { amplitude ->
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F //꽉 채운 것보다 0.8 정도가 더 이쁨

            offsetX -= LINE_SPACE //오른쪽에서 왼쪽으로 이동
            if (offsetX < 0) { //뷰의 왼쪽 영역보다 밖으로 나간 상황
                return@forEach
            }

            canvas.drawLine(
                offsetX, centerY - lineLength / 2F,
                offsetX, centerY + lineLength / 2F,
                amplitudePaint
            )

        }

    }

}