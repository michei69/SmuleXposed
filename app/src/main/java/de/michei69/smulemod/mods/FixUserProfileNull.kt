package de.michei69.smulemod.mods

import de.michei69.smulemod.util.Logging
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.XposedHelpers.setObjectField

/**
 * Fixes null profileCustomizations for non-vip
 */
object FixUserProfileNull {
    fun runPatch(classLoader: ClassLoader) {
        val profileCustomizations = classLoader.loadClass("com.smule.android.network.models.ProfileCustomizations")
        val profileCustomizationsConstructor = profileCustomizations.getDeclaredConstructor()

        ModelCreationHooks.getInstance()!!.addListener { type, data ->
            if (type != "SingUserProfile") return@addListener
            if (getObjectField(data, "singProfile") == null) {
                val profile = profileCustomizationsConstructor.newInstance()
                setObjectField(data, "singProfile", profile)
                Logging.log("[FIX] Fixed null sing profile")
            }
        }
    }
}