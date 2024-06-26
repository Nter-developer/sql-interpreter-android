package com.csapp.sqli.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        // Not yet implemented
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int,
    ) {
        // Not yet implemented
    }

    suspend fun execStatement(statement: String): Any =
        withContext(Dispatchers.IO) {
            return@withContext try {
                if (isSelectStatement(statement)) {
                    execSelectStatement(statement)
                } else {
                    execNonSelectStatement(statement)
                }
            } catch (e: Exception) {
                Log.e(TAG, MSG.ERROR + e.message)
                MSG.ERROR + e.message
            }
        }

    private suspend fun execNonSelectStatement(statement: String): String =
        withContext(Dispatchers.IO) {
            val database = writableDatabase
            return@withContext try {
                database.execSQL(statement)
                Log.i(TAG, MSG.SUCCESS + statement)
                MSG.SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, MSG.ERROR + e.message)
                MSG.ERROR + e.message
            } finally {
                database.close()
            }
        }

    @SuppressLint("Recycle")
    private suspend fun execSelectStatement(sql: String): Any =
        withContext(Dispatchers.IO) {
            val database = readableDatabase
            return@withContext try {
                val cursor = database.rawQuery(sql, null)
                Log.i(TAG, MSG.SUCCESS)
                cursor
            } catch (e: Exception) {
                Log.e(TAG, MSG.ERROR + "${e.message}")
                MSG.ERROR + e.message
            }
        }

    // Check if a statement starts with "SELECT" ignore case
    private fun isSelectStatement(statement: String): Boolean {
        return statement.startsWith("SELECT", ignoreCase = true)
    }

    companion object {
        private const val TAG = "DB_HELPER"
        private const val DATABASE_NAME = "SQLInterpreter.db"
        private const val DATABASE_VERSION = 1

        private object MSG {
            const val SUCCESS = "Query executed successfully"
            const val ERROR = "Error executing query: "
        }
    }
}
