package com.masterproject.englishapp.vision

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream


object ImageUtils {

    fun cropBitmap(source: Bitmap, rect: Rect): Bitmap {
        val safeRect = Rect(
            rect.left.coerceAtLeast(0),
            rect.top.coerceAtLeast(0),
            rect.right.coerceAtMost(source.width),
            rect.bottom.coerceAtMost(source.height)
        )

        val width = safeRect.width().coerceAtLeast(1)
        val height = safeRect.height().coerceAtLeast(1)

        return Bitmap.createBitmap(
            source,
            safeRect.left,
            safeRect.top,
            width,
            height
        )
    }

    fun cropBitmap(source: Bitmap, rectF: RectF): Bitmap {
        val left = rectF.left.coerceAtLeast(0f).toInt()
        val top = rectF.top.coerceAtLeast(0f).toInt()
        val right = rectF.right.coerceAtMost(source.width.toFloat()).toInt()
        val bottom = rectF.bottom.coerceAtMost(source.height.toFloat()).toInt()

        return Bitmap.createBitmap(
            source,
            left,
            top,
            (right - left).coerceAtLeast(1),
            (bottom - top).coerceAtLeast(1)
        )
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, displayName: String = "debug_image") {
        val filename = "${displayName}_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            var outStream: OutputStream? = null
            try {
                outStream = resolver.openOutputStream(uri)
                if (outStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                }
            } finally {
                outStream?.close()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }

            Toast.makeText(context, "Saved to Gallery: $filename", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}