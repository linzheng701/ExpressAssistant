package com.base.expressassistant

import android.app.Application
import androidx.room.Room
import com.base.expressassistant.utils.AppDatabase
import com.base.expressassistant.utils.DBUtil

class MainApplication : Application() {
    companion object {
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        // 创建数据库
        appDatabase =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                applicationContext.resources.getString(R.string.database_name)
            )
                .allowMainThreadQueries()
                .build()

        // 数据库备份
        DBUtil.backup(applicationContext)
    }
}
