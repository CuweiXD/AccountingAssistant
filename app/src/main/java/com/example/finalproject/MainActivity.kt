package com.example.finalproject

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
import androidx.navigation.compose.composable

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically




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
    var expenseList = mutableStateListOf<Pair<String, String>>()

    fun addExpense(amount: String, note: String) {
        expenseList.add(Pair(amount, note))
    }

    fun getTotalExpense(): Int {
        return expenseList.sumOf { it.first.toIntOrNull() ?: 0 }
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

    fun toggleLock() {
        isLocked.value = !isLocked.value
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
    //val image = painterResource(R.drawable.expensive, R.drawable.spending)


    Column(
        modifier = modifier
            .background(color = Color(0xFFE8F7FF)),
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
                        shape = CutCornerShape(10),
                        colors = ButtonDefaults.buttonColors(Color(0xFF00C851))
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
                        shape = CutCornerShape(10),
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF4444))
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
                .background(color = Color(0xFFA0C1D1)),
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
                        modifier = Modifier.size(75.dp)
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

    Column( modifier = Modifier
        .background(color = Color(0xFFE8F7FF))
    ) {
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
                Spacer(modifier = Modifier.height(30.dp))
                IncomeList(incomeData = incomeViewModel.incomeList)
            }

            Spacer(modifier = Modifier.height(50.dp))

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
                .background(color = Color(0xFFA0C1D1)),
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
                    TextButton(
                        onClick = { navController.navigate("deletedataPage") },
                        modifier = Modifier.size(75.dp)
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
}

@Composable
fun IncomeList(incomeData: List<Pair<String, String>>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()

    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text("收入", fontSize = 18.sp)
            Text("|")
            Text("備註事項", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
        }



        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 2.dp,
            color = Color.Black
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp) // 設定清單高度
                .padding(start = 12.dp, end = 12.dp)
                .background(Color(0xFFF0F8FF))

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
                        text = item.first + "元",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.second,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
                HorizontalDivider(thickness = 3.dp, color = Color(0xFF232C33))
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 2.dp,
            color = Color.Black
        )
    }
}

@Composable
fun IncomeFunction(navController: NavController, incomeViewModel: IncomeViewModel = viewModel()) {

    var incomeAmount by remember { mutableStateOf("") }
    var incomeNote by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
        //.padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFE8F7FF))

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
                        .background(color = Color(0xFFA0C1D1)),
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

    Column( modifier = Modifier
        .background(color = Color(0xFFE8F7FF))
    ) {
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
                Spacer(modifier = Modifier.height(30.dp))
                ExpenseList(expenseData = expenseViewModel.expenseList)
            }

            Spacer(modifier = Modifier.height(30.dp))
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
                .background(color = Color(0xFFA0C1D1)),
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
                    TextButton(
                        onClick = { navController.navigate("deletedataPage") },
                        modifier = Modifier.size(75.dp)
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
}

@Composable
fun ExpenseList(expenseData: List<Pair<String, String>>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()

    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text("支出", fontSize = 18.sp)
            Text("|")
            Text("備註事項", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
        }



        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 2.dp,
            color = Color.Black
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp) // 設定清單高度
                .padding(start = 12.dp, end = 12.dp)
                .background(Color(0xFFF0F8FF))
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
                        text = item.first + "元",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = item.second,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
                HorizontalDivider(thickness = 3.dp, color = Color(0xFF232C33))
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            thickness = 2.dp,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
        //.padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFE8F7FF))
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
                    .background(color = Color(0xFFA0C1D1)),
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
    navController: NavController
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




    Text(
        text = "餘額: NT\$ $balance",
        fontSize = 30.sp,
        color = if (balance > 0) {
            Color.Black
        } else if (balance < 0) {
            Color.Black
        } else {
            Color.Black
        }

    )

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = TextDisplay,
        fontSize = 24.sp
    )

    HorizontalDivider(thickness = 2.dp, color = Color.Black)

    Text(
        text = TextDisplay2,
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
                .background(color = Color(0xFFE8F7FF))
                //.padding(16.dp),
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.Start,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text("收入", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(40.dp))
                Text("|", fontSize = 20.sp)
                //VerticalDivider(color = Color.Black)
                Spacer(modifier = Modifier.width(40.dp))
                Text("備註事項", fontSize = 20.sp)

            }

            Column() {
                HorizontalDivider(thickness = 2.dp, color = Color.Black)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(10.dp)
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
                        Text(
                            text = item.first + "元",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            color = Color.Green
                        )
                        Text(
                            text = item.second,
                            fontSize = 18.sp,
                            textAlign = TextAlign.End,
                            color = Color.Green
                        )
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

            Column() {
                HorizontalDivider(thickness = 2.dp, color = Color.Black)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text("支出", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(40.dp))
                Text("|", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(40.dp))
                Text("備註事項", fontSize = 20.sp)

            }

            Column() {
                HorizontalDivider(thickness = 2.dp, color = Color.Black)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(10.dp)
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
                        Text(
                            text = item.first + "元",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            color = Color.Red
                        )
                        Text(
                            text = item.second,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            color = Color.Red
                        )
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
            Column {
                HorizontalDivider(thickness = 2.dp, color = Color.Black)
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
                    .background(color = Color(0xFFA0C1D1)),
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
            containerColor = Color(0xFFE9D758),
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
    var incomeAmount by remember { mutableStateOf("") }
    var incomeNote by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFE8F7FF))
            //.padding(16.dp)
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
                // 顯示收入資料
                items(incomeData) { item ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "+" + item.first + "元",
                                fontSize = 25.sp,
                                textAlign = TextAlign.End,
                                color = Color.Green
                            )
                            Text(
                                text = item.second,
                                fontSize = 25.sp,
                                textAlign = TextAlign.Start,
                                color = Color.Green
                            )
                        }
                        HorizontalDivider(thickness = 2.dp)
                    }
                }

                // 顯示支出資料
                items(expenseData) { item ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "-" + item.first + "元",
                                fontSize = 25.sp,
                                textAlign = TextAlign.End,
                                color = Color.Red
                            )
                            Text(
                                text = item.second,
                                fontSize = 25.sp,
                                textAlign = TextAlign.Start,
                                color = Color.Red
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
                    .background(color = Color(0xFFA0C1D1)),
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
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFE8F7FF))
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

                    /*   //檢查用
                    Text(text = "goalAmount: $goalAmount", fontSize = 18.sp, color = Color.Black)
                    val goalAmountInt = goalAmount.toIntOrNull() ?: 0
                    Text(text = "goalAmountInt: $goalAmountInt", fontSize = 18.sp, color = Color.Black)
                    Text(text = "balance: $balance", fontSize = 18.sp, color = Color.Black)*/

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
                    .background(color = Color(0xFFA0C1D1)),
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
                                    id = if (isLocked) R.drawable.padlock else R.drawable.unlock
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
                                showDialog = true
                            },
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.savedisk),
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
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
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
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
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
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
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