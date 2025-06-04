package de.michei69.smulemod.mods

import de.michei69.smulemod.util.Logging
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.XposedHelpers.setObjectField
import okhttp3.OkHttpClient
import okhttp3.Request

object FakeProfiles {
    fun runPatch(classLoader: ClassLoader) {
        val profileCustomizations = classLoader.loadClass("com.smule.android.network.models.ProfileCustomizations")
        val jsonUtils = classLoader.loadClass("com.smule.android.utils.JsonUtils")
        val parseJson = jsonUtils.methods.first { method ->
            method.parameterCount == 2 &&
            method.parameterTypes[0].equals(String::class.java) &&
            method.parameterTypes[1].equals(Class::class.java) &&
            method.returnType.equals(Object::class.java)
        }

        val httpClient = OkHttpClient()

        // TODO: add setting
        ModelCreationHooks.getInstance()!!.addListener { type, data ->
            if (type != "SingUserProfile") return@addListener
            //Logging.log("[FakeProfiles] ${data}")
            val accountId = getObjectField(getObjectField(getObjectField(data, "profile"), "accountIcon"), "accountId")
            Logging.log("[FakeProfiles] ${accountId}")

            val resp = httpClient.newCall(
                Request.Builder()
                    .url("https://raw.githubusercontent.com/michei69/SmuleXposed/refs/heads/main/Profiles/$accountId.json") // TODO: allow other hosts
                    .build()
            ).execute()
            if (resp.code == 200) {
                val content = resp.body!!.string()
                //Logging.log("[FakeProfiles] ${content}")

                val obj = parseJson.invoke(null, content, profileCustomizations)
                setObjectField(data, "singProfile", obj)
                //Logging.log("[FakeProfiles] ${obj}")
            } else {
                Logging.log("[FakeProfiles] No customizations for $accountId found")
            }
        }
    }
}