package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import javax.inject.Inject

class LocalizedPromptStarterManager @Inject constructor(private val languageProvider: LanguageProvider) {
    private val promptStartersEs = listOf(
        "Hoy me levanté con ganas de",
        "Qué bonito es",
        "Me encanta",
        "Es maravilloso pensar en",
        "Una de las cosas que me alegran es",
        "Estoy feliz de",
        "Qué felicidad es",
        "Me siento agradecido por",
        "Una cosa que valoro es",
        "Me hace sonreír pensar en",
        "Es un placer para mí",
        "Estoy agradecido por",
        "Es reconfortante saber que",
        "Una alegría en mi vida es",
        "Me llena de alegría",
        "Siento una gran satisfacción al",
        "Me siento afortunado por",
        "Estoy contento de",
        "Es un privilegio",
        "Qué alegría me da",
        "Qué maravilloso es",
        "Estoy encantado de",
        "Me llena de gratitud",
        "Es inspirador",
        "Me enorgullece",
        "Me siento bendecido por",
        "Es gratificante",
        "Me anima saber que",
        "Estoy agradecido por la oportunidad de",
        "Qué bendición es"
    )

    private val promptStartersEn = listOf(
        "Today I woke up wanting to",
        "How beautiful it is",
        "I love",
        "It's wonderful to think about",
        "One of the things that makes me happy is",
        "I'm happy about",
        "What happiness it is",
        "I feel grateful for",
        "One thing I value is",
        "It makes me smile to think about",
        "It's a pleasure for me",
        "I'm grateful for",
        "It's comforting to know that",
        "A joy in my life is",
        "It fills me with joy",
        "I feel a great satisfaction when",
        "I feel fortunate to",
        "I'm glad about",
        "It's a privilege",
        "What joy it gives me",
        "How wonderful it is",
        "I'm delighted to",
        "It fills me with gratitude",
        "It's inspiring",
        "I'm proud",
        "I feel blessed by",
        "It's rewarding",
        "It encourages me to know that",
        "I'm grateful for the opportunity to",
        "What a blessing it is"
    )

    fun getLocalizedPromptStarters(): List<String> {
        return when (languageProvider.getCurrentLanguage()) {
            "es" -> promptStartersEs
            else -> promptStartersEn
        }
    }
}
