package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.ui.theme.FinalProjectTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalProjectTheme {
                AppNavigation()

            }
        }
    }
}

class IncomeViewModel : ViewModel() {
    var incomeList = mutableStateListOf<Pair<String, String>>() // 動態更新的收入列表
    //添加收入
    fun addIncome(amount: String, note: String) {
        incomeList.add(Pair(amount, note))
    }
    //加總收入
    fun getTotalIncome(): Int {
        return incomeList.sumOf { it.first.toIntOrNull() ?: 0 }
    }
}

class ExpenseViewModel : ViewModel() {
    var expenseList = mutableStateListOf<Pair<String, String>>() // 動態更新的收入列表

    fun addExpense(amount: String, note: String) {
        expenseList.add(Pair(amount, note))
    }

    fun getTotalExpense(): Int {
        return expenseList.sumOf { it.first.toIntOrNull() ?: 0 }
    }
}

@Composable
fun AccountingAssistant(navController: NavController,modifier: Modifier = Modifier.fillMaxSize().padding(16.dp),incomeViewModel: IncomeViewModel = viewModel(),expenseViewModel: ExpenseViewModel = viewModel()) {

    val totalIncome = incomeViewModel.getTotalIncome()
    val totalExpense = expenseViewModel.getTotalExpense()

    Column (
        modifier = modifier,

        horizontalAlignment = Alignment.Start
    )
    {
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { navController.navigate("incomePage") },
            modifier = Modifier
                .size(170.dp)
                .padding(start = 8.dp, top = 16.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF00C851))  //AARRGGBB(A為Channel去背)

        )
        {
            Text("Test")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "收入Test",
            modifier = Modifier.padding(start = 48.dp),
            fontSize = 22.sp
        )
    }
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.End
    )
    {
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {  navController.navigate("expensePage") },
            modifier = Modifier
                .size(170.dp)
                .padding(end = 8.dp, top = 16.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFFFF4444))

        )
        {
            Text("Test")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "支出Test",
            modifier = Modifier.padding(end = 48.dp),
            fontSize = 22.sp
        )
    }
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    )
    {

        BalanceDisplay(
            totalIncome = totalIncome,
            totalExpense = totalExpense
        )

        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { navController.navigate("deletedataPage") },
            modifier = Modifier
                .size(width = 300.dp,height = 100.dp)
                .padding(bottom = 16.dp),

            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))

        )
        {
            Text("功能Test")
        }
    }

}

@Composable
fun IncomePage(navController: NavController,incomeViewModel: IncomeViewModel = viewModel()) {
    var incomeAmount by remember { mutableStateOf("") }
    var incomeNote by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //收入按鈕
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { navController.navigate("incomeFunction") },
                modifier = Modifier
                    .size(width = 400.dp, height = 150.dp)
                    .padding(end = 8.dp, top = 16.dp),
                shape = CutCornerShape(10),
                colors = ButtonDefaults.buttonColors(Color(0xFF00C851))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // 左右對齊
                    verticalAlignment = Alignment.CenterVertically // 垂直居中
                ) {
                    Text(
                        "目前收入",
                        fontSize = 25.sp
                    )


                    Text(
                        "NT\$ ${incomeViewModel.getTotalIncome()}",
                        fontSize = 25.sp
                    )
                }
            }
        }

        //事項備註
        IncomeList(incomeData = incomeViewModel.incomeList)

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "返回",
                fontSize = 25.sp
            )

        }
    }
}

