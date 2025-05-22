package de.michei69.smulemod.mods

import android.util.Log
import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.Logging
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookConstructor
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.XposedHelpers.setObjectField

/**
 * Makes all performances, recordings, whatever joinable
 */
object MakeEverythingJoinable {
    fun runPatch() {
        ModelCreationHooks.getInstance()!!.addListener { type, data ->
            if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("recordings_joinable", true)) return@addListener
            if (type == "SASearchResponse") {
                val recordings = getObjectField(data, "mRecs") as ArrayList<*>
                for (rec in recordings) {
                    setObjectField(rec, "isJoinable", true)
                    if (getObjectField(rec, "isParentJoinable") != null) setObjectField(rec, "isParentJoinable", true)
                    if (getObjectField(rec, "restricted") != null) setObjectField(rec, "restricted", false)
                    setObjectField(rec, "seed", true)
                }
                Logging.log("[Joinable] ${recordings.size} recordings made joinable")
            }
        }
    }
}