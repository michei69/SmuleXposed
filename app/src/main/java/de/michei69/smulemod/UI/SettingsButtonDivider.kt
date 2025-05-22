package de.michei69.smulemod.UI

import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import de.michei69.smulemod.util.AppCompatUtility
import de.michei69.smulemod.util.dp

object SettingsButtonDivider {
    fun create(): View {
        val buttonDivider = View(AppCompatUtility.context)
        val buttonDividerLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.dp
        )
        buttonDividerLayoutParams.marginStart = 16.dp
        buttonDivider.layoutParams = buttonDividerLayoutParams
        buttonDivider.setBackgroundColor(
            ContextCompat.getColor(
                AppCompatUtility.context,
                Resources.findColorByString("ds_grey_200")
            )
        )
        return buttonDivider
    }
}