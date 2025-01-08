package com.example.finalproject

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.filled.Add
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.navigation.compose.composable

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape

import com.example.finalproject.IncomeRecord
import com.example.finalproject.ExpenseRecord

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

import com.example.finalproject.Quadruple
import kotlin.math.cos
import kotlin.math.sin


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

    var incomeList = mutableStateListOf<IncomeRecord>()


    fun addIncome(amount: String, note: String) {
        val date = getCurrentDate()
        val incomeRecord = IncomeRecord(amount, note, date)
        incomeList.add(incomeRecord)
    }


    fun getTotalIncome(): Int {
        return incomeList.sumOf { it.amount.toIntOrNull() ?: 0 }
    }


    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd", Locale.getDefault()) // 日期格式
        return sdf.format(Date()) // 取得今天的日期
    }
}


class ExpenseViewModel : ViewModel() {

    var expenseList = mutableStateListOf<ExpenseRecord>()


    fun addExpense(amount: String, note: String) {
        val date = getCurrentDate()
        val expenseRecord = ExpenseRecord(amount, note, date)
        expenseList.add(expenseRecord)
    }


    fun getTotalExpense(): Int {
        return expenseList.sumOf { it.amount.toIntOrNull() ?: 0 }
    }

    // 取得當前日期
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd", Locale.getDefault()) // 日期格式
        return sdf.format(Date()) // 取得今天的日期
    }
}


class GoalViewModel : ViewModel() {
    var goalAmount = mutableStateOf(0)
    var isLocked = mutableStateOf(false)
        private set
    var currentAmount = mutableStateOf(0)
        private set
    var balance = mutableStateOf(0)
        private set

    fun setGoalAmount(amount: Int) {
        goalAmount.value = amount
    }

    fun getGoalAmount(): Int = goalAmount.value

    fun lockGoal() {
        isLocked.value = true
    }


    fun updateCurrentAmount(totalIncome: Int, totalExpense: Int) {
        balance.value = totalIncome - totalExpense
        currentAmount.value = balance.value
        checkUnlockCondition()
    }

    private fun checkUnlockCondition() {
        if (currentAmount.value >= goalAmount.value) {
            isLocked.value = false // 達標時自動解鎖
            goalAmount.value = 0
        }
    }
}


