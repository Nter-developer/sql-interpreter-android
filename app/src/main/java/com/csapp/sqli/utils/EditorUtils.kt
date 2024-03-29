package com.csapp.sqli.utils

import android.annotation.SuppressLint
import android.database.Cursor
import android.widget.TableRow
import android.widget.TextView
import com.csapp.sqli.databinding.ActivityEditorBinding
import com.csapp.sqli.utils.ViewUtils.addViewWithParams
import com.csapp.sqli.utils.ViewUtils.applyCommonProperties

object EditorUtils {
    fun renderResult(
        binding: ActivityEditorBinding,
        statementResult: Any?,
    ) {
        statementResult?.let { nonNullResult ->
            when (nonNullResult) {
                is Cursor -> renderQueryResult(binding, nonNullResult)
                is String -> renderStatementResult(binding, nonNullResult)
            }
        }
    }

    @SuppressLint("Range")
    private fun renderQueryResult(
        binding: ActivityEditorBinding,
        cursor: Cursor,
    ) {
        binding.tableQueryExecuteResult.removeAllViews()

        val headerRow = TableRow(binding.root.context)
        val columns = cursor.columnNames
        for (column in columns) {
            val textView = TextView(binding.root.context)
            textView.text = column
            textView.applyCommonProperties()
            headerRow.addViewWithParams(textView)
        }
        binding.tableQueryExecuteResult.addView(headerRow)

        while (cursor.moveToNext()) {
            val dataRow = TableRow(binding.root.context)
            for (column in columns) {
                val textView = TextView(binding.root.context)
                textView.text = cursor.getString(cursor.getColumnIndex(column))
                textView.applyCommonProperties()
                dataRow.addViewWithParams(textView)
            }
            binding.tableQueryExecuteResult.addView(dataRow)
        }
    }

    private fun renderStatementResult(
        binding: ActivityEditorBinding,
        msg: String,
    ) {
        binding.tableQueryExecuteResult.removeAllViews()
        val row = TableRow(binding.root.context)
        val textView = TextView(binding.root.context)
        textView.applyCommonProperties()
        textView.text = msg
        row.addViewWithParams(textView, true)
        binding.tableQueryExecuteResult.addView(row)
    }
}
