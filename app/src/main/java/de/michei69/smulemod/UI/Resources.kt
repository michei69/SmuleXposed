package de.michei69.smulemod.UI

import de.robv.android.xposed.XposedHelpers.getObjectField

object Resources {
    var Rcls: Class<*>? = null
    var Rid: Class<*>? = null
    var Rstyle: Class<*>? = null
    var Rattr: Class<*>? = null
    var Rlayout: Class<*>? = null
    var Rcolor: Class<*>? = null
    var Rdrawable: Class<*>? = null

    fun initialize(classLoader: ClassLoader) {
        Rcls = classLoader.loadClass("com.smule.singandroid.R")
        Rid = classLoader.loadClass("com.smule.singandroid.R\$id")
        Rstyle = classLoader.loadClass("com.smule.singandroid.R\$style")
        Rattr = classLoader.loadClass("com.smule.singandroid.R\$attr")
        Rlayout = classLoader.loadClass("com.smule.singandroid.R\$layout")
        Rcolor = classLoader.loadClass("com.smule.singandroid.R\$color")
        Rdrawable = classLoader.loadClass("com.smule.singandroid.R\$drawable")
    }

    fun findIdByString(id: String): Int {
        return Rid!!.fields.find { field -> field.name == id }!!.getInt(null)
    }

    fun findStyleByString(id: String): Int {
        val id2 = id.replace(".", "_")
        return Rstyle!!.fields.find { field -> field.name == id2 }!!.getInt(null)
    }

    fun findAttrByString(id: String): Int {
        return Rattr!!.fields.find { field -> field.name == id }!!.getInt(null)
    }

    fun findLayoutByString(id: String): Int {
        return Rlayout!!.fields.find { field -> field.name == id }!!.getInt(null)
    }

    fun findColorByString(id: String): Int {
        return Rcolor!!.fields.find { field -> field.name == id }!!.getInt(null)
    }

    fun findDrawableByString(id: String): Int {
        return Rdrawable!!.fields.find { field -> field.name == id }!!.getInt(null)
    }
}