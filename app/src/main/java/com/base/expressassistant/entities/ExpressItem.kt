package com.base.expressassistant.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "express_items")
data class ExpressItem(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String?,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "remark") val remark: String?,
    @ColumnInfo(name = "status") var status: Int, // 0:未签收 1：已签收
    @ColumnInfo(name = "createDate") val createDate: Date?,
    @ColumnInfo(name = "finishDate") var finishDate: Date?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}