@Composable
fun AccountingAssistant(
    navController: NavController,
    modifier: Modifier = Modifier.fillMaxSize(),
    incomeViewModel: IncomeViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel(),
    goalViewModel: GoalViewModel = viewModel()
) {

    val totalIncome = incomeViewModel.getTotalIncome()
    val totalExpense = expenseViewModel.getTotalExpense()
    val totalGoal = goalViewModel.getGoalAmount()
    val expanded = remember { mutableStateOf(false) }
    val chartHeight by animateDpAsState(targetValue = if (expanded.value) 280.dp else 0.dp) // 條形圖高度動畫

    var selectedChart by remember { mutableStateOf("PieChart") }


    //val image = painterResource(R.drawable.expensive, R.drawable.spending)


    Column(
        modifier = modifier
            .background(color = Color(0xFFFAF9F6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.height(50.dp))

            TextCard(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                totalGoal = totalGoal,
                navController = navController
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ElevatedButton


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { navController.navigate("incomePage") },
                        modifier = Modifier
                            .size(170.dp)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.expensive),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "收入",
                        fontSize = 22.sp
                    )
                }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { navController.navigate("expensePage") },
                        modifier = Modifier
                            .size(170.dp)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF5252))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.spending),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "支出",
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
            ) {
                Text(if (expanded.value) "收起圖表" else "展開圖表")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(2.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (expanded.value) {

                    PieChart(
                        incomeData = incomeViewModel.incomeList,
                        expenseData = expenseViewModel.expenseList,
                        modifier = Modifier
                            .size(280.dp)
                            .padding(8.dp)
                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
            //Spacer(modifier = Modifier.height(10.dp))

        }
    }


    Spacer(modifier = Modifier.height(10.dp))

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color(0xFFF4E8D7)),
            //.padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { navController.navigate("deleteDataPage") },
                        modifier = Modifier.size(75.dp)//.shadow(elevation = 4.dp, shape = CutCornerShape(10))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.deletedata),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Text(
                        "刪除紀錄",
                        fontSize = 20.sp
                    )

                }

                VerticalDivider(thickness = 2.dp)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {


                    TextButton(
                        onClick = { navController.navigate("allRecord") },
                        modifier = Modifier.size(75.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.folder1),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Text(
                        "所有紀錄",
                        fontSize = 20.sp
                    )

                }

                VerticalDivider(thickness = 2.dp)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { navController.navigate("setGoal") },
                        modifier = Modifier.size(75.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.target),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Text(
                        "目標設定",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}


@Composable
fun IncomePage(navController: NavController, incomeViewModel: IncomeViewModel = viewModel()) {
    var incomeAmount by remember { mutableStateOf("") }
    var incomeNote by remember { mutableStateOf("") }

    val expanded = remember { mutableStateOf(false) }
    val chartHeight by animateDpAsState(targetValue = if (expanded.value) 280.dp else 0.dp)

    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (showDialog) {
        EnterCheckDialog(
            checkText = "確定輸入此金額與備註事項?",
            checkTitle = "輸入確認",
            onConfirmation = {
                incomeViewModel.addIncome(incomeAmount, incomeNote)
                incomeAmount = ""
                incomeNote = ""
                scope.launch {
                    snackbarHostState.showSnackbar("收入金額已更新")
                }
                showDialog = false
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .background(color = Color(0xFFFAF9F6))
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.SpaceEvenly
            ) {


                Spacer(modifier = Modifier.height(180.dp))
                IncomeList(incomeData = incomeViewModel.incomeList)


            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                //.padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp),
                            enabled = !expanded.value
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )

                    }

                    VerticalDivider(thickness = 2.dp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.navigate("deletedataPage") },
                            modifier = Modifier.size(75.dp),
                            enabled = !expanded.value
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.deletedata),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "刪除紀錄",
                            fontSize = 20.sp
                        )

                    }
                }
            }
        }
        if (expanded.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start

        ) {
            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier
                    .size(width = 400.dp, height = 150.dp)
                    .padding(top = 16.dp),
                shape = CutCornerShape(10),
                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFEDEDED))
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(2.dp)

                    ),

                contentAlignment = Alignment.Center
            ) {
                if (expanded.value) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        TextField(
                            value = incomeAmount,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) incomeAmount =
                                    newValue
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("輸入收入金額") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))


                        TextField(
                            value = incomeNote,
                            onValueChange = { incomeNote = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("備註事項") }
                        )

                        Spacer(modifier = Modifier.height(27.dp))


                        Button(
                            onClick = {
                                if (incomeAmount.isNotEmpty()) {
                                    if (incomeNote.isEmpty()) {
                                        incomeNote = "None"
                                    }
                                    showDialog = true
                                }
                            },
                            modifier = Modifier
                                .size(width = 100.dp, height = 50.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF42A5F5))
                        ) {
                            Text("儲存")
                        }
                    }
                }
            }


        }

    }
}


@Composable
fun IncomeList(incomeData: List<IncomeRecord>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text("收入", fontSize = 18.sp)
            Text("|", fontSize = 18.sp)
            Text("備註事項", fontSize = 18.sp)
            Text("|", fontSize = 18.sp)
            Text("日期", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))


        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 3.dp,
            color = Color.Black
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(start = 12.dp, end = 12.dp)
                .background(Color(0xFFF0F8FF))
        ) {
            items(incomeData) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.amount + "元",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.note,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.date, // 顯示時間
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                HorizontalDivider(thickness = 2.dp, color = Color(0xFF232C33))
            }
        }


        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 3.dp,
            color = Color.Black
        )
    }
}


