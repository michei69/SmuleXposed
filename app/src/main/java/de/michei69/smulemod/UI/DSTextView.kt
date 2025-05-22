package de.michei69.smulemod.UI

import android.view.View
import de.michei69.smulemod.util.AppCompatUtility
import de.robv.android.xposed.XposedHelpers.callMethod
import java.lang.reflect.Constructor

object DSTextView {
    var dstextViewCls: Class<*>? = null
    var dstextViewConstructor: Constructor<*>? = null

    fun initialize(classLoader: ClassLoader) {
        dstextViewCls = classLoader.loadClass("com.smule.designsystem.DSTextView")
        dstextViewConstructor = dstextViewCls!!.declaredConstructors.first()
    }

    fun create(text: String): View {
        val instance = dstextViewConstructor!!.newInstance(AppCompatUtility.context)
        callMethod(instance, "setText", text)
        return instance as View
    }
}