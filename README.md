# Smule Mod

A home-made mod for Smule

# Features

- Free VIP features
- Disables analytics (speeds up app and saves internet)
- Patches signature verification
- Enables the dark mode option on Xiaomi Devices
- Disables the location permission request popup on startup
- Ability to add a staff badge to your profile (local, visual only, but why not)
- Custom fake VIP profiles (sorta like USRBG)

# VIP on non-VIP accounts

- Your performances will only last 3 days, instead of 7
- Profile customization is not available (the FakeProfiles mod does allow for something like USRBG does on discord)
- You cannot re-cast your performance as a seed (invitation) once it expires
- Everything else _probably_ works, but i havent tried all of it lol

# Installation

- Download the mod from the [releases page](https://github.com/michei69/SmuleXposed/releases) or compile it yourself
- Install it via [LSPosed (root)](https://github.com/JingMatrix/LSPosed) or via [LSPatch (rootless)](https://github.com/JingMatrix/LSPatch)
- Restart the app

# Notes

- This mod is still very much in a work in progress, and there's still a lot to do
- It's also my first time trying out Xposed, so please report any bugs you find
- The app doesn't really have an icon as of yet
- Mod settings are available inside of Smule's own settings
- This mod uses Smule's own sharedPreferences, so all mod data will get deleted if you uninstall Smule
- This mod was mostly tested on Smule v12.2.3 build 2544. It might break with updates, and it might not work at all on older versions
- Some stuff might be broken (for example, joining recordings doesn't work atm, i'll have to fix it _eventually_) due to _MagicPreferences_ (feature flags) which i haven't tested yet
- On Xiaomi devices running MIUI / HyperOS, in order for dark mode to work, you must both enable it in smule's settings, and also enable it under display -> dark mode options -> individual apps -> smule

###### P.S. don't yell at me for my absolutely dogshit code :3   i don't use kotlin that often