@Composable
fun IncomeFunction(navController: NavController, incomeViewModel: IncomeViewModel = viewModel()) {

    var incomeAmount by remember { mutableStateOf("") }
    var incomeNote by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(bottom = 50.dp))
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
            //.padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFFAF9F6))

                    //.padding(25.dp)
                    .align(Alignment.TopCenter),

                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF00C851))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "輸入收入",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }


                Spacer(modifier = Modifier.height(25.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = incomeAmount,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) incomeAmount = newValue
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("金額") }
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    TextField(
                        value = incomeNote,
                        onValueChange = { incomeNote = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("備註事項") }
                    )
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                //.padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )

                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (showDialog) {
                            EnterCheckDialog(
                                checkText = "確定輸入此金額與備註事項?",
                                checkTitle = "輸入確認",
                                onConfirmation = {
                                    incomeViewModel.addIncome(incomeAmount, incomeNote)
                                    incomeAmount = ""
                                    incomeNote = ""
                                    scope.launch {
                                        snackbarHostState.showSnackbar("收入金額已更新")
                                    }
                                    showDialog = false
                                },
                                onDismissRequest = {
                                    showDialog = false
                                }
                            )
                        }

                        TextButton(
                            onClick = {
                                if (incomeAmount.isNotEmpty()) {
                                    showDialog = true
                                }
                            },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.inputdata),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "輸入",
                            fontSize = 20.sp
                        )

                    }
                }
            }
        }

    }
}


@Composable
fun ExpensePage(navController: NavController, expenseViewModel: ExpenseViewModel = viewModel()) {

    var expenseAmount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }

    val expanded = remember { mutableStateOf(false) }
    val chartHeight by animateDpAsState(targetValue = if (expanded.value) 280.dp else 0.dp)

    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (showDialog) {
        EnterCheckDialog(
            checkText = "確定輸入此金額與備註事項?",
            checkTitle = "輸入確認",
            onConfirmation = {
                expenseViewModel.addExpense(expenseAmount, expenseNote)
                expenseAmount = ""
                expenseNote = ""
                scope.launch {
                    snackbarHostState.showSnackbar("支出金額已更新")
                }
                showDialog = false
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .background(color = Color(0xFFFAF9F6))
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.SpaceEvenly
            ) {


                Spacer(modifier = Modifier.height(180.dp))
                ExpenseList(expenseData = expenseViewModel.expenseList)


            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                //.padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp),
                            enabled = !expanded.value
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )

                    }

                    VerticalDivider(thickness = 2.dp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.navigate("deletedataPage") },
                            modifier = Modifier.size(75.dp),
                            enabled = !expanded.value
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.deletedata),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "刪除紀錄",
                            fontSize = 20.sp
                        )

                    }
                }
            }
        }
        if (expanded.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start

        ) {
            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier
                    .size(width = 400.dp, height = 150.dp)
                    .padding(top = 16.dp),
                shape = CutCornerShape(10),
                colors = ButtonDefaults.buttonColors(Color(0xFFFF5252))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFEDEDED))
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(2.dp)

                    ),

                contentAlignment = Alignment.Center
            ) {
                if (expanded.value) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        TextField(
                            value = expenseAmount,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) expenseAmount =
                                    newValue
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("輸入支出金額") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))


                        TextField(
                            value = expenseNote,
                            onValueChange = { expenseNote = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("備註事項") }
                        )

                        Spacer(modifier = Modifier.height(27.dp))


                        Button(
                            onClick = {
                                if (expenseAmount.isNotEmpty()) {
                                    if (expenseNote.isEmpty()) {
                                        expenseNote = "None"
                                    }
                                    showDialog = true
                                }
                            },
                            modifier = Modifier
                                .size(width = 100.dp, height = 50.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF42A5F5))
                        ) {
                            Text("儲存")
                        }
                    }
                }
            }


        }

    }
}

@Composable
fun ExpenseList(expenseData: List<ExpenseRecord>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text("支出", fontSize = 18.sp)
            Text("|", fontSize = 18.sp)
            Text("備註事項", fontSize = 18.sp)
            Text("|", fontSize = 18.sp)
            Text("日期", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))


        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 3.dp,
            color = Color.Black
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(start = 12.dp, end = 12.dp)
                .background(Color(0xFFF0F8FF))
        ) {
            items(expenseData) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.amount + "元",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.note,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.date,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                HorizontalDivider(thickness = 2.dp, color = Color(0xFF232C33))
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 3.dp,
            color = Color.Black
        )
    }
}


