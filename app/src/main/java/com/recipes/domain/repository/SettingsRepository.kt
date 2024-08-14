package com.recipes.domain.repository

interface SettingsRepository {
    fun isPermissionsWasAsked(): Boolean
    fun setPermissionsWasAsked()
}