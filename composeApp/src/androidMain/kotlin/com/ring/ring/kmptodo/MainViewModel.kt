package com.ring.ring.kmptodo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import data.ScreenSettings
import data.ScreenSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val screenSettingsRepository: ScreenSettingsRepository,
) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()

    init {
        updateIsDarkMode()
    }

    fun setDarkMode(isDarkMode: Boolean) {
        saveScreenSettings(isDarkMode)
        updateIsDarkMode()
    }

    private fun updateIsDarkMode() {
        _isDarkMode.value = screenSettingsRepository.get().isDarkMode
    }

    private fun saveScreenSettings(isDarkMode: Boolean) {
        val screenSettings = ScreenSettings(isDarkMode)
        screenSettingsRepository.set(screenSettings)
    }
}