@Composable
fun ExpenseFunction(
    navController: NavController,
    expenseViewModel: ExpenseViewModel = viewModel()
) {

    var expenseAmount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(bottom = 50.dp))
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFFAF9F6))
                    .align(Alignment.TopCenter),

                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFFF4444))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "輸入支出",
                        fontSize = 24.sp,
                        color = Color.White // 使用白色字體在藍色背景上
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = expenseAmount,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) expenseAmount = newValue
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("金額") }
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    TextField(
                        value = expenseNote,
                        onValueChange = { expenseNote = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("備註事項") }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                //.padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )

                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (showDialog) {
                            EnterCheckDialog(
                                checkText = "確定輸入此金額與備註事項?",
                                checkTitle = "輸入確認",
                                onConfirmation = {
                                    expenseViewModel.addExpense(expenseAmount, expenseNote)
                                    expenseAmount = ""
                                    expenseNote = ""
                                    scope.launch {
                                        snackbarHostState.showSnackbar("支出金額已更新")
                                    }
                                    showDialog = false
                                },
                                onDismissRequest = {
                                    showDialog = false
                                }
                            )
                        }

                        TextButton(
                            onClick = {
                                if (expenseAmount.isNotEmpty()) {
                                    showDialog = true
                                }
                            },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.inputdata),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "輸入",
                            fontSize = 20.sp
                        )

                    }
                }
            }
        }

    }
}

@Composable
fun BalanceDisplay(
    totalIncome: Int,
    totalExpense: Int,
    totalGoal: Int,
    navController: NavController,
    goalViewModel: GoalViewModel = viewModel()
) {
    val balance = totalIncome - totalExpense
    val goal = totalGoal - balance

    val TextDisplay = if (totalGoal == 0) {
        "未設定目標金額"
    } else if (balance < totalGoal) {
        "距離目標金額: NT\$ $goal"
    } else {
        "已達標"
    }

    val TextDisplay2 = if (totalGoal == 0) {
        "當前設定目標金額: 未設定"
    } else {
        "當前設定目標金額: NT\$ $totalGoal"
    }





    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "餘額: NT\$ $balance",
            fontSize = 35.sp,
            color = if (balance > 0) {
                Color.Black
            } else if (balance < 0) {
                Color.Black
            } else {
                Color.Black
            }

        )

    }

    Spacer(modifier = Modifier.height(10.dp))
    HorizontalDivider(thickness = 2.dp, color = Color.Black)
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = TextDisplay2,
        fontSize = 24.sp

    )

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        text = TextDisplay,
        fontSize = 24.sp
    )
}


