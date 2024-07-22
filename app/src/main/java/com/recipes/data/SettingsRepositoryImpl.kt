package com.recipes.data

import android.content.Context
import androidx.core.content.edit
import com.recipes.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    context: Context
) : SettingsRepository {

    companion object {
        private const val PREFERENCES_NAME = "prefs_settings"

        private const val PERMISSION_WAS_ASKED = "permission_was_asked"
    }

    private val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun isPermissionsWasAsked(): Boolean = prefs.getBoolean(PERMISSION_WAS_ASKED, false)

    override fun setPermissionsWasAsked() {
        prefs.edit { putBoolean(PERMISSION_WAS_ASKED, true) }
    }
}