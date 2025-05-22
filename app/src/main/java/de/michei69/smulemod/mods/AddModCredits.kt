package de.michei69.smulemod.mods

import android.app.ActionBar.LayoutParams
import android.app.AndroidAppHelper
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.paris.Paris
import de.michei69.smulemod.UI.DSTextView
import de.michei69.smulemod.UI.Resources.findIdByString
import de.michei69.smulemod.UI.Resources.findStyleByString
import de.robv.android.xposed.XposedHelpers.callMethod

object AddModCredits {
    fun runPatch() {
        FragmentHook.getInstance()!!.addListener { type, layoutInflater, viewGroup, bundle, fragment ->
            if (type != "SettingsFragment") return@addListener
            val rootSettings = viewGroup.findViewById<LinearLayout>(findIdByString("root"))
            val buildInfoView = rootSettings.findViewById<TextView>(findIdByString("build_info"))
            val idx = rootSettings.indexOfChild(buildInfoView)

            val modTxt = DSTextView.create("Modded w/ 💜 by michei69")
            Paris.style(modTxt).apply(findStyleByString("DS_Text_Body_Normal"))
            val layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            callMethod(modTxt, "setLayoutParams", layoutParams)

            rootSettings.addView(modTxt, idx + 2)
        }
    }
}