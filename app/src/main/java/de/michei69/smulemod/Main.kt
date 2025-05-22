package de.michei69.smulemod

import android.app.AndroidAppHelper
import de.michei69.smulemod.UI.UIUtils
import de.michei69.smulemod.mods.AddModCredits
import de.michei69.smulemod.mods.AddModSettingsPage
import de.michei69.smulemod.mods.AddStaffVerificationBadge
import de.michei69.smulemod.mods.DisableAnalytics
import de.michei69.smulemod.mods.DisablePermissionRequest
import de.michei69.smulemod.mods.DisableXiaomiDiscrimination
import de.michei69.smulemod.mods.EnableAudioSettings
import de.michei69.smulemod.mods.FixUserProfileNull
import de.michei69.smulemod.mods.FragmentHook
import de.michei69.smulemod.mods.JSONHook
import de.michei69.smulemod.mods.MakeEverythingJoinable
import de.michei69.smulemod.mods.ModelCreationHooks
import de.michei69.smulemod.mods.PatchPro
import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.AppInfo
import de.michei69.smulemod.util.Logging
import de.michei69.smulemod.util.UserInfo
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage


class Main : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (!lpparam?.packageName.equals("com.smule.singandroid")) return
        Logging.log("Loaded package: ${lpparam?.packageName}")

        if (lpparam != null)
            hooking(lpparam)
        else
            Logging.log("lpparam is null")
    }

    fun hooking(lpparam: XC_LoadPackage.LoadPackageParam) {
        AppCompatUtility.hookAppCompat(lpparam.classLoader)
        UIUtils.initialize(lpparam.classLoader)
        SignatureUtil.hookCertCheck(lpparam.classLoader)
        DisableAnalytics.hookAnalytics(lpparam.classLoader)
        UserInfo.hookLoginInfo(lpparam.classLoader)

        if (ModelCreationHooks.getInstance() == null) {
            ModelCreationHooks(lpparam.classLoader)
        }
        if (FragmentHook.getInstance() == null) {
            FragmentHook(lpparam.classLoader)
        }
        JSONHook.runPatch(lpparam.classLoader)

        PatchPro.runPatch(lpparam.classLoader)
        AddStaffVerificationBadge.runPatch(lpparam.classLoader)
        FixUserProfileNull.runPatch(lpparam.classLoader)
        MakeEverythingJoinable.runPatch()
        AddModCredits.runPatch()
        EnableAudioSettings.runPatch()
        DisableXiaomiDiscrimination.runPatch(lpparam.classLoader)
        AddModSettingsPage.runPatch(lpparam.classLoader)
        DisablePermissionRequest.runPatch(lpparam.classLoader)
    }
}