package de.michei69.smulemod.UI

import de.michei69.smulemod.util.Logging
import java.lang.reflect.Proxy

object UIUtils {
    var func0: Class<*>? = null

    fun initialize(classLoader: ClassLoader) {
        func0 = classLoader.loadClass("kotlin.jvm.functions.Function0")
        Resources.initialize(classLoader)
        DialogButton.initialize(classLoader)
        DSDialog.initialize(classLoader)
        DSTextView.initialize(classLoader)
        Logging.log("[UIUtils] Initialized!")
    }

    fun createFunc0(func: () -> Unit): Any? {
        if (func0 == null) return null
        return Proxy.newProxyInstance(func0!!.classLoader, arrayOf(func0!!)) { _, _, _ -> func() }
    }
}