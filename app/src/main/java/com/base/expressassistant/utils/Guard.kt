package com.base.expressassistant.utils

import android.content.Context
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier

object Guard {
    fun isRegister(context: Context): Boolean {
        val key = createKey()
        val uniqueID = getUniqueID(context)
        return key == uniqueID
    }

    fun getUniqueID(context: Context): String {
        val supported = DeviceID.supportedOAID(context)
        var uniqueID: String
        if (supported)
            uniqueID = DeviceIdentifier.getOAID(context) // OAID或AAID
        else {
            uniqueID = DeviceIdentifier.getWidevineID() //数字版权ID
            if (uniqueID.isEmpty()) {
                uniqueID = DeviceIdentifier.getAndroidID(context) // 安卓ID
                if (uniqueID.isEmpty())
                    uniqueID = DeviceIdentifier.getPseudoID() // 都没有的情况，使用硬件伪造ID，但有较大概率重复
            }
        }
        return uniqueID
    }

    private fun createKey(): String {
        return ""
    }
}
