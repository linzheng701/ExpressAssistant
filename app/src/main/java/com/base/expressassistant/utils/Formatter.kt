package com.base.expressassistant.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatter {
    /*
        日期格式化
     */
    fun dateFormat(date: Date?): String {
        if (date == null)
            return "日期为空"
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date)
    }
}
