package com.ptechgithub.cloudflare_scanner

import android.content.Context
import java.io.File

class ScannerRunner(private val context: Context) {
    private val binaryName = "cloudflare_scanner"
    private val dataFiles = listOf("ip.txt", "ipv6.txt")

    fun runScanner(args: List<String>): String {
        return try {
            val workingDir = context.filesDir
            val binaryFile = copyAsset(binaryName, workingDir, executable = true)
            dataFiles.forEach { copyAsset(it, workingDir, executable = false) }

            val command = mutableListOf(binaryFile.absolutePath)
            command.addAll(args)

            val process = ProcessBuilder(command)
                .directory(workingDir)
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().use { it.readText() }.trim()
            val exitCode = process.waitFor()

            if (output.isBlank()) {
                "Command finished.\n\nExit code: $exitCode"
            } else {
                "$output\n\nExit code: $exitCode"
            }
        } catch (e: Exception) {
            "Scanner failed: ${e.message ?: "unknown error"}"
        }
    }

    private fun copyAsset(name: String, targetDir: File, executable: Boolean): File {
        val targetFile = File(targetDir, name)
        context.assets.open(name).use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        if (executable) {
            targetFile.setExecutable(true, true)
        }
        return targetFile
    }
}
