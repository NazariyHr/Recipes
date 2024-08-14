package com.recipes.presentation.common.utils

import android.content.Context
import android.net.Uri
import java.io.IOException


@Throws(IOException::class)
fun copyFile(c: Context, src: Uri?, dst: Uri?) {
    if (src == null || dst == null) return
    val inChannel = c.contentResolver.openInputStream(src)
    val outChannel = c.contentResolver.openOutputStream(dst)
    if (inChannel == null || outChannel == null) return

    try {
        val buffer = ByteArray(1024)
        var len: Int
        while ((inChannel.read(buffer).also { len = it }) != -1) {
            outChannel.write(buffer, 0, len)
        }
    } finally {
        inChannel.close()
        outChannel.close()
    }
}

