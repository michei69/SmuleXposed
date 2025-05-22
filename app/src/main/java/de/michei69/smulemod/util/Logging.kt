package de.michei69.smulemod.util

import de.robv.android.xposed.XposedBridge

/**
 * Logging functions n stuff
 */
object Logging {
    const val TAG = "[SmuleMod]"
    fun log(vararg args: Any) {
        val str = StringBuilder(TAG).apply { args.forEach { append(" $it") } }.toString()
        XposedBridge.log(str)
    }
}