@Composable
fun IncomeList(incomeData: List<Pair<String, String>>){

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {

      Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(30.dp),
       horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text("收入", fontSize = 18.sp)
            Text("|")
            Text("備註事項", fontSize = 18.sp)

      }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // 設定清單高度
            .padding(start = 12.dp,end = 12.dp)
            .background(Color(0xFF909598))

    ) {
        items(incomeData) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                    .weight(1f),
                    text = item.first,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = item.second,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun IncomeFunction(navController: NavController, incomeViewModel: IncomeViewModel = viewModel()){

    var incomeAmount by remember { mutableStateOf("") }
    var incomeNote by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ){

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Text("輸入收入：")
            TextField(
                value = incomeAmount,
                onValueChange = { newValue -> if (newValue.all { it.isDigit() }) incomeAmount = newValue },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("金額") }
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                value = incomeNote,
                onValueChange = { incomeNote = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("備註") }
            )
        }

        Spacer(modifier = Modifier.height(480.dp))
        Button(
            onClick = {
                if (incomeAmount.isNotEmpty()) {
                    incomeViewModel.addIncome(incomeAmount, incomeNote)
                    navController.popBackStack() // 返回上一頁
                }
            },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "完成",
                fontSize = 25.sp
            )

        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "返回",
                fontSize = 25.sp
            )

        }

    }
}



@Composable
fun ExpensePage(navController: NavController,expenseViewModel: ExpenseViewModel = viewModel()){

    var expenseAmount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //收入按鈕
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { navController.navigate("expenseFunction") },
                modifier = Modifier
                    .size(width = 400.dp, height = 150.dp)
                    .padding(end = 8.dp, top = 16.dp),
                shape = CutCornerShape(10),
                colors = ButtonDefaults.buttonColors(Color(0xFFFF4444))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // 左右對齊
                    verticalAlignment = Alignment.CenterVertically // 垂直居中
                ) {
                    Text(
                        "目前支出",
                        fontSize = 25.sp
                    )


                    Text(
                        "NT\$ ${expenseViewModel.getTotalExpense()}",
                        fontSize = 25.sp
                    )
                }
            }
        }



        ExpenseList(expenseData = expenseViewModel.expenseList)

        //返回按鈕
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "返回",
                fontSize = 25.sp
            )

        }
    }
}

@Composable
fun ExpenseList(expenseData: List<Pair<String, String>>){

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text("支出", fontSize = 18.sp)
            Text("|")
            Text("備註事項", fontSize = 18.sp)

        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // 設定清單高度
            .padding(start = 12.dp,end = 12.dp)
            .background(Color(0xFF909598))

    ) {
        items(expenseData) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = item.first,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = item.second,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ExpenseFunction(navController: NavController, expenseViewModel: ExpenseViewModel = viewModel()){

    var expenseAmount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ){

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Text("輸入支出：")
            TextField(
                value = expenseAmount,
                onValueChange = { newValue -> if (newValue.all { it.isDigit() }) expenseAmount = newValue },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("金額") }
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                value = expenseNote,
                onValueChange = { expenseNote = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("備註") }
            )
        }

        Spacer(modifier = Modifier.height(480.dp))
        Button(
            onClick = {
                if (expenseAmount.isNotEmpty()) {
                    expenseViewModel.addExpense(expenseAmount, expenseNote)
                    navController.popBackStack() // 返回上一頁
                }
            },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "完成",
                fontSize = 25.sp
            )

        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "返回",
                fontSize = 25.sp
            )

        }

    }
}

@Composable
fun BalanceDisplay(totalIncome: Int, totalExpense: Int){
    val balance = totalIncome - totalExpense

    Text(
        text = "餘額: NT\$ $balance",
        fontSize = 24.sp,
        color = if (balance >= 0) Color.Green else Color.Red
    )
}


