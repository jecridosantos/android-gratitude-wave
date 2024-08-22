package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedInterestsManager @Inject constructor(
    private val languageProvider: LanguageProvider
) {
    private val interestsEn = listOf(
        "Music",
        "Reading",
        "Nature",
        "Travel",
        "Sports",
        "Gastronomy",
        "Technology",
        "Art",
        "Others"
    )

    private val interestsEs = listOf(
        "Música",
        "Lectura",
        "Naturaleza",
        "Viajes",
        "Deportes",
        "Gastronomía",
        "Tecnología",
        "Arte",
        "Otros"
    )

    fun getInterests(): List<String> {
        return when (languageProvider.getCurrentLanguage()) {
            "es" -> interestsEs
            "en" -> interestsEn
            else -> interestsEn
        }
    }
}
