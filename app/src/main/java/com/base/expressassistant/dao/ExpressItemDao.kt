package com.base.expressassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.base.expressassistant.entities.ExpressItem
import java.util.Date

@Dao
interface ExpressItemDao {
    @Query("SELECT * FROM express_items")
    fun getAll(): List<ExpressItem>

    @Query(
        "SELECT * FROM express_items " +
                "WHERE (:address = '' OR address = :address) " +
                "AND (:code = '' OR code = :code) " +
                "AND (:phoneNumber = '' OR phoneNumber = :phoneNumber) " +
                "AND (:name = '' OR name = :name) " +
                "AND (:beginDate IS NULL OR createDate <= :beginDate) " +
                "AND (:endDate IS NULL OR createDate >= :endDate)" +
                "ORDER BY status ASC, createDate DESC"
    )
    fun getItems(
        address: String?,
        code: String?,
        phoneNumber: String?,
        name: String?,
        beginDate: Date?,
        endDate: Date?
    ): List<ExpressItem>

    @Update
    fun update(item: ExpressItem)

    @Insert
    fun insertAll(vararg items: ExpressItem)

    @Delete
    fun delete(item: ExpressItem)

    @Query("DELETE FROM express_items")
    fun deleteAll()
}
