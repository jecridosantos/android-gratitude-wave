package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedRelationshipsManager @Inject constructor(
    private val languageProvider: LanguageProvider
) {
    private val relationshipsEn = listOf(
        "Partner",
        "Family",
        "Friends",
        "Colleagues",
        "Others"
    )

    private val relationshipsEs = listOf(
        "Pareja",
        "Familia",
        "Amigos",
        "Compañeros de trabajo",
        "Otros"
    )

    fun getRelationships(): List<String> {
        return when (languageProvider.getCurrentLanguage()) {
            "es" -> relationshipsEs
            "en" -> relationshipsEn
            else -> relationshipsEn
        }
    }
}
