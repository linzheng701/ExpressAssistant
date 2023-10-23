package com.base.expressassistant.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.base.expressassistant.MainApplication
import com.base.expressassistant.R
import com.base.expressassistant.entities.ExpressItem
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    address: String,
    onAddressChange: (String) -> Unit,
    code: String,
    onCodeChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit
) {
    var isAddressError by remember { mutableStateOf(false) }
    var isCodeError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = context.resources.getString(R.string.app_name),
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("地址") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = isAddressError,
            supportingText = {
                if (isAddressError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "地址不可为空",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.padding(8.dp, 4.dp)
        )
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = { Text("编号") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.padding(8.dp, 4.dp),
            isError = isCodeError,
            supportingText = {
                if (isCodeError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "编号不可为空",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("手机号") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.padding(8.dp, 4.dp),
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("姓名") },
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = {
                if (address.isEmpty()) {
                    isAddressError = true
                } else if (code.isEmpty()) {
                    isCodeError = true
                } else {
                    isAddressError = false
                    isCodeError = false
                    val item =
                        ExpressItem(name, address, phoneNumber, code, null, 0, Date(), null)
                    MainApplication.appDatabase.expressItemDao().insertAll(item)
                    onAddressChange("")
                    onCodeChange("")
                    onPhoneNumberChange("")
                    onNameChange("")
                    Toast.makeText(context, "添加完成", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp)
        ) {
            Text("添加")
        }
        Button(
            onClick = {
                if (address.isEmpty() and code.isEmpty() and phoneNumber.isEmpty() and name.isEmpty()) {
                    Toast.makeText(context, "查找时至少填写一个查找条件", Toast.LENGTH_LONG).show()
                    return@Button
                }
                navController.navigate("ListScreen") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp)
        ) {
            Text("查找")
        }
    }
}
