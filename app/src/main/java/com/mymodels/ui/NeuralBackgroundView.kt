package com.mymodels.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class NeuralBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paintNode = Paint().apply {
        color = Color.parseColor("#60A5FA")
        isAntiAlias = true
    }

    private val paintLine = Paint().apply {
        color = Color.parseColor("#2563EB")
        strokeWidth = 2f
        isAntiAlias = true
    }

    private val nodes = mutableListOf<Pair<Float, Float>>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        nodes.clear()

        if (w == 0 || h == 0) return

        repeat(20) {
            nodes.add(
                Pair(
                    Random.nextFloat() * w,
                    Random.nextFloat() * h
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (nodes.isEmpty()) return

        for (i in nodes.indices) {

            val (x1, y1) = nodes[i]

            canvas.drawCircle(x1, y1, 6f, paintNode)

            for (j in i + 1 until nodes.size) {

                val (x2, y2) = nodes[j]

                val dx = x1 - x2
                val dy = y1 - y2
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

                if (distance < 200) {
                    canvas.drawLine(x1, y1, x2, y2, paintLine)
                }
            }
        }
    }
}