package com.jdosantos.gratitudewavev1.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPreferences(
    val uid: String = "",
    val userId: String = "",
    val id: String = "",
    val name: String = "",
    val age: String = "",
    val country: String = "",
    val interests: String = "",
    val profession: String = "",
    val relationship: String = "",
    val selectedInterests: List<String> = mutableListOf(),
    val selectedRelationships: List<String> = mutableListOf(),
    val about: String = "",
    val skip: Boolean = false
): Parcelable
