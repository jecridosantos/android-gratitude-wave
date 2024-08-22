package com.jdosantos.gratitudewavev1.ui.notification

import android.content.Context
import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedMessageNotificationManager @Inject constructor(private val languageProvider: LanguageProvider) {
    private val titlesEn = listOf(
        "Moment of Gratitude",
        "Remember to Be Grateful",
        "Your Daily Gratitude Note",
        "Time for Gratitude",
        "Give Thanks Today",
        "A Moment for Reflection",
        "It's Time to Be Grateful",
        "Daily Gratitude",
        "Remember to Give Thanks",
        "Gratitude Moment"
    )

    private val titlesEs = listOf(
        "Momento de Agradecer",
        "Recuerda Ser Agradecido",
        "Tu Nota de Gratitud Diaria",
        "Tiempo para la Gratitud",
        "Agradece Hoy",
        "Un Momento para Reflexionar",
        "Es Hora de Agradecer",
        "Gratitud Diaria",
        "Recuerda Agradecer",
        "Momento de Gratitud"
    )

    private val messagesEn = listOf(
        "Take a moment to write down why you're grateful today.",
        "Why are you grateful today? Jot it down in your journal.",
        "Remember to express your gratitude. Write a note now!",
        "Reflect on the good things of today and write them down.",
        "Gratitude changes perspective. Jot down your thoughts.",
        "Write about something that made you happy today.",
        "A grateful mind is a happy mind. What are you grateful for today?",
        "Take a few minutes to reflect and give thanks.",
        "Small moments of gratitude can make a big difference. Write them down!",
        "Take a breath and jot down what you appreciate today."
    )

    private val messagesEs = listOf(
        "Tómate un momento para escribir por qué estás agradecido hoy.",
        "¿Por qué estás agradecido hoy? Anótalo en tu diario.",
        "Recuerda expresar tu gratitud. ¡Escribe una nota ahora!",
        "Reflexiona sobre las cosas buenas de hoy y escríbelas.",
        "La gratitud cambia la perspectiva. Anota tus pensamientos.",
        "Escribe sobre algo que te hizo feliz hoy.",
        "Una mente agradecida es una mente feliz. ¿Qué agradeces hoy?",
        "Dedica unos minutos para reflexionar y agradecer.",
        "Pequeños momentos de gratitud pueden hacer una gran diferencia. ¡Escríbelos!",
        "Tómate un respiro y anota lo que aprecias hoy."
    )

    fun getLocalizedTitles(): List<String> {
        val currentLanguage = languageProvider.getCurrentLanguage()
        return if (currentLanguage == "es") {
            titlesEs
        } else {
            titlesEn
        }
    }

    fun getLocalizedMessages(): List<String> {
        val currentLanguage = languageProvider.getCurrentLanguage()
        return if (currentLanguage == "es") {
            messagesEs
        } else {
            messagesEn
        }
    }

}
