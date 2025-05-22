package de.michei69.smulemod.UI

import de.michei69.smulemod.UI.UIUtils.createFunc0
import de.robv.android.xposed.XposedHelpers.callMethod
import java.lang.reflect.Constructor

object DialogButton {
    var dialogButtonCls: Class<*>? = null
    var dialogButtonConstructor: Constructor<*>? = null

    var ButtonRegular: Any? = null
    var ButtonPositive: Any? = null
    var ButtonDestructive: Any? = null
    var ButtonDestructiveRegular: Any? = null

    var ButtonsVertical: Any? = null
    var ButtonsHorizontal: Any? = null

    fun initialize(classLoader: ClassLoader) {
        dialogButtonCls = classLoader.loadClass("com.smule.designsystem.dialog.DialogButton")
        dialogButtonConstructor = dialogButtonCls!!.declaredConstructors.first()

        val buttonTypes = classLoader.loadClass("com.smule.designsystem.dialog.DialogButtonType")
        val buttonTypesValues = buttonTypes.getDeclaredMethod("values").invoke(null) as Array<*>

        ButtonRegular = buttonTypesValues[0]
        ButtonPositive = buttonTypesValues[1]
        ButtonDestructive = buttonTypesValues[2]
        ButtonDestructiveRegular = buttonTypesValues[3]

        val buttonOrientations = classLoader.loadClass("com.smule.designsystem.dialog.DialogButtonsOrientation")
        val buttonOrientationsValues = buttonOrientations.getDeclaredMethod("values").invoke(null) as Array<*>

        ButtonsVertical = buttonOrientationsValues[0]
        ButtonsHorizontal = buttonOrientationsValues[1]
    }

    fun create(title: String, type: Any, onClick: () -> Unit): Any? {
        if (dialogButtonCls == null) return null
        return dialogButtonConstructor!!.newInstance(
            title,
            type,
            createFunc0(onClick)
        )
    }
}