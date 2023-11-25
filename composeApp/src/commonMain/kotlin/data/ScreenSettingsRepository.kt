package data

import com.russhwolf.settings.Settings
import data.ScreenSettings.Companion.IS_DARK_MODE_KEY

data class ScreenSettings(
    val isDarkMode: Boolean,
) {
    companion object {
        const val IS_DARK_MODE_KEY = "isDarkMode"
    }
}

class ScreenSettingsRepository(
    private val settings: Settings = Settings()
) {
    fun get(): ScreenSettings {
        return ScreenSettings(
            isDarkMode = settings.getBoolean(IS_DARK_MODE_KEY, false)
        )
    }

    fun set(screenSettings: ScreenSettings) {
        settings.putBoolean(IS_DARK_MODE_KEY, screenSettings.isDarkMode)
    }
}