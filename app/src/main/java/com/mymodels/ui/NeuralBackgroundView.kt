package com.mymodels.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class NeuralBackgroundView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.parseColor("#55FFFFFF")
        strokeWidth = 2f
    }

    private val nodePaint = Paint().apply {
        color = Color.parseColor("#88FFFFFF")
    }

    private val nodes = mutableListOf<PointF>()
    private val random = Random(System.currentTimeMillis())

    init {
        repeat(25) {
            nodes.add(PointF(random.nextFloat() * 1000, random.nextFloat() * 2000))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in nodes.indices) {

            val p1 = nodes[i]

            for (j in i + 1 until nodes.size) {

                val p2 = nodes[j]

                val dx = p1.x - p2.x
                val dy = p1.y - p2.y

                val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

                if (distance < 250) {
                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
                }
            }

            canvas.drawCircle(p1.x, p1.y, 6f, nodePaint)

            p1.x += random.nextFloat() * 4 - 2
            p1.y += random.nextFloat() * 4 - 2
        }

        postInvalidateDelayed(40)
    }
}