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
/*
 * The entirety of MainActivity class has been written by me with minimal help from documentation to
 * utilize imported functions correctly
 */
class MainActivity : Activity() {

    //Late Initializing the Expense Database
    private lateinit var expenseDbHelper: ExpenseDbHelper
    private var listAdapter: ArrayAdapter<Expense>? = null

    //OnCreate function:  responsible for starting p a new instance of the MainActivity app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //selects a display layout
        setContentView(R.layout.activity_main)
        // creates an instance of the ExpenseDbHelper var
        expenseDbHelper = ExpenseDbHelper(this)
        //opens a writable and readable SQLite Databse
        expenseDbHelper.writableDatabase
        //gets all expenses and saves it into list expenses
        val expenses = expenseDbHelper.getAllExpenses()
        //creates a listAdapter using the SimpleDataFormat layout and expenses as resource
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expenses)
        val listView = findViewById<ListView>(R.id.expense_list)
        //sets the ListView.adaptor as the before-mentioned ListAdapter
        listView.adapter = listAdapter
        //finds View by Id and sets the format of the total amount display text
        val totalAmountTextView = findViewById<TextView>(R.id.total_amount)
        val totalAmount = expenseDbHelper.getTotalAmountSpent()
        val formattedString = resources.getString(R.string.total_amount_label, totalAmount.toString())
        totalAmountTextView.text = formattedString

    }

    //addExpenses: Responsible for reading user input and updating the database
    fun addExpense(view: View) {
        //finds the name of expense and retrieves it
        val nameEditText = findViewById<EditText>(R.id.expense_name)
        val name = nameEditText.text.toString()
        //Null-check for the inputed name
        if (!name.isEmpty()) {
            //finds user ammount and retrieves it
            val amountEditText = findViewById<EditText>(R.id.expense_amount)
            val amount = amountEditText.text.toString()
            // Null-check for the amount
            if (!amount.isEmpty()) {
                //converts the amount into double
                val amount = amount.toDouble()
                //sets and formats the date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = dateFormat.format(Date())
                //adds the expense based on the user's input
                var expense = Expense(0, name, amount, date)
                var expenseId = expenseDbHelper.insertExpense(expense)
                //fixes id, adds expense into the database, and displays the uupdated list
                if (expenseId != -1L) {
                    expense.id = expenseId
                    listAdapter?.add(expense)
                    listAdapter?.notifyDataSetChanged()
                    val totalAmountTextView = findViewById<TextView>(R.id.total_amount)
                    val totalAmount = expenseDbHelper.getTotalAmountSpent()
                    val formattedString = resources.getString(R.string.total_amount_label, totalAmount.toString())
                    totalAmountTextView.text = formattedString
                    //resets the user input fields
                    nameEditText.setText("")
                    amountEditText.setText("")
                }
            } else {
                return
            }
        } else {
            return
        }
    }
    //onDestroy: runs after the programs is closed
    override fun onDestroy() {
        super.onDestroy()
        expenseDbHelper.close()
    }



}
