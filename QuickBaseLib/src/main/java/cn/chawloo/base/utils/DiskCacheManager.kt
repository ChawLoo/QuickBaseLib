package cn.chawloo.base.utils

import android.content.Context
import android.os.Environment
import com.safframework.log.L.e
import java.io.File
import java.text.DecimalFormat

/**
 * 缓存管理工具类
 * @author Create by 鲁超 on 2021/12/6 0006 11:09:03
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
object DiskCacheManager {

    fun getCacheSize(mContext: Context, filterMinCache: Boolean = false): String {
        val cacheFile = File(mContext.cacheDir, "RxCache")
        val path = arrayOf(
            cacheFile.absolutePath,
            mContext.cacheDir.absolutePath,
            mContext.externalCacheDir?.absolutePath,
            mContext.getDir("webview", Context.MODE_PRIVATE).absolutePath
        )
        var maxSize = 0L
        path.forEach {
            it?.run {
                val file = File(this)
                val size = getDirSize(file)
                e("路径：：$this 占用大小：：$size")
                maxSize += size
            }
        }
        return if (filterMinCache && maxSize / 1024 < 50) {
            return ""
        } else {
            escapeSize(maxSize)
        }
    }

    /**
     * 清除所有缓存
     *
     * @return
     */
    fun clearAllCache(context: Context) {
        cleanExternalCache(context)
        cleanInternalCache(context)
        deleteFilesByDirectory(context.getDir("webview", Context.MODE_PRIVATE))
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    private fun cleanExternalCache(mContext: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFilesByDirectory(mContext.externalCacheDir)
        }
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    private fun cleanInternalCache(mContext: Context) {
        deleteFilesByDirectory(mContext.cacheDir)
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    private fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir.isDirectory) {
            return 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        files?.takeIf { it.isNotEmpty() }?.forEach {
            if (it.isFile) {
                dirSize += it.length()
            } else if (it.isDirectory) {
                dirSize += it.length()
                dirSize += getDirSize(it) // 递归调用继续统计
            }
        }
        return dirSize
    }

    /**
     * 获取文件大小
     *
     * @param size 字节
     * @return
     */
    private fun escapeSize(size: Long): String {
        if (size <= 0) {
            return "0 KB"
        }
        val df = DecimalFormat("##.##")
        val temp = size.toFloat() / 1024
        return if (temp >= 1024) {
            df.format((temp / 1024).toDouble()) + " MB"
        } else {
            df.format(temp.toDouble()) + " KB"
        }
    }

    /**
     * 删除目标文件目录下的所有子文件
     *
     * @param directory 目录文件夹
     */
    private fun deleteFilesByDirectory(directory: File?) {
        if (directory != null && directory.exists() && directory.isDirectory) {
            directory.listFiles()?.takeIf { it.isNotEmpty() }?.forEach {
                recursionDeleteFile(it)
            }
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    private fun recursionDeleteFile(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }
        if (file.isDirectory) {
            val childFile = file.listFiles()
            if (childFile == null || childFile.isEmpty()) {
                file.delete()
                return
            }
            for (f in childFile) {
                recursionDeleteFile(f)
            }
            file.delete()
        }
    }
}