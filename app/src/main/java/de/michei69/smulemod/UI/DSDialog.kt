package de.michei69.smulemod.UI

import de.michei69.smulemod.UI.DialogButton.ButtonRegular
import de.michei69.smulemod.UI.DialogButton.ButtonsVertical
import de.michei69.smulemod.util.AppCompatUtility
import de.robv.android.xposed.XposedHelpers.callMethod
import java.lang.reflect.Constructor

object DSDialog {
    var dsDialogCls: Class<*>? = null
    var dsDialogConstructor: Constructor<*>? = null

    var defDialogImageShape: Any? = null

    fun initialize(classLoader: ClassLoader) {
        dsDialogCls = classLoader.loadClass("com.smule.designsystem.dialog.DSDialog")
        dsDialogConstructor = dsDialogCls!!.declaredConstructors.first { constructor -> constructor.parameterCount == 8 }

        val dialogImageShapes = classLoader.loadClass("com.smule.designsystem.dialog.DialogImageShape")
        // TODO
        defDialogImageShape = dialogImageShapes.declaredFields.first { field -> field.type.equals(dialogImageShapes) }.get(null)
    }

    fun showSimpleText(text: String) = showText("Info", text)

    fun showText(title: String, description: String) {
        create(
            title,
            description,
            listOf(
                DialogButton.create(
                    "Ok",
                    ButtonRegular!!
                ) {}
            ),
            ButtonsVertical!!,
            true
        )
    }

    fun create(title: String, description: String, buttons: List<*>, buttonOrientation: Any, cancellable: Boolean) {
        val dialog = dsDialogConstructor!!.newInstance(
            title,
            description,
            null,
            defDialogImageShape,
            null,
            buttons,
            buttonOrientation,
            cancellable
        )
        callMethod(dialog, "show", AppCompatUtility.supportFragmentManager, if (buttonOrientation == ButtonsVertical) "DS_DIALOG_VERTICAL" else "DS_DIALOG_HORIZONTAL")
    }
}