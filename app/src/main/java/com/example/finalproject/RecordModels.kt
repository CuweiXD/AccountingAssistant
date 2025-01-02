package com.example.finalproject

// 收入記錄
data class IncomeRecord(
    val amount: String,    // 金額
    val note: String,  // 備註
    val date: String       // 日期
)

// 支出記錄
data class ExpenseRecord(
    val amount: String,    // 金額
    val note: String,  // 備註
    val date: String       // 日期
)