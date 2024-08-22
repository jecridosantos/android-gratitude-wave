package com.jdosantos.gratitudewavev1.domain.services.impl

import android.content.Context
import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import java.util.Locale
import javax.inject.Inject

class DefaultLanguageProvider @Inject constructor(private val context: Context) : LanguageProvider {
    override fun getCurrentLanguage(): String {
        val locale: Locale =
            context.resources.configuration.locales[0]
        return locale.language
    }
}
