package de.michei69.smulemod.mods

import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.Logging
import de.michei69.smulemod.util.UserInfo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers.callMethod
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.XposedHelpers.setObjectField

/**
 * makes u vip
 */
object PatchPro {
    fun runPatch(classLoader: ClassLoader) {
        //* intercept all account icons and make only yours vip
        val modelCreateInst = ModelCreationHooks.getInstance()!!
        modelCreateInst.addListener { type, data ->
            if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("patch_vip", true)) return@addListener
            if (type != "AccountIcon" && type != "SingUserProfile") return@addListener

            val profile: Any = if (type == "AccountIcon") data
            else getObjectField(getObjectField(data, "profile"), "accountIcon")

            if (UserInfo.accountId != 0L && UserInfo.accountId != getObjectField(profile, "accountId")) return@addListener

            val appsWithSubField = getObjectField(profile, "appsWithSubscription")
            callMethod(appsWithSubField, "add", "sing")

            val isVipMethod = profile.javaClass.getMethod("isVip")
            XposedBridge.hookMethod(isVipMethod, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = UserInfo.accountId == 0L || getObjectField(param.thisObject, "accountId") == UserInfo.accountId
                }
            })

            Logging.log("[PRO] [ACCOUNT ICON] ${data}")
        }

        //* intercept subscription check
        val subscriptionStatusResponse = classLoader.loadClass("com.smule.android.billing.managers.SubscriptionManager\$SubscriptionStatusResponse")
        val subscriptionManager = classLoader.loadClass("com.smule.android.billing.managers.SubscriptionManager")
        val parseSubscriptionResponseMethod = subscriptionManager.declaredMethods.first { method ->
            method.parameterCount == 1 &&
            method.parameterTypes[0].equals(subscriptionStatusResponse)
        }
        parseSubscriptionResponseMethod.isAccessible = true
        XposedBridge.hookMethod(parseSubscriptionResponseMethod, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("patch_vip", true)) return
                val sub = param.args[0]
                setObjectField(sub, "status", "PAID")
                setObjectField(sub, "isActive", true)
                setObjectField(sub, "expireAt", System.currentTimeMillis() / 1000 + 1000000000)
                setObjectField(sub, "skipTrial", false)
                setObjectField(sub, "sku", "com.smule.sing_google.sub.year.2024.07")
                setObjectField(sub, "storeCancellable", false)
                Logging.log("[PRO] [SUBSCRIPTION] ${sub}")
                param.args[0] = sub
            }
        })
    }
}