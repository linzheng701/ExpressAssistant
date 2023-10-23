package com.base.expressassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.base.expressassistant.MainApplication
import com.base.expressassistant.entities.ExpressItem
import com.base.expressassistant.utils.Formatter
import java.util.Calendar
import java.util.Date

@Composable
fun ListScreen(
    address: String,
    code: String,
    phoneNumber: String,
    name: String,
    all: Boolean = false
) {
    val expressItemDao = MainApplication.appDatabase.expressItemDao()
    var beginDate: Date? = null
    var endDate: Date? = null
    if (!all) {
        // 默认获取最近七天数据
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        beginDate = Date()
        endDate = calendar.time
    }
    val data = expressItemDao.getItems(address, code, phoneNumber, name, beginDate, endDate)
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(data.size) { index ->
            ExpressItemCard(data[index])
        }
    }
}

@Composable
fun ExpressItemCard(
    item: ExpressItem
) {
    var openDetailDialog by remember { mutableStateOf(false) }
    var status by remember { mutableIntStateOf(0) }
    status = item.status

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
                .height(168.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    text = item.address,
                    modifier = Modifier
                        .padding(8.dp),
                )
                Text(
                    text = "编号: ${item.code}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                Text(
                    text = Formatter.dateFormat(item.createDate),
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                TextButton(
                    onClick = {
                        openDetailDialog = true
                    }
                ) {
                    Text("详情")
                }
            }
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            color = if (status == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                        )
                        .clickable {
                            if (status == 0) {
                                status = 1
                                item.status = 1
                                item.finishDate = Date()
                            } else {
                                status = 0
                                item.status = 0
                            }
                            MainApplication.appDatabase
                                .expressItemDao()
                                .update(item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        text = if (status == 0) "完成" else "取消",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
    when {
        openDetailDialog -> {
            DetailDialog(item) {
                openDetailDialog = false
            }
        }
    }
}
