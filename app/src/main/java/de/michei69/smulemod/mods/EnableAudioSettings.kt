package de.michei69.smulemod.mods

import android.view.ViewGroup
import android.widget.LinearLayout
import de.michei69.smulemod.UI.Resources.findIdByString

object EnableAudioSettings {
    fun runPatch() {
        FragmentHook.getInstance()!!.addListener { type, layoutInflater, viewGroup, bundle, fragment ->
            if (type != "SettingsFragment") return@addListener
            val audioSettings = viewGroup.findViewById<LinearLayout>(findIdByString("audio_settings_layout"))
            audioSettings.visibility = ViewGroup.VISIBLE
        }
    }
}