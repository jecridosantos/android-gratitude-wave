package com.jdosantos.gratitudewavev1.core.common.confignote

import androidx.annotation.StringRes
import com.jdosantos.gratitudewavev1.R


sealed class NoteEmotionConfig(
    @StringRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val message: Int
) {
    data object NoteEmotionConfigHappy : NoteEmotionConfig(
        R.string.note_emotion_icon_Happy,
        R.string.note_emotion_title_Happy,
        R.string.note_emotion_message_Happy
    )

    data object NoteEmotionConfigThankful : NoteEmotionConfig(
        R.string.note_emotion_icon_Thankful,
        R.string.note_emotion_title_Thankful,
        R.string.note_emotion_message_Thankful
    )

    data object NoteEmotionConfigGrateful : NoteEmotionConfig(
        R.string.note_emotion_icon_Grateful,
        R.string.note_emotion_title_Grateful,
        R.string.note_emotion_message_Grateful
    )

    data object NoteEmotionConfigHeartfelt : NoteEmotionConfig(
        R.string.note_emotion_icon_Heartfelt,
        R.string.note_emotion_title_Heartfelt,
        R.string.note_emotion_message_Heartfelt
    )

    data object NoteEmotionConfigHug : NoteEmotionConfig(
        R.string.note_emotion_icon_Hug,
        R.string.note_emotion_title_Hug,
        R.string.note_emotion_message_Hug
    )

    data object NoteEmotionConfigSunflower : NoteEmotionConfig(
        R.string.note_emotion_icon_Sunflower,
        R.string.note_emotion_title_Sunflower,
        R.string.note_emotion_message_Sunflower
    )

    data object NoteEmotionConfigRaisedHands : NoteEmotionConfig(
        R.string.note_emotion_icon_Raised_Hands,
        R.string.note_emotion_title_Raised_Hands,
        R.string.note_emotion_message_Raised_Hands
    )
}