package com.csapp.sqli.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.csapp.sqli.R
import com.csapp.sqli.databinding.ActivityEditorBinding
import com.csapp.sqli.utils.EditorUtils
import com.csapp.sqli.viewmodel.EditorViewModel
import com.csapp.sqli.viewmodel.EditorViewModelFactory

class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding
    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, EditorViewModelFactory(this))[EditorViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        binding.lifecycleOwner = this
        binding.editorViewModel = viewModel
        viewModel.editTextStatement.value = binding.edittextQueryEditor

        viewModel.queryResult.observe(this) { result ->
            EditorUtils.renderResult(binding, result)
        }

        binding.btnQueryEditorRun.setOnClickListener {
            viewModel.execStatement()
        }
    }
}
