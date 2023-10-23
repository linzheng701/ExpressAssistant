package com.base.expressassistant.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.base.expressassistant.dao.ExpressItemDao
import com.base.expressassistant.entities.ExpressItem

@Database(entities = [ExpressItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expressItemDao(): ExpressItemDao
}
