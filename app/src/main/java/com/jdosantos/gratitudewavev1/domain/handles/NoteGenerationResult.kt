package com.jdosantos.gratitudewavev1.domain.handles

sealed class NoteGenerationResult {
    data object CanGenerate : NoteGenerationResult()
    data class MustWait(val waitTime: Long) : NoteGenerationResult()
    data object DailyLimitReached : NoteGenerationResult()
}