@Composable
fun DeleteDataPage(
    navController: NavController,
    incomeViewModel: IncomeViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    var selectedIncomeItems by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var selectedExpenseItems by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var showDialog by remember { mutableStateOf(false) }



    Box(
        modifier = Modifier
            .fillMaxSize()
        //.padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFAF9F6))
                //.padding(16.dp),
                .align(Alignment.TopCenter),
            //horizontalAlignment = Alignment.Start,
            //verticalArrangement = Arrangement.SpaceBetween

        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFFF8552))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "選擇要刪除紀錄的項目",
                    fontSize = 24.sp,
                    color = Color.White // 使用白色字體在藍色背景上
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider(thickness = 3.dp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                //.background(color = Color(0xFFFF8552)),
                //contentAlignment = Alignment.Center
            ) {
                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("收入", fontSize = 18.sp)
                        Text("|", fontSize = 18.sp)
                        Text("備註事項", fontSize = 18.sp)
                        Text("|", fontSize = 18.sp)
                        Text("日期", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(60.dp))
                    }

                    HorizontalDivider(thickness = 3.dp, color = Color.Black)
                    //Spacer(modifier = Modifier.height(16.dp))


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        items(incomeViewModel.incomeList.size) { index ->
                            val item = incomeViewModel.incomeList[index]

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        // 點擊時切換選擇狀態
                                        selectedIncomeItems =
                                            if (selectedIncomeItems.contains(index)) {
                                                selectedIncomeItems - index
                                            } else {
                                                selectedIncomeItems + index
                                            }
                                    },
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.amount + "元",
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Green
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.note,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.date,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Checkbox(
                                    modifier = Modifier.weight(0.3f),
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
                            HorizontalDivider(thickness = 2.dp, color = Color.Black)
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 3.dp, color = Color.Black)

            Spacer(modifier = Modifier.height(5.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                //.background(color = Color(0xFFFF8552)),
                //contentAlignment = Alignment.Center
            ) {
                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("支出", fontSize = 18.sp)
                        Text("|", fontSize = 18.sp)
                        Text("備註事項", fontSize = 18.sp)
                        Text("|", fontSize = 18.sp)
                        Text("日期", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(60.dp))
                    }

                    Column() {
                        HorizontalDivider(thickness = 3.dp, color = Color.Black)
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        items(expenseViewModel.expenseList.size) { index ->
                            val item = expenseViewModel.expenseList[index]

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        // 點擊時切換選擇狀態
                                        selectedExpenseItems =
                                            if (selectedExpenseItems.contains(index)) {
                                                selectedExpenseItems - index
                                            } else {
                                                selectedExpenseItems + index
                                            }
                                    },
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.amount + "元",
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Red
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.note,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.date,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Checkbox(
                                    modifier = Modifier.weight(0.3f),
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
                            HorizontalDivider(thickness = 2.dp, color = Color.Black)
                        }
                    }
                }
            }
            HorizontalDivider(thickness = 2.dp, color = Color.Black)
        }


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                //.padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )

                    }

                    VerticalDivider(thickness = 2.dp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (showDialog) {
                            EnterCheckDialog(
                                checkText = "確認後將無法復原，確定刪除所勾選的項目?",
                                checkTitle = "刪除確認",
                                onConfirmation = {
                                    // 使用mutableStateListOf來保證與Compose狀態同步
                                    val updatedIncomeList =
                                        incomeViewModel.incomeList.toMutableStateList()

                                    // 過濾掉被選中的項目
                                    updatedIncomeList.removeAll { item ->
                                        selectedIncomeItems.contains(updatedIncomeList.indexOf(item))
                                    }

                                    val updatedExpenseList =
                                        expenseViewModel.expenseList.toMutableStateList()

                                    // 過濾掉被選中的項目
                                    updatedExpenseList.removeAll { item ->
                                        selectedExpenseItems.contains(
                                            updatedExpenseList.indexOf(
                                                item
                                            )
                                        )
                                    }

                                    // 更新 incomeList
                                    incomeViewModel.incomeList.clear()
                                    incomeViewModel.incomeList.addAll(updatedIncomeList)

                                    expenseViewModel.expenseList.clear()
                                    expenseViewModel.expenseList.addAll(updatedExpenseList)


                                    // 清空選擇的項目
                                    selectedIncomeItems = emptySet()
                                    selectedExpenseItems = emptySet()

                                    showDialog = false
                                },
                                onDismissRequest = {
                                    showDialog = false
                                }
                            )
                        }

                        TextButton(
                            onClick = {
                                if (selectedIncomeItems.isEmpty() && selectedExpenseItems.isEmpty()) {

                                    showDialog = false

                                } else {
                                    showDialog = true
                                }
                            },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "刪除項目",
                            fontSize = 20.sp
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun EnterCheckDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    checkText: String,
    checkTitle: String
) {
    AlertDialog(
        title = {
            Text(text = checkTitle)
        },
        text = {
            Text(text = checkText)
        },


        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()

                }
            ) {
                Text("確認")
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
fun InfoCheckDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    checkText: String,
    checkText2: String,
    checkTitle: String
) {
    AlertDialog(
        title = {
            Text(text = checkTitle)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = checkText,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = checkText2,
                )
            }
        },


        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()

                }
            ) {
                Text("確認")
            }
        },
    )
}


