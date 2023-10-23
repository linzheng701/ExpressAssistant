package com.base.expressassistant.utils

import android.content.Context
import com.base.expressassistant.MainApplication
import com.base.expressassistant.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DBUtil {

    /**
     * 数据库备份目录
     */
    const val BackupDirName = "backup"

    /**
     * 备份文件日期格式
     */
    private const val BackupDateFormat = "yyyy-MM-dd"

    /**
     * 备份文件后缀
     */
    private const val BackupFileSuffix = ".db"

    /**
     * 恢复数据库
     */
    fun restore(context: Context) {
        MainApplication.appDatabase.close()

        val backupDir = File(context.getExternalFilesDir(null), BackupDirName)
        val files = backupDir.listFiles()
        var lastFile: File? = null
        var lastModified = Long.MAX_VALUE
        if (files != null) {
            for (file in files) {
                val currentModified = file.lastModified()
                if (currentModified < lastModified) {
                    lastModified = currentModified
                    lastFile = file
                }
            }
        }
        lastFile?.apply {
            val databaseFile =
                context.getDatabasePath(context.resources.getString(R.string.database_name))
            copyTo(databaseFile, true)
        }
    }

    /**
     * 备份数据库
     */
    fun backup(context: Context) {
        MainApplication.appDatabase.close()

        val databaseFile =
            context.getDatabasePath(context.resources.getString(R.string.database_name))
        val backupDir = File(context.getExternalFilesDir(null), BackupDirName)
        clearExpiresBackup(backupDir)
        if (!databaseFile.exists())
            return
        backupToFile(databaseFile, backupDir)
    }

    /**
     * 数据库备份到文件
     */
    private fun backupToFile(databaseFile: File, backupDir: File): Boolean {
        val date = Date()
        val formatter = SimpleDateFormat(BackupDateFormat, Locale.getDefault())
        val formattedDate = formatter.format(date)
        val fileName = formattedDate + BackupFileSuffix
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
        val backupFile = File(backupDir, fileName)
        databaseFile.copyTo(backupFile, true)
        return false
    }

    /**
     * 清除过期备份
     */
    private fun clearExpiresBackup(backupDir: File) {
        val files = backupDir.listFiles()
        if (files != null) {
            for (file in files) {
                val lastModified = file.lastModified()
                val currentTime = System.currentTimeMillis()
                val timeDifference = currentTime - lastModified
                val daysDifference = timeDifference / (1000 * 60 * 60 * 24)
                if (daysDifference > 7) { //删除大于七天的备份文件
                    file.delete()
                }
            }
        }
    }
}
