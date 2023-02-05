package com.example.expensetrackerapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/*
 * The class of ExpenseDbHelper had external supervision and guidance because of inadequate
 * knowledge of SQlite
 */
//ExpenseDbHelper: extends the SQLiteOpenHelper, used to create a local database to save the Expenses
class ExpenseDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //defines values that will be used in creation of the database tables
    companion object {
        private const val DATABASE_NAME = "expenses.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "expenses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_DATE = "expenseDate"

    }


    //onCreate: runs on the startup of the class
    override fun onCreate(db: SQLiteDatabase) {
        // create the "expenses" table
        val createTableSql = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT NOT NULL," +
                "$COLUMN_AMOUNT REAL NOT NULL," +
                "$COLUMN_DATE TEXT NOT NULL" +

                ")"
        db.execSQL(createTableSql)


    }
    //onUpgrade: used when the database needs to be upgraded. The implementation should use this method to drop tables, add tables,
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    // insert a new expense into the database
    fun insertExpense(expense: Expense): Long {
        //try method to catch any unexpected exceptions while inserting new data
        try {
            //initializes a writable and readable database
            val db = writableDatabase
            val contentValues = ContentValues()
            //retrieves the information
            contentValues.put(COLUMN_NAME, expense.name)
            contentValues.put(COLUMN_AMOUNT, expense.amount)
            contentValues.put(COLUMN_DATE, expense.date)
            //adds the inserteddata into the databse
            return db.insert(TABLE_NAME, null, contentValues)
        } catch (e: Exception) {
            Log.e("ExpenseDbHelper", "Error inserting expense: $e")
        }
        return -1
    }


    // get all expenses from the database
    fun getAllExpenses(): List<Expense> {
        val expenses = ArrayList<Expense>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name, amount, expenseDate FROM $TABLE_NAME", null)
        //uses the curser value to target specific table elements
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                expenses.add(Expense(id, name, amount, date))
                //logs the information for debugging
                Log.d("Expense", "id: $id, name: $name, amount: $amount, date: $date")
            } while (cursor.moveToNext())
        }
        //closes the cursor to avoid leaks
        cursor.close()
        return expenses
    }


    // get the total amount spent on expenses
    fun getTotalAmountSpent(): Double {
        val db = readableDatabase
        //queries the database to find the amount of a expense
        val cursor = db.rawQuery("SELECT SUM($COLUMN_AMOUNT) FROM $TABLE_NAME", null)
        //moves the cursor to the first value
        cursor.moveToFirst()
        //starts adding the amount to find the total amount
        val sum = cursor.getDouble(0)
        cursor.close()
        //return the total Amount of Expenses
        return sum
    }


}
