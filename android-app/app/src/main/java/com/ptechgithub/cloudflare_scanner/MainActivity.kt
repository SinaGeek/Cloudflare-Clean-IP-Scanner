package com.ptechgithub.cloudflare_scanner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var scannerRunner: ScannerRunner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scannerRunner = ScannerRunner(applicationContext)

        val argsInput = findViewById<EditText>(R.id.args_input)
        val runButton = findViewById<Button>(R.id.run_button)
        val outputView = findViewById<TextView>(R.id.output_view)

        runButton.setOnClickListener {
            val args = parseArgs(argsInput.text.toString())
            runButton.isEnabled = false
            outputView.text = getString(R.string.running_message)

            lifecycleScope.launch {
                val output = withContext(Dispatchers.IO) {
                    scannerRunner.runScanner(args)
                }
                outputView.text = output
                runButton.isEnabled = true
            }
        }
    }

    private fun parseArgs(raw: String): List<String> {
        return raw.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    }
}
