package com.example.expensetrackerapp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    private lateinit var expenseDbHelper: ExpenseDbHelper
    private var listAdapter: ArrayAdapter<Expense>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenseDbHelper = ExpenseDbHelper(this)

        val expenseDbHelper = ExpenseDbHelper(this)
        expenseDbHelper.writableDatabase

        val expenses = expenseDbHelper.getAllExpenses()
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expenses)
        val listView = findViewById<ListView>(R.id.expense_list)
        listView.adapter = listAdapter

        val totalAmountTextView = findViewById<TextView>(R.id.total_amount)
        val totalAmount = expenseDbHelper.getTotalAmountSpent()
        totalAmountTextView.text = totalAmount.toString()
    }

    fun addExpense(view: View) {
        val nameEditText = findViewById<EditText>(R.id.expense_name)
        val name = nameEditText.text.toString()

        val amountEditText = findViewById<EditText>(R.id.expense_amount)
        val amount = amountEditText.text.toString().toDouble()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.format(Date())

        var expense = Expense(0, name, amount, date)
        var expenseId = expenseDbHelper.insertExpense(expense)

        if (expenseId != -1L) {
            expense.id = expenseId
            listAdapter?.add(expense)
            listAdapter?.notifyDataSetChanged()
            val totalAmountTextView = findViewById<TextView>(R.id.total_amount)
            val totalAmount = expenseDbHelper.getTotalAmountSpent()
            totalAmountTextView.text = totalAmount.toString()

            nameEditText.setText("")
            amountEditText.setText("")
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        expenseDbHelper.close()
    }



}
