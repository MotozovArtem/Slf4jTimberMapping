package io.rienel.slf4j

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Timber Tree to log specific log levels to a file
 */
class TimberTreeToFile(
    private val logLevel: Int,
    private val logDirName: String = "log_cat",
    private val dateFormat: DateFormat = DEFAULT_DATE_FORMAT,
    private val logStorageTimeInDays: Int = 7
) : Timber.DebugTree() {
    private val logQueue = LinkedBlockingQueue<String>()
    private var dequeueThread: Thread? = null
    private val isRunning = AtomicBoolean(false)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= logLevel) {
            logQueue.add(
                String.format(
                    "%s %s/%s : %s",
                    dateFormat.format(Date()),
                    getPriorityString(priority),
                    tag,
                    message
                )
            )
        }
    }

    private fun getPriorityString(priority: Int) = when (priority) {
        Log.ASSERT -> "ASSERT"
        Log.ERROR -> "E"
        Log.WARN -> "W"
        Log.INFO -> "I"
        Log.DEBUG -> "D"
        Log.VERBOSE -> "V"
        else -> ""
    }

    private fun createLogFile(context: Context) {
        if (dequeueThread?.isAlive == true) {
            stopLoggingThread()
        }
        dequeueThread = Thread {
            val pInfo: PackageInfo
            var version: String? = ""
            try {
                pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }
                version = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.w("Failed to get package info")
            }
            val dir = File(context.getExternalFilesDir(null), logDirName)
            dir.mkdirs()
            val currentCalendar = Calendar.getInstance()
            currentCalendar.add(Calendar.DAY_OF_YEAR, -logStorageTimeInDays)
            getAllFilesInDir(dir)?.let { allLogs ->
                for (f in allLogs) {
                    if (f.exists()) {
                        val lastModified = Date(f.lastModified())
                        if (lastModified.before(currentCalendar.time)) {
                            f.delete()
                        }
                    }
                }
            }
            val sdf = SimpleDateFormat(FILE_NAME_DATE_FORMAT, Locale.US)
            var logFile: File
            var fileIndex = 0
            do {
                val fileName = if (fileIndex == 0) {
                    String.format("%s_%s.txt", version, sdf.format(Date()))
                } else {
                    String.format("%s_%s_%s.txt", version, sdf.format(Date()), fileIndex)
                }
                logFile = File(dir, fileName)
                fileIndex++
            } while (logFile.exists())

            PrintStream(
                FileOutputStream(logFile, true),
                true,
                StandardCharsets.UTF_8.toString()
            ).use {
                while (isRunning.get()) {
                    try {
                        val message = logQueue.take()
                        it.println(message)
                    } catch (e: InterruptedException) {
                        Timber.w("Failed to receive message from logQueue")
                    }
                }
            }
        }
        isRunning.set(true)
        dequeueThread?.start()
    }

    fun stopLoggingThread() {
        if (dequeueThread != null) {
            isRunning.set(false)
            dequeueThread?.interrupt()
            dequeueThread = null
        }
    }

    companion object {
        private val DEFAULT_DATE_FORMAT = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
        private const val FILE_NAME_DATE_FORMAT = "yyyy_MM_dd_HH_mm"

        fun create(context: Context, logLevel: Int): TimberTreeToFile {
            val timberTreeToFile = TimberTreeToFile(logLevel).apply {
                createLogFile(context)
            }
            return timberTreeToFile
        }

        private fun getAllFilesInDir(dir: File?): ArrayList<File>? {
            if (dir == null) return null
            val files = ArrayList<File>()
            val dirList = Stack<File>()
            dirList.push(dir)
            while (!dirList.isEmpty()) {
                val dirCurrent = dirList.pop()
                val fileList = dirCurrent.listFiles()
                if (fileList != null) {
                    for (aFileList in fileList) {
                        if (aFileList.isDirectory) dirList.push(aFileList) else files.add(aFileList)
                    }
                }
            }
            return files
        }
    }
}