package com.base.expressassistant.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import com.base.expressassistant.entities.ExpressItem
import com.base.expressassistant.utils.Formatter

@Composable
fun MinimalDialog(text: String, onDismissRequest: (() -> Unit)?) {
    Dialog(onDismissRequest = { onDismissRequest?.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "提示图标")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("取消")
            }
        }
    )
}


@Composable
fun DetailDialog(item: ExpressItem, onDismissRequest: (() -> Unit)?) {
    Dialog(onDismissRequest = { onDismissRequest?.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "地址: ${item.address}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                Text(
                    text = "编号: ${item.code}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                Text(
                    text = "姓名: ${item.name}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                Text(
                    text = "电话: ${item.phoneNumber}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                Text(
                    text = "收货：${Formatter.dateFormat(item.createDate)}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
                Text(
                    text = "签收：${Formatter.dateFormat(item.finishDate)}",
                    modifier = Modifier
                        .padding(8.dp, 2.dp)
                )
            }
        }
    }
}
