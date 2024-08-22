package com.jdosantos.gratitudewavev1.domain.handles

import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedMessageManager @Inject constructor(private val languageProvider: LanguageProvider) {

    enum class Language {
        EN, ES
    }

    companion object {
        const val RESET_SEND_EMAIL_SUCCESS_EN = "Verification link sent"
        const val RESET_SEND_EMAIL_ERROR_EN = "Failed to send link"

        const val RESET_SEND_EMAIL_SUCCESS_ES = "Link de verificación enviado"
        const val RESET_SEND_EMAIL_ERROR_ES = "No se pudo enviar link"

        const val WELCOME_GOOD_MORNING_EN = "Good morning"
        const val WELCOME_GOOD_AFTERNOON_EN = "Good afternoon"
        const val WELCOME_GOOD_NIGHT_EN = "Good night"

        const val WELCOME_GOOD_MORNING_ES = "Buenos días"
        const val WELCOME_GOOD_AFTERNOON_ES = "Buenas tardes"
        const val WELCOME_GOOD_NIGHT_ES = "Buenas noches"

        const val SUCCESS_FEEDBACK_EN = "Thank you!!"
        const val ERROR_FEEDBACK_EN = "There was an error :("

        const val SUCCESS_FEEDBACK_ES = "Gracias!!"
        const val ERROR_FEEDBACK_ES = "Hubo un error :("

        const val GENERIC_ERROR_EN = "An error has occurred"
        const val GENERIC_ERROR_ES = "Ha ocurrido un error"

        const val PROMPT_MESSAGE_AGE_EN = "My age (but don't mention my age) "
        const val PROMPT_MESSAGE_COUNTRY_EN = "I live in "
        const val PROMPT_MESSAGE_PROFESSION_EN = "I am a "
        const val PROMPT_MESSAGE_INTERESTS_EN = "I am interested in "
        const val PROMPT_MESSAGE_RELATIONSHIPS_EN = "My close people are "
        const val PROMPT_MESSAGE_ABOUT_EN = "An important event in my life was "
        const val PROMPT_MESSAGE_RANDOM_PROMPT_STARTER_EN =
            "I want to write a motivating and grateful phrase using these keywords:"
        const val PROMPT_MESSAGE_RANDOM_PART_EN = "Any aspect that may be important"
        const val PROMPT_MESSAGE_FINAL_PROMPT_EN =
            "Please create a positive and encouraging sentence that sounds like I wrote it myself."
        const val PROMPT_MESSAGE_WORDS_TO_IMPROVE_EN = "Words to improve: "
        const val PROMPT_MESSAGE_AGE_ES = "Mi edad (pero no menciones mi edad) "
        const val PROMPT_MESSAGE_COUNTRY_ES = "Vivo en "
        const val PROMPT_MESSAGE_PROFESSION_ES = "Soy "
        const val PROMPT_MESSAGE_INTERESTS_ES = "Me interesan "
        const val PROMPT_MESSAGE_RELATIONSHIPS_ES = "Mis personas cercanas son "
        const val PROMPT_MESSAGE_ABOUT_ES = "Un evento importante en mi vida fue "
        const val PROMPT_MESSAGE_RANDOM_PROMPT_STARTER_ES =
            "Quiero escribir una frase motivadora y agradecida usando estas palabras clave:"
        const val PROMPT_MESSAGE_RANDOM_PART_ES = "Cualquier aspecto que pueda ser importante"
        const val PROMPT_MESSAGE_FINAL_PROMPT_ES =
            "Por favor, crea una frase positiva y alentadora que suene como si la escribiera yo mismo."

        const val PROMPT_MESSAGE_WORDS_TO_IMPROVE_ES = "Palabras a mejorar: "
    }

    private val language: Language

    init {
        val currentLanguage = languageProvider.getCurrentLanguage()
        language = when (currentLanguage) {
            "es" -> Language.ES
            "en" -> Language.EN
            else -> Language.EN
        }
    }

    fun getMessage(key: MessageKey): String {
        return when (language) {
            Language.EN -> getEnglishMessage(key)
            Language.ES -> getSpanishMessage(key)
        }
    }

    private fun getEnglishMessage(key: MessageKey): String {
        return when (key) {
            MessageKey.RESET_SEND_EMAIL_SUCCESS -> RESET_SEND_EMAIL_SUCCESS_EN
            MessageKey.RESET_SEND_EMAIL_ERROR -> RESET_SEND_EMAIL_ERROR_EN
            MessageKey.WELCOME_GOOD_MORNING -> WELCOME_GOOD_MORNING_EN
            MessageKey.WELCOME_GOOD_AFTERNOON -> WELCOME_GOOD_AFTERNOON_EN
            MessageKey.WELCOME_GOOD_NIGHT -> WELCOME_GOOD_NIGHT_EN
            MessageKey.SUCCESS_FEEDBACK -> SUCCESS_FEEDBACK_EN
            MessageKey.ERROR_FEEDBACK -> ERROR_FEEDBACK_EN
            MessageKey.GENERIC_ERROR -> GENERIC_ERROR_EN
            MessageKey.PROMPT_MESSAGE_AGE -> PROMPT_MESSAGE_AGE_EN
            MessageKey.PROMPT_MESSAGE_COUNTRY -> PROMPT_MESSAGE_COUNTRY_EN
            MessageKey.PROMPT_MESSAGE_PROFESSION -> PROMPT_MESSAGE_PROFESSION_EN
            MessageKey.PROMPT_MESSAGE_INTERESTS -> PROMPT_MESSAGE_INTERESTS_EN
            MessageKey.PROMPT_MESSAGE_RELATIONSHIPS -> PROMPT_MESSAGE_RELATIONSHIPS_EN
            MessageKey.PROMPT_MESSAGE_ABOUT -> PROMPT_MESSAGE_ABOUT_EN
            MessageKey.PROMPT_MESSAGE_RANDOM_PROMPT_STARTER -> PROMPT_MESSAGE_RANDOM_PROMPT_STARTER_EN
            MessageKey.PROMPT_MESSAGE_RANDOM_PART -> PROMPT_MESSAGE_RANDOM_PART_EN
            MessageKey.PROMPT_MESSAGE_FINAL_PROMPT -> PROMPT_MESSAGE_FINAL_PROMPT_EN
            MessageKey.PROMPT_MESSAGE_WORDS_TO_IMPROVE -> PROMPT_MESSAGE_WORDS_TO_IMPROVE_EN
        }
    }

    private fun getSpanishMessage(key: MessageKey): String {
        return when (key) {
            MessageKey.RESET_SEND_EMAIL_SUCCESS -> RESET_SEND_EMAIL_SUCCESS_ES
            MessageKey.RESET_SEND_EMAIL_ERROR -> RESET_SEND_EMAIL_ERROR_ES
            MessageKey.WELCOME_GOOD_MORNING -> WELCOME_GOOD_MORNING_ES
            MessageKey.WELCOME_GOOD_AFTERNOON -> WELCOME_GOOD_AFTERNOON_ES
            MessageKey.WELCOME_GOOD_NIGHT -> WELCOME_GOOD_NIGHT_ES
            MessageKey.SUCCESS_FEEDBACK -> SUCCESS_FEEDBACK_ES
            MessageKey.ERROR_FEEDBACK -> ERROR_FEEDBACK_ES
            MessageKey.GENERIC_ERROR -> GENERIC_ERROR_ES
            MessageKey.PROMPT_MESSAGE_AGE -> PROMPT_MESSAGE_AGE_ES
            MessageKey.PROMPT_MESSAGE_COUNTRY -> PROMPT_MESSAGE_COUNTRY_ES
            MessageKey.PROMPT_MESSAGE_PROFESSION -> PROMPT_MESSAGE_PROFESSION_ES
            MessageKey.PROMPT_MESSAGE_INTERESTS -> PROMPT_MESSAGE_INTERESTS_ES
            MessageKey.PROMPT_MESSAGE_RELATIONSHIPS -> PROMPT_MESSAGE_RELATIONSHIPS_ES
            MessageKey.PROMPT_MESSAGE_ABOUT -> PROMPT_MESSAGE_ABOUT_ES
            MessageKey.PROMPT_MESSAGE_RANDOM_PROMPT_STARTER -> PROMPT_MESSAGE_RANDOM_PROMPT_STARTER_ES
            MessageKey.PROMPT_MESSAGE_RANDOM_PART -> PROMPT_MESSAGE_RANDOM_PART_ES
            MessageKey.PROMPT_MESSAGE_FINAL_PROMPT -> PROMPT_MESSAGE_FINAL_PROMPT_ES
            MessageKey.PROMPT_MESSAGE_WORDS_TO_IMPROVE -> PROMPT_MESSAGE_WORDS_TO_IMPROVE_ES
        }
    }

    enum class MessageKey {
        RESET_SEND_EMAIL_SUCCESS,
        RESET_SEND_EMAIL_ERROR,
        WELCOME_GOOD_MORNING,
        WELCOME_GOOD_AFTERNOON,
        WELCOME_GOOD_NIGHT,
        SUCCESS_FEEDBACK,
        ERROR_FEEDBACK,
        GENERIC_ERROR,
        PROMPT_MESSAGE_AGE,
        PROMPT_MESSAGE_COUNTRY,
        PROMPT_MESSAGE_PROFESSION,
        PROMPT_MESSAGE_INTERESTS,
        PROMPT_MESSAGE_RELATIONSHIPS,
        PROMPT_MESSAGE_ABOUT,
        PROMPT_MESSAGE_RANDOM_PROMPT_STARTER,
        PROMPT_MESSAGE_RANDOM_PART,
        PROMPT_MESSAGE_FINAL_PROMPT,
        PROMPT_MESSAGE_WORDS_TO_IMPROVE
    }
}