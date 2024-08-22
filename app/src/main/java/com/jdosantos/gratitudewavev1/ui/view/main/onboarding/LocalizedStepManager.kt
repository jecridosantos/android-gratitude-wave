package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import com.jdosantos.gratitudewavev1.domain.enums.OnboardingSteps
import com.jdosantos.gratitudewavev1.domain.models.Step
import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedStepManager @Inject constructor(private val languageProvider: LanguageProvider) {

    private val stepsEn = listOf(
        Step(
            title = "Update your information",
            description = "We want to know you a bit more.",
            step = OnboardingSteps.STEP_ONE
        ),
        Step(
            title = "What do you identify with?",
            description = "Select aspects of your life that you are passionate about.",
            step = OnboardingSteps.STEP_TWO
        ),
        Step(
            title = "What do you do?",
            description = "You can write if you don't find it in the list.",
            step = OnboardingSteps.STEP_THREE
        ),
        Step(
            title = "Who would you like to express gratitude to?",
            description = "Select those important people in your life.",
            step = OnboardingSteps.STEP_FOUR
        ),
        Step(
            title = "Has there been any recent event in your life that you would like to reflect on with gratitude?",
            description = "You can indicate any event that is important to you, something small can be very significant.",
            step = OnboardingSteps.STEP_FIVE
        ),
        Step(
            title = "",
            description = "",
            step = OnboardingSteps.STEP_SIX
        )
    )

    private val stepsEs = listOf(
        Step(
            title = "Actualiza tu información",
            description = "Queremos conocerte un poco más.",
            step = OnboardingSteps.STEP_ONE
        ),
        Step(
            title = "¿Con qué te identificas?",
            description = "Selecciona aspectos de tu vida que te apasionan.",
            step = OnboardingSteps.STEP_TWO
        ),
        Step(
            title = "¿A qué te dedicas?",
            description = "Puedes escribir si no encuentras en la lista.",
            step = OnboardingSteps.STEP_THREE
        ),
        Step(
            title = "¿Con quiénes te gustaría expresar gratitud?",
            description = "Selecciona aquellas personas importantes en tu vida.",
            step = OnboardingSteps.STEP_FOUR
        ),
        Step(
            title = "¿Ha habido algún evento reciente en tu vida que te gustaría reflexionar con gratitud?",
            description = "Puedes indicar cualquier evento que para ti sea importante, algo pequeño puede ser sumamente significativo.",
            step = OnboardingSteps.STEP_FIVE
        ),
        Step(
            title = "",
            description = "",
            step = OnboardingSteps.STEP_SIX
        )
    )

    fun getSteps(): List<Step> {
        val language = languageProvider.getCurrentLanguage()
        return if (language == "es") {
            stepsEs
        } else {
            stepsEn
        }
    }
}
