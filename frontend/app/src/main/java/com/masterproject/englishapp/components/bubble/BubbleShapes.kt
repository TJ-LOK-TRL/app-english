package com.masterproject.englishapp.components.bubble

import androidx.compose.foundation.shape.GenericShape

enum class Side {
    Bottom, Left, Right, Top
}

fun bubbleShape(side: Side, tipSize: Float = 20f): GenericShape {
    return GenericShape { size, _ ->
        when (side) {

            Side.Bottom -> {
                // Tip on the bottom, extending outside the normal height
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)

                lineTo(size.width / 2 + tipSize, size.height)
                lineTo(size.width / 2, size.height + tipSize) // tip outside
                lineTo(size.width / 2 - tipSize, size.height)

                lineTo(0f, size.height)
                close()
            }

            Side.Top -> {
                // Tip on the top, extending outside the normal height
                moveTo(0f, 0f)

                lineTo(size.width / 2 - tipSize, 0f)
                lineTo(size.width / 2, -tipSize) // tip outside
                lineTo(size.width / 2 + tipSize, 0f)

                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            Side.Left -> {
                // Tip on the left, extending outside the normal width
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)

                lineTo(0f, size.height / 2 + tipSize)
                lineTo(-tipSize, size.height / 2) // tip outside
                lineTo(0f, size.height / 2 - tipSize)

                close()
            }

            Side.Right -> {
                // Tip on the right, extending outside the normal width
                moveTo(0f, 0f)
                lineTo(size.width, 0f)

                lineTo(size.width, size.height / 2 - tipSize)
                lineTo(size.width + tipSize, size.height / 2) // tip outside
                lineTo(size.width, size.height / 2 + tipSize)

                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
        }
    }
}
