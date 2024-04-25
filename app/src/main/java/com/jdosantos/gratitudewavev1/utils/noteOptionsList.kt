package com.jdosantos.gratitudewavev1.utils

import com.jdosantos.gratitudewavev1.domain.handles.PublishingOption
import com.jdosantos.gratitudewavev1.domain.enums.Emotion

val publishingOptionLists = listOf(
    PublishingOption.PublishingOptionPrivate,
    PublishingOption.PublishingOptionPublic,
    PublishingOption.PublishingOptionAnonymous,
    PublishingOption.PublishingOptionSend
)

val emotionLists = listOf(
    Emotion.Happy,
    Emotion.Thankful,
    Emotion.Grateful,
    Emotion.Heartfelt,
    Emotion.Hug,
    Emotion.Sunflower,
    Emotion.RaisedHands
)