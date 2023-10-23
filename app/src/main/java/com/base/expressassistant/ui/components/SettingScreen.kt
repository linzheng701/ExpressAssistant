package com.base.expressassistant.ui.components

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.base.expressassistant.MainApplication
import com.base.expressassistant.R
import com.base.expressassistant.utils.DBUtil
import com.base.expressassistant.utils.Guard
import java.io.File

@Composable
fun SettingScreen(
) {
    val context = LocalContext.current
    val packInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val openTipDialog = remember { mutableStateOf(false) }
    val tipDialogContent = remember {
        mutableStateOf("")
    }
    val openClearDialog = remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "设置",
            style = MaterialTheme.typography.headlineSmall
        )
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(128.dp),
            onClick = {
                tipDialogContent.value = "目前免费，暂无收费计划"
                openTipDialog.value = true
            }) {
            Text(
                text = "注册",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(128.dp),
            onClick = {
                shareFile(context.getActivity())
            }
        ) {
            Text(
                text = "导出数据",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(128.dp),
            onClick = {
                try {
                    DBUtil.backup(context)
                    tipDialogContent.value = "备份成功"
                } catch (e: Exception) {
                    tipDialogContent.value = "备份失败"
                }
                openTipDialog.value = true
            }) {
            Text(
                text = "备份数据",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(128.dp),
            onClick = {
                try {
                    DBUtil.restore(context)
                    tipDialogContent.value = "恢复成功"
                } catch (e: Exception) {
                    tipDialogContent.value = "恢复失败"
                }
                openTipDialog.value = true
            }) {
            Text(
                text = "恢复数据",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(128.dp),
            onClick = {
                openClearDialog.value = true
            }) {
            Text(
                text = "清空数据",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(128.dp),
            onClick = {
                context.sendMail(
                    to = "sldkfja@163.com",
                    subject = "${context.resources.getString(R.string.app_name)} ${packInfo.versionName} ${
                        Guard.getUniqueID(
                            context
                        )
                    }"
                )
            }) {
            Text(
                text = "联系我们",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "${context.resources.getString(R.string.app_name)} ${packInfo.versionName}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "(C) 2023 Lin Zheng. All rights Reserved",
            style = MaterialTheme.typography.bodySmall
        )
        when {
            openTipDialog.value -> {
                MinimalDialog(tipDialogContent.value) {
                    openTipDialog.value = false
                }
            }
        }
        when {
            openClearDialog.value -> {
                AlertDialog(
                    { openClearDialog.value = false },
                    {
                        MainApplication.appDatabase.expressItemDao().deleteAll()
                        openClearDialog.value = false
                    },
                    "警告",
                    "该操作将删除全部数据，并且无法恢复，确定要这么做吗?",
                    Icons.Default.Info
                )
            }
        }
    }

}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

/**
 * 发送邮件
 */
fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("SettingScreen", "发送邮件失败")
    } catch (t: Throwable) {
        Log.e("SettingScreen", "发送邮件失败")
    }
}

/**
 * 分享文件
 */
private fun shareFile(activity: ComponentActivity?) {
    activity?.apply {
        val resultIntent = Intent("com.base.expressassistant.ACTION_RETURN_FILE")
        val filesDir = File(getExternalFilesDir(null), DBUtil.BackupDirName)
        val fileList = filesDir.listFiles()
        setResult(Activity.RESULT_CANCELED, null)
        if (fileList != null) {
            if (fileList.isNotEmpty()) {
                resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val file = fileList[0]

                val fileUri = try {
                    FileProvider.getUriForFile(
                        this,
                        "com.base.expressassistant.fileprovider",
                        file
                    )
                } catch (e: IllegalArgumentException) {
                    Log.e(
                        "File Selector",
                        "The selected file can't be shared: $file"
                    )
                    null
                }

                if (fileUri != null) {
                    resultIntent.setDataAndType(fileUri, contentResolver.getType(fileUri))
                    setResult(Activity.RESULT_OK, resultIntent)

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                    intent.type = "*/*" //分享文件
                    startActivity(Intent.createChooser(intent, "分享"))

                } else {
                    resultIntent.setDataAndType(null, "")
                    activity.setResult(ComponentActivity.RESULT_CANCELED, resultIntent)
                }
            }
        }
    }
}
