package com.mohanjp.runningtracker.common.utils.presentation

import android.content.Context
import androidx.annotation.StringRes
import com.mohanjp.runningtracker.R

sealed interface UiText {

    data class DynamicString(val value: String): UiText

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiText

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }

    companion object {
        internal val unknownError: UiText = StringResource(R.string.unknown_error)
        internal val somethingWentWrong: UiText = StringResource(R.string.something_went_wrong_try_later)
        internal val noInternet: UiText = StringResource(R.string.check_your_internet)
        internal val emptyString: UiText = DynamicString("")
    }
}