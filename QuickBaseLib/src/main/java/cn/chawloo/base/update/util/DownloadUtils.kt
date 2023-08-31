package cn.chawloo.base.update.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

/**
 * TODO
 * @author Create by 鲁超 on 2022/4/19 0019 15:40:56
 *----------Dragon be here!----------/
 *       ┌─┐      ┌─┐
 *     ┌─┘─┴──────┘─┴─┐
 *     │              │
 *     │      ─       │
 *     │  ┬─┘   └─┬   │
 *     │              │
 *     │      ┴       │
 *     │              │
 *     └───┐      ┌───┘
 *         │      │神兽保佑
 *         │      │代码无BUG！
 *         │      └──────┐
 *         │             ├┐
 *         │             ┌┘
 *         └┐ ┐ ┌───┬─┐ ┌┘
 *          │ ┤ ┤   │ ┤ ┤
 *          └─┴─┘   └─┴─┘
 *─────────────神兽出没───────────────/
 */
object DownloadUtils {
    fun downloadBySystem(context: Context, url: String, title: String, desc: String): Long {
        val uri = Uri.parse(url)
        val req = DownloadManager.Request(uri)
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, title)
        req.setTitle(title)
        req.setDescription(desc)
        req.setMimeType("application/vnd.android.package-archive")
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return dm.enqueue(req)
    }

    fun clearCurrentTask(context: Context, id: Long) {
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try {
            dm.remove(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}