package com.mymodels.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class NeuralBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val nodePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#60A5FA")
        style = Paint.Style.FILL
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2563EB")
        strokeWidth = 2f
    }

    private val nodes = mutableListOf<Node>()

    private val handler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            updateNodes()
            invalidate()
            handler.postDelayed(this, 30)
        }
    }

    data class Node(
        var x: Float,
        var y: Float,
        var dx: Float,
        var dy: Float
    )

    init {
        createNodes()
        handler.post(updateRunnable)
    }

    private fun createNodes() {

        if (nodes.isNotEmpty()) return

        for (i in 0 until 25) {

            nodes.add(
                Node(
                    Random.nextFloat() * 1000,
                    Random.nextFloat() * 1800,
                    Random.nextFloat() * 2 - 1,
                    Random.nextFloat() * 2 - 1
                )
            )
        }
    }

    private fun updateNodes() {

        val w = width
        val h = height

        if (w == 0 || h == 0) return

        for (node in nodes) {

            node.x += node.dx
            node.y += node.dy

            if (node.x < 0 || node.x > w) node.dx *= -1
            if (node.y < 0 || node.y > h) node.dy *= -1
        }
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        if (nodes.isEmpty()) return

        for (i in nodes.indices) {

            val n1 = nodes[i]

            canvas.drawCircle(n1.x, n1.y, 5f, nodePaint)

            for (j in i + 1 until nodes.size) {

                val n2 = nodes[j]

                val dx = n1.x - n2.x
                val dy = n1.y - n2.y

                val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

                if (distance < 200) {

                    canvas.drawLine(
                        n1.x,
                        n1.y,
                        n2.x,
                        n2.y,
                        linePaint
                    )
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(updateRunnable)
    }
}