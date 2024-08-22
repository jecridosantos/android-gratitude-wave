package com.jdosantos.gratitudewavev1.domain.models

import com.jdosantos.gratitudewavev1.domain.enums.OnboardingSteps

data class Step(
    val title: String,
    val description: String,
    val image: Int? = null,
    val step: OnboardingSteps
)