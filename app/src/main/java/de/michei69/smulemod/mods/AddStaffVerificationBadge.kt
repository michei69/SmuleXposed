package de.michei69.smulemod.mods

import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.UserInfo
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.setObjectField

/**
 * Adds the staff badge to your profile
 *
 * Only visual
 */
object AddStaffVerificationBadge {
    fun runPatch(classLoader: ClassLoader) {
        val accVerifiedType = classLoader.loadClass("com.smule.android.network.models.account.AccountVerifiedType")
        val strToEnumMethod = accVerifiedType.declaredMethods.first { method ->
            method.parameterCount == 1 &&
                    method.parameterTypes[0].equals(String::class.java) &&
                    method.returnType.equals(accVerifiedType)
        }
        val staffVerif = strToEnumMethod.invoke(null, "STAFF")

        ModelCreationHooks.getInstance()!!.addListener { type, data ->
            if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("staff_badge", true)) return@addListener
            if (type != "AccountIcon" && type != "SingUserProfile") return@addListener

            val profile: Any = if (type == "AccountIcon") data
            else XposedHelpers.getObjectField(
                XposedHelpers.getObjectField(data, "profile"),
                "accountIcon"
            )
            if (UserInfo.accountId != 0L && UserInfo.accountId != XposedHelpers.getObjectField(
                    profile,
                    "accountId"
                )
            ) return@addListener

            setObjectField(profile, "verifiedType", staffVerif)
        }
    }
}