@Composable
fun TextCard(totalIncome: Int, totalExpense: Int, totalGoal: Int, navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9C4),
        ),
        modifier = Modifier
            .size(width = 500.dp, height = 180.dp)
        //.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BalanceDisplay(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                totalGoal = totalGoal,
                navController = navController
            )
        }

    }
}

@Composable
fun AllRecord(
    navController: NavController,
    incomeViewModel: IncomeViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel()
) {

    val incomeData = incomeViewModel.incomeList
    val expenseData = expenseViewModel.expenseList


    val allRecords = remember { mutableStateListOf<Quadruple<String, String, String, String>>() }


    LaunchedEffect(incomeData, expenseData) {
        allRecords.clear()

        val maxSize = maxOf(incomeData.size, expenseData.size)

        for (i in 0 until maxSize) {
            if (i < incomeData.size) {
                allRecords.add(
                    Quadruple(
                        incomeData[i].amount,
                        incomeData[i].note,
                        "income",
                        incomeData[i].date
                    )
                )
            }
            if (i < expenseData.size) {
                allRecords.add(
                    Quadruple(
                        expenseData[i].amount,
                        expenseData[i].note,
                        "expense",
                        expenseData[i].date
                    )
                )
            }
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFAF9F6))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF8AA8A1))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "所有紀錄",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                items(allRecords) { item -> // 顯示交替後的資料
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val amount = item.first
                            val description = item.second
                            val type = item.third
                            val date = item.fourth

                            // 根據類型顯示顏色和符號
                            Text(
                                modifier = Modifier.weight(1f),
                                text = if (type == "income") "+$amount 元" else "-$amount 元",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start,
                                color = if (type == "income") Color.Green else Color.Red
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = description,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = date,
                                fontSize = 20.sp,
                                textAlign = TextAlign.End,
                                color = Color.Black
                            )
                        }
                        HorizontalDivider(thickness = 2.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SetGoal(
    navController: NavController,
    incomeViewModel: IncomeViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel(),
    goalViewModel: GoalViewModel = viewModel()
) {
    val totalIncome = incomeViewModel.getTotalIncome()
    val totalExpense = expenseViewModel.getTotalExpense()
    val balance = totalIncome - totalExpense
    var showDialog by remember { mutableStateOf(false) }
    var showLockDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    // 更新餘額到 GoalViewModel 並檢查是否達標
    LaunchedEffect(balance) {
        goalViewModel.updateCurrentAmount(totalIncome, totalExpense)
    }

    val isLocked = goalViewModel.isLocked.value
    val goalAmount = goalViewModel.goalAmount.value.toString()
    val goalAmountInt = goalViewModel.goalAmount.value

    // 這裡將 goalAmountTemp 初始化為空字串，並且它用來綁定 TextField
    var goalAmountTemp by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }



    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(bottom = 50.dp))
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFFAF9F6))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF8AA8A1))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "目標設定",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.weight(0.7f))

                        if (showInfoDialog) {
                            InfoCheckDialog(
                                checkText = "此功能可設定所想要達成的目標金額，您也可以使用鎖定功能(下方註解)來限制修改金額",
                                checkText2 = "*鎖定:使用時，將會鎖住金額輸入欄位及鎖定按鈕，直至達成所設定的金額目標為止",
                                checkTitle = "資訊Info",
                                onConfirmation = {
                                    showInfoDialog = false
                                },
                                onDismissRequest = {
                                    showInfoDialog = false
                                }
                            )
                        }

                        TextButton(
                            onClick = { showInfoDialog = true },
                            modifier = Modifier.size(60.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.question),
                                contentDescription = "Custom Image Button",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = goalAmountTemp,  // 綁定到 goalAmountTemp，這樣 TextField 顯示的是空字串或用戶輸入的值
                            onValueChange = { newValue ->
                                if (!isLocked && newValue.all { it.isDigit() }) {
                                    goalAmountTemp = newValue // 更新 goalAmountTemp
                                }
                            },
                            enabled = !isLocked,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("金額") }
                        )

                        if (isLocked) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.forbidden),
                                    contentDescription = "",
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }

                        /*   //檢查用
                    Text(text = "goalAmount: $goalAmount", fontSize = 18.sp, color = Color.Black)
                    val goalAmountInt = goalAmount.toIntOrNull() ?: 0
                    Text(text = "goalAmountInt: $goalAmountInt", fontSize = 18.sp, color = Color.Black)
                    Text(text = "balance: $balance", fontSize = 18.sp, color = Color.Black)*/


                    }
                    Spacer(modifier = Modifier.height(60.dp))


                    Text(
                        text = "當前設定目標金額: $goalAmountInt" + "元",
                        fontSize = 25.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isLocked) {
                        Text(
                            "已鎖定",
                            fontSize = 25.sp,
                            color = Color.Red
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFFF4E8D7)),
                //.padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.resource_return),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "返回",
                            fontSize = 20.sp
                        )

                    }

                    VerticalDivider(thickness = 2.dp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (showLockDialog) {
                            EnterCheckDialog(
                                checkText = "鎖定後將無法變更目標金額，直至您達成目標金額，確認鎖定?",
                                checkTitle = "最終確認",
                                onConfirmation = {
                                    goalViewModel.lockGoal()
                                    showLockDialog = false
                                },
                                onDismissRequest = {
                                    showLockDialog = false
                                }
                            )
                        }

                        if (showErrorDialog) {
                            EnterCheckDialog(
                                checkText = "無法鎖定! 您的餘額大於或等於所設定的目標金額",
                                checkTitle = "錯誤",
                                onConfirmation = {
                                    showErrorDialog = false
                                },
                                onDismissRequest = {
                                    showErrorDialog = false
                                }
                            )
                        }



                        TextButton(
                            onClick = {
                                val goalAmountInt = goalAmount.toIntOrNull() ?: 0
                                if (balance >= goalAmountInt) {
                                    showErrorDialog = true
                                } else if (!isLocked) {
                                    showLockDialog = true
                                }

                            },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    id = if (isLocked) R.drawable.forbidden else R.drawable.unlock
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "鎖定",
                            fontSize = 20.sp
                        )

                    }

                    VerticalDivider(thickness = 2.dp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (showDialog) {
                            EnterCheckDialog(
                                checkText = "確定輸入此目標金額?",
                                checkTitle = "輸入確認",
                                onConfirmation = {
                                    goalViewModel.setGoalAmount(
                                        goalAmountTemp.toIntOrNull() ?: 0
                                    ) // 儲存目標金額
                                    scope.launch {
                                        snackbarHostState.showSnackbar("目標金額已設定")
                                    }
                                    goalAmountTemp = "" //儲存後清空 TextField
                                    showDialog = false
                                },
                                onDismissRequest = {
                                    showDialog = false
                                }
                            )
                        }
                        TextButton(
                            onClick = {
                                if(goalAmountTemp.isNotEmpty()) {
                                    showDialog = true
                                }
                            },
                            enabled = !isLocked,
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    id = if (isLocked) R.drawable.forbidden else R.drawable.savedisk
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Text(
                            "儲存設定",
                            fontSize = 20.sp
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun PieChart(
    incomeData: List<IncomeRecord>,
    expenseData: List<ExpenseRecord>,
    modifier: Modifier = Modifier
) {
    // 計算收入和支出的總額
    val totalIncome = incomeData.sumOf { it.amount.toIntOrNull() ?: 0 }
    val totalExpense = expenseData.sumOf { it.amount.toIntOrNull() ?: 0 }

    // 合併資料，將收入和支出轉換成百分比
    val totalAmount = totalIncome + totalExpense
    if (totalAmount == 0) return // 總額為零時，不繪製圖表

    val incomePercentage = (totalIncome.toFloat() / totalAmount) * 360f
    val expensePercentage = (totalExpense.toFloat() / totalAmount) * 360f

    // Pie Chart 顏色設定
    val colors = listOf(Color(0xFF4CAF50), Color(0xFFFF5252))

    // 繪製 Pie Chart
    Canvas(
        modifier = modifier
            .size(180.dp) // 設置固定大小
            .aspectRatio(1f) // 確保寬高比例為 1:1
    ) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        var startAngle = 0f

        // 繪製收入區塊
        drawArc(
            color = colors[0],
            startAngle = startAngle,
            sweepAngle = incomePercentage,
            useCenter = true
        )
        if (incomePercentage > 0) {
            // 計算收入文字位置
            val middleAngle = startAngle + incomePercentage / 2
            val textOffset = Offset(
                x = center.x + (radius / 2) * cos(Math.toRadians(middleAngle.toDouble())).toFloat(),
                y = center.y + (radius / 2) * sin(Math.toRadians(middleAngle.toDouble())).toFloat()
            )
            // 繪製收入文字
            drawContext.canvas.nativeCanvas.drawText(
                "收入: $totalIncome 元 , ${(incomePercentage / 360 * 100).toInt()}%",
                textOffset.x,
                textOffset.y,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 40f
                }
            )
        }
        startAngle += incomePercentage

        // 繪製支出區塊
        drawArc(
            color = colors[1],
            startAngle = startAngle,
            sweepAngle = expensePercentage,
            useCenter = true
        )
        if (expensePercentage > 0) {
            // 計算支出文字位置
            val middleAngle = startAngle + expensePercentage / 2
            val textOffset = Offset(
                x = center.x + (radius / 2) * cos(Math.toRadians(middleAngle.toDouble())).toFloat(),
                y = center.y + (radius / 2) * sin(Math.toRadians(middleAngle.toDouble())).toFloat()
            )
            // 繪製支出文字
            drawContext.canvas.nativeCanvas.drawText(
                "支出: $totalExpense 元 , ${(expensePercentage / 360 * 100).toInt()}%",
                textOffset.x,
                textOffset.y,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 40f
                }
            )
        }
    }
}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val incomeViewModel = IncomeViewModel()
    val expenseViewModel = ExpenseViewModel()
    val goalViewModel = GoalViewModel()

    NavHost(navController = navController, startDestination = "home") {
        // 主頁面
        composable("home",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {

            AccountingAssistant(
                navController,
                incomeViewModel = incomeViewModel,
                expenseViewModel = expenseViewModel,
                goalViewModel = goalViewModel,
            )

        }
        // 收入頁面
        composable("incomePage",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            IncomePage(navController, incomeViewModel)
        }
        //支出
        composable("expensePage",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            ExpensePage(navController, expenseViewModel)
        }
        composable("incomeFunction",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            IncomeFunction(navController, incomeViewModel)
        }
        composable("expenseFunction",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            ExpenseFunction(navController, expenseViewModel)
        }
        composable("deletedataPage",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            DeleteDataPage(
                navController,
                incomeViewModel = incomeViewModel,
                expenseViewModel = expenseViewModel
            )
        }
        composable("allRecord",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            AllRecord(navController, incomeViewModel, expenseViewModel)
        }
        composable("setGoal",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            SetGoal(
                navController = navController,
                goalViewModel = goalViewModel,
                incomeViewModel = incomeViewModel,
                expenseViewModel = expenseViewModel
            )
        }
        composable("incomePage") {
            IncomePage(navController, incomeViewModel)
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
fun PreviewIncomeFunction() {
    FinalProjectTheme {
        val navController = rememberNavController()
        IncomeFunction(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseFunction() {
    FinalProjectTheme {
        val navController = rememberNavController()
        ExpenseFunction(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteDataPage() {
    FinalProjectTheme {
        val navController = rememberNavController()
        DeleteDataPage(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAllRecord() {
    FinalProjectTheme {
        val navController = rememberNavController()

        val sampleIncomeData = listOf(
            "收入1" to "1000元",
            "收入2" to "2000元",
            "收入3" to "1500元"
        )

        val sampleExpenseData = listOf(
            "支出1" to "500元",
            "支出2" to "300元",
            "支出3" to "800元"
        )
        AllRecord(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetGoal() {
    FinalProjectTheme {
        val navController = rememberNavController()
        SetGoal(navController)
    }
}