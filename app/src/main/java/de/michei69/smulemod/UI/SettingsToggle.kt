package de.michei69.smulemod.UI

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.airbnb.paris.Paris
import de.michei69.smulemod.UI.Resources.findDrawableByString
import de.michei69.smulemod.UI.Resources.findStyleByString
import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.Logging
import de.michei69.smulemod.util.dp

object SettingsToggle {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun create(text: String, isActive: Boolean, onClick: (isChecked: Boolean) -> Unit): RelativeLayout {
        val holder = RelativeLayout(AppCompatUtility.context)
        val holderLayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        holder.layoutParams = holderLayout

        val txtView = DSTextView.create(text)
        Paris.style(txtView).apply(findStyleByString("DS.Text.Body.Large"))
        val txtViewLayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        txtViewLayout.topMargin = 15.dp
        txtViewLayout.bottomMargin = 16.dp
        txtViewLayout.marginStart = 16.dp
        txtViewLayout.marginEnd = 16.dp
        txtViewLayout.addRule(RelativeLayout.ALIGN_PARENT_START)
        txtViewLayout.addRule(RelativeLayout.ALIGN_PARENT_END)
        txtView.layoutParams = txtViewLayout

        val toggleBtnDrawable = ContextCompat.getDrawable(AppCompatUtility.context, findDrawableByString("toggle_button_background"))
        val toggleBtn = ToggleButton(AppCompatUtility.context)
        val toggleBtnLayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        toggleBtnLayout.addRule(RelativeLayout.CENTER_VERTICAL)
        toggleBtnLayout.addRule(RelativeLayout.ALIGN_PARENT_END)
        toggleBtnLayout.width = toggleBtnDrawable!!.intrinsicWidth
        toggleBtnLayout.height = toggleBtnDrawable.intrinsicHeight
        toggleBtnLayout.marginEnd = 16.dp
        toggleBtn.layoutParams = toggleBtnLayout
        toggleBtn.isClickable = true
        toggleBtn.isFocusable = true
        toggleBtn.setOnCheckedChangeListener { _, isChecked ->
            onClick(isChecked)
        }
        toggleBtn.isChecked = isActive
        toggleBtn.background = null
        toggleBtn.text = ""
        toggleBtn.textOff = ""
        toggleBtn.textOn = ""
        toggleBtn.setPadding(0,0,0,0)
        toggleBtn.buttonDrawable = null
        toggleBtn.minWidth = 0
        toggleBtn.minHeight = 0
        toggleBtn.setBackgroundDrawable(toggleBtnDrawable)

        holder.addView(txtView)
        holder.addView(toggleBtn)
        return holder
    }

    fun createSimple(text: String, key: String, defaultValue: Boolean? = true): View {
        return create(
            text,
            AppCompatUtility.sharedPrefs!!.getBoolean(key, defaultValue ?: true)
        ) { AppCompatUtility.sharedPrefs!!.edit().putBoolean(key, it).apply() }
    }
}