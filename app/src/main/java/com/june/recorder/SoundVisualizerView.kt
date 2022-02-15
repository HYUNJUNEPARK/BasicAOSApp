package com.june.recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class SoundVisualizerView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat() //public const val MAX_VALUE: Short = 32767
    }

    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    var drawingAmplitudesList: List<Int> = (0..10).map { Random.nextInt(Short.MAX_VALUE.toInt()) } //emptyList()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        val centerY = drawingHeight / 2f //View 의 Y축 중앙
        var offsetX = drawingWidth.toFloat() //View 의 X축 가장 오른쪽(끝)

        drawingAmplitudesList.forEach { amplitude ->
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F //화면 크기에 맞춰 진폭값(X축) 조정

            offsetX -= LINE_SPACE //오른쪽에서 LINE_SPACE 만큼 왼쪽으로 이동하며 생기는 X축 좌표
            if (offsetX < 0) return@forEach //가장 왼쪽(끝)을 벗어난 X축 좌표

            canvas.drawLine(
                /*startXY*/offsetX, centerY - (lineLength / 2F),
                /*stopXY */offsetX, centerY + (lineLength / 2F),
                /*paint  */amplitudePaint
            )

        }

    }
}