@Composable
fun DeleteDataPage(navController: NavController,incomeViewModel: IncomeViewModel = viewModel(),expenseViewModel: ExpenseViewModel = viewModel()){
    var selectedIncomeItems by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var selectedExpenseItems by remember { mutableStateOf<Set<Int>>(emptySet()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "選擇要刪除的項目")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(incomeViewModel.incomeList.size) { index ->
                val item = incomeViewModel.incomeList[index]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable {
                            // 點擊時切換選擇狀態
                            selectedIncomeItems = if (selectedIncomeItems.contains(index)) {
                                selectedIncomeItems - index
                            } else {
                                selectedIncomeItems + index
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.first, fontSize = 16.sp, textAlign = TextAlign.Start)
                    Text(text = item.second, fontSize = 16.sp, textAlign = TextAlign.Start)
                    Checkbox(
                        checked = selectedIncomeItems.contains(index),
                        onCheckedChange = { isChecked ->
                            selectedIncomeItems = if (isChecked) {
                                selectedIncomeItems + index
                            } else {
                                selectedIncomeItems - index
                            }
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(expenseViewModel.expenseList.size) { index ->
                val item = expenseViewModel.expenseList[index]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable {
                            // 點擊時切換選擇狀態

                            selectedExpenseItems = if (selectedExpenseItems.contains(index)) {
                                selectedExpenseItems - index
                            } else {
                                selectedExpenseItems + index
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.first, fontSize = 16.sp, textAlign = TextAlign.Start)
                    Text(text = item.second, fontSize = 16.sp, textAlign = TextAlign.Start)
                    Checkbox(
                        checked = selectedExpenseItems.contains(index),
                        onCheckedChange = { isChecked ->
                            val expenseIndex = index + incomeViewModel.incomeList.size
                            selectedExpenseItems = if (isChecked) {

                                selectedExpenseItems + index
                            } else {
                                selectedExpenseItems - index
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(200.dp))
        // 刪除選中的項目
        Button(
            onClick = {
                // 使用mutableStateListOf來保證與Compose狀態同步
                val updatedIncomeList = incomeViewModel.incomeList.toMutableStateList()

                // 過濾掉被選中的項目
                updatedIncomeList.removeAll { item ->
                    selectedIncomeItems.contains(updatedIncomeList.indexOf(item))
                }

                val updatedExpenseList = expenseViewModel.expenseList.toMutableStateList()

                // 過濾掉被選中的項目
                updatedExpenseList.removeAll { item ->
                    selectedExpenseItems.contains(updatedExpenseList.indexOf(item))
                }

                // 更新 incomeList
                incomeViewModel.incomeList.clear()
                incomeViewModel.incomeList.addAll(updatedIncomeList)

                expenseViewModel.expenseList.clear()
                expenseViewModel.expenseList.addAll(updatedExpenseList)


                // 清空選擇的項目
                selectedIncomeItems = emptySet()
                selectedExpenseItems = emptySet()
            },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp)
                .padding(top = 16.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text("刪除選中的項目", fontSize = 20.sp)
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(width = 350.dp, height = 80.dp),
            shape = CutCornerShape(10),
            colors = ButtonDefaults.buttonColors(Color(0xFF0099CC))
        ) {
            Text(
                "返回",
                fontSize = 25.sp
            )

        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val incomeViewModel = IncomeViewModel()
    val expenseViewModel = ExpenseViewModel()

    NavHost(navController = navController, startDestination = "home") {
        // 主頁面
        composable("home") {
            AccountingAssistant(navController, incomeViewModel = incomeViewModel, expenseViewModel = expenseViewModel)
        }
        // 收入頁面
        composable("incomePage") {
            IncomePage(navController,incomeViewModel)
        }
        //支出
        composable("expensePage"){
            ExpensePage(navController,expenseViewModel)
        }
        composable("incomeFunction"){
            IncomeFunction(navController,incomeViewModel)
        }
        composable("expenseFunction"){
            ExpenseFunction(navController,expenseViewModel)
        }
        composable("deletedataPage") {
            DeleteDataPage(navController, incomeViewModel = incomeViewModel, expenseViewModel = expenseViewModel)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAccountingAssistant() {
    FinalProjectTheme {

        val navController = rememberNavController()
        AccountingAssistant(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIncomePage() {
    FinalProjectTheme {
        val navController = rememberNavController()
        IncomePage(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpensePage() {
    FinalProjectTheme {
        val navController = rememberNavController()
        ExpensePage(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIncomeFunction(){
    FinalProjectTheme {
        val navController = rememberNavController()
        IncomeFunction(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseFunction(){
    FinalProjectTheme {
        val navController = rememberNavController()
        ExpenseFunction(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteDataPage(){
    FinalProjectTheme {
        val navController = rememberNavController()
        DeleteDataPage(navController)
    }
}