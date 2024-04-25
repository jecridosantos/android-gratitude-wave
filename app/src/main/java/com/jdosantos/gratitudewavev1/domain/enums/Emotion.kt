package com.jdosantos.gratitudewavev1.domain.enums

import androidx.annotation.StringRes
import com.jdosantos.gratitudewavev1.R

enum class Emotion(
    @StringRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val message: Int
) {
    Happy(
        R.string.note_emotion_icon_Happy,
        R.string.note_emotion_title_Happy,
        R.string.note_emotion_message_Happy
    ),

    Thankful(
        R.string.note_emotion_icon_Thankful,
        R.string.note_emotion_title_Thankful,
        R.string.note_emotion_message_Thankful
    ),

    Grateful(
        R.string.note_emotion_icon_Grateful,
        R.string.note_emotion_title_Grateful,
        R.string.note_emotion_message_Grateful
    ),

    Heartfelt(
        R.string.note_emotion_icon_Heartfelt,
        R.string.note_emotion_title_Heartfelt,
        R.string.note_emotion_message_Heartfelt
    ),

    Hug(
        R.string.note_emotion_icon_Hug,
        R.string.note_emotion_title_Hug,
        R.string.note_emotion_message_Hug
    ),

    Sunflower(
        R.string.note_emotion_icon_Sunflower,
        R.string.note_emotion_title_Sunflower,
        R.string.note_emotion_message_Sunflower
    ),

    RaisedHands(
        R.string.note_emotion_icon_Raised_Hands,
        R.string.note_emotion_title_Raised_Hands,
        R.string.note_emotion_message_Raised_Hands
    )
}