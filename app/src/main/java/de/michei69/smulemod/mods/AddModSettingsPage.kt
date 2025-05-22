package de.michei69.smulemod.mods

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.airbnb.paris.Paris
import de.michei69.smulemod.UI.Resources.findColorByString
import de.michei69.smulemod.UI.Resources.findIdByString
import de.michei69.smulemod.UI.Resources.findLayoutByString
import de.michei69.smulemod.UI.Resources.findStyleByString
import de.michei69.smulemod.UI.SettingsButtonDivider
import de.michei69.smulemod.UI.SettingsToggle
import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.dp
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.callMethod
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object AddModSettingsPage {
    @SuppressLint("SetTextI18n")
    fun runPatch(classLoader: ClassLoader) {
        FragmentHook.getInstance()!!.addListener { type, layoutInflater, viewGroup, bundle, fragment ->
            if (type != "SettingsFragment") return@addListener
            val parentView = viewGroup.findViewById<LinearLayout>(findIdByString("root"))
            val aboutSmule = viewGroup.findViewById<TextView>(findIdByString("info_header"))

            val pagesParentView = viewGroup.findViewById<RelativeLayout>(findIdByString("grp_container"))
            val settingsScrollView = viewGroup.findViewById<View>(findIdByString("settings_scroll_view"))
            val accountView = pagesParentView.findViewById<View>(findIdByString("account_view"))

            //* Creating the page
            val modSettingsPageCoordLayout = CoordinatorLayout(AppCompatUtility.context)
            modSettingsPageCoordLayout.setBackgroundColor(ContextCompat.getColor(AppCompatUtility.context, findColorByString("ds_white")))
            modSettingsPageCoordLayout.visibility = View.GONE
            modSettingsPageCoordLayout.fitsSystemWindows = true
            modSettingsPageCoordLayout.clipToPadding = false
            val modSettingsPageLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            modSettingsPageCoordLayout.layoutParams = modSettingsPageLayoutParams

            val scrollView = NestedScrollView(AppCompatUtility.context)
            scrollView.layoutParams = modSettingsPageLayoutParams

            val pageLinearLayout = LinearLayout(AppCompatUtility.context)
            pageLinearLayout.orientation = LinearLayout.VERTICAL
            pageLinearLayout.layoutParams = modSettingsPageLayoutParams

            // adding toggles and whatev
            pageLinearLayout.addView(
                SettingsToggle.createSimple("Patch VIP", "patch_vip")
            )
            pageLinearLayout.addView(SettingsButtonDivider.create())

            pageLinearLayout.addView(
                SettingsToggle.createSimple("Disable Analytics", "disable_analytics")
            )
            pageLinearLayout.addView(SettingsButtonDivider.create())

            if (Build.MANUFACTURER.contains("Xiaomi")) {
                pageLinearLayout.addView(
                    SettingsToggle.createSimple("Disable Xiaomi Discrimination", "disable_xiaomi_check")
                )
                pageLinearLayout.addView(SettingsButtonDivider.create())
            }

            pageLinearLayout.addView(
                SettingsToggle.createSimple("Make Recordings Joinable", "recordings_joinable")
            )
            pageLinearLayout.addView(SettingsButtonDivider.create())

            pageLinearLayout.addView(
                SettingsToggle.createSimple("Enable Staff Badge", "staff_badge")
            )
            pageLinearLayout.addView(SettingsButtonDivider.create())

            pageLinearLayout.addView(
                SettingsToggle.createSimple("Disable Location Request Popup", "disable_loc_req")
            )


            // finishing page
            scrollView.addView(pageLinearLayout)
            modSettingsPageCoordLayout.addView(scrollView)
            pagesParentView.addView(modSettingsPageCoordLayout)

            //* Creating and adding the button
            val modSettingsBtn = layoutInflater!!.inflate(findLayoutByString("settings_fragment_button"), null, false) as LinearLayout
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            modSettingsBtn.layoutParams = layoutParams

            val baseFrag = classLoader.loadClass("com.smule.singandroid.BaseFragment")
            val setTitleMethod = baseFrag!!.declaredMethods.first { method ->
                method.parameterCount == 1 &&
                method.parameterTypes[0].equals(CharSequence::class.java)
            }
            setTitleMethod.isAccessible = true
            modSettingsBtn.setOnClickListener {
                settingsScrollView.scrollTo(0,0)
                settingsScrollView.visibility = View.GONE
                modSettingsPageCoordLayout.visibility = View.VISIBLE

                setTitleMethod.invoke(fragment, "Mod Settings")
            }
            val modSettingTextView = modSettingsBtn.findViewById<TextView>(findIdByString("text"))
            modSettingTextView.text = "Mod Settings"

            findAndHookMethod(
                View::class.java,
                "getVisibility",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (param.thisObject != accountView) return
                        if (modSettingsPageCoordLayout.visibility == View.VISIBLE) {
                            param.result = View.VISIBLE
                        }
                    }
                }
            )
            findAndHookMethod(
                View::class.java,
                "setVisibility",
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (param.thisObject != accountView) return
                        modSettingsPageCoordLayout.visibility = View.GONE
                    }
                }
            )

            val idx = parentView.indexOfChild(aboutSmule)
            parentView.addView(modSettingsBtn, idx + 1)

            parentView.addView(SettingsButtonDivider.create(), idx + 2)
        }
    }
}