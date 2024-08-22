package com.jdosantos.gratitudewavev1.domain.models

import java.util.Date

data class Note(
    val email: String = "",
    val note: String = "",
    val iv: Any? = null,
    val type: Int? = 0,
    val emotion: Int? = -1,
    val noteTag: NoteTag? = null,
    val date: String = "",
    val idDoc: String = "",
    val updateAt: Date? = null,
    val createAt: Date? = null,
    val color: Int? = -1,
    val generatedByAI: Boolean? = false
)