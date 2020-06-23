package com.example.android.hilt.data

import androidx.room.ForeignKey
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

/**
 * When use this implementation of the LoggerDataSource interface
 * Logs are not saved and don't persist between sessions.
 *
 *  On each activity container, the same instance of LoggerInMemoryDataSource will be provided
 *  since it's annotated with @ActivityScoped inside the Module function.
 */

class LoggerInMemoryDataSource @Inject constructor(): LoggerDataSource{

    private val logs = LinkedList<Log>()

    override fun addLog(msg: String) {
        logs.addFirst(Log(msg, System.currentTimeMillis()))
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(logs)
    }

    override fun removeLogs() {
        logs.clear()
    }
}