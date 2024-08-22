package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedProfessionsManager @Inject constructor(
    private val languageProvider: LanguageProvider
) {
    private val professionsEn = listOf(
        "Software Engineer",
        "Web Developer",
        "Graphic Designer",
        "Doctor",
        "Lawyer",
        "Teacher",
        "Accountant",
        "Chef",
        "Waiter",
        "Salesperson",
        "Dentist"
    )

    private val professionsEs = listOf(
        "Ingeniero de software",
        "Desarrollador web",
        "Diseñador gráfico",
        "Médico",
        "Abogado",
        "Profesor",
        "Contador",
        "Cocinero",
        "Camarero",
        "Vendedor",
        "Odontólogo"
    )

    fun getProfessions(): List<String> {
        return when (languageProvider.getCurrentLanguage()) {
            "es" -> professionsEs
            "en" -> professionsEn
            else -> professionsEn
        }
    }
}
