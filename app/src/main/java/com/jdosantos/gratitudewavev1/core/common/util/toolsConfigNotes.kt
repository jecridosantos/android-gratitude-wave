package com.jdosantos.gratitudewavev1.core.common.util

import com.jdosantos.gratitudewavev1.core.common.confignote.NoteEmotionConfig
import com.jdosantos.gratitudewavev1.core.common.confignote.NoteTypeConfig

val noteTypeConfigLists = listOf(
    NoteTypeConfig.NoteTypeConfigPrivate,
    NoteTypeConfig.NoteTypeConfigPublic,
    NoteTypeConfig.NoteTypeConfigAnonymous,
    NoteTypeConfig.NoteTypeConfigSend
)

val noteEmotionConfigLists = listOf(
    NoteEmotionConfig.NoteEmotionConfigHappy,
    NoteEmotionConfig.NoteEmotionConfigThankful,
    NoteEmotionConfig.NoteEmotionConfigGrateful,
    NoteEmotionConfig.NoteEmotionConfigHeartfelt,
    NoteEmotionConfig.NoteEmotionConfigHug,
    NoteEmotionConfig.NoteEmotionConfigSunflower,
    NoteEmotionConfig.NoteEmotionConfigRaisedHands
)