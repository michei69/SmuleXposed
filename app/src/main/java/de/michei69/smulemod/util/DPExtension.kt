package de.michei69.smulemod.util

val Int.dp: Int
    get() = (this * AppCompatUtility.context.resources.displayMetrics.density).toInt()

val Float.dp: Float
    get() = (this * AppCompatUtility.context.resources.displayMetrics.density)