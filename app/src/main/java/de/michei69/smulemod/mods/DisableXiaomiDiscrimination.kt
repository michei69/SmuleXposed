package de.michei69.smulemod.mods

import de.michei69.smulemod.util.AppCompatUtility
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Modifier

/**
 * yes its not actually discrimination, but the fact i'm
 * not allowed to use dark mode on my own darn phone makes
 * absolutely no sense
 */
object DisableXiaomiDiscrimination {
    fun runPatch(classLoader: ClassLoader) {
        val deviceSettings = classLoader.loadClass("com.smule.singandroid.DeviceSettings")
        val isXiaomi = deviceSettings.declaredMethods.first { method ->
            method.parameterCount == 0 &&
            method.returnType.equals(Boolean::class.java) &&
            Modifier.isStatic(method.modifiers) &&
            Modifier.isPublic(method.modifiers)
        }

        XposedBridge.hookMethod(isXiaomi, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("disable_xiaomi_check", true)) return
                param.result = false
            }
        })
    }
}