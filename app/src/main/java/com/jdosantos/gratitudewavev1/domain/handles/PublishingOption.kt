package com.jdosantos.gratitudewavev1.domain.handles

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jdosantos.gratitudewavev1.R


sealed class PublishingOption(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    data object PublishingOptionPrivate : PublishingOption(
        R.drawable.icon_publish_option_private,
        R.string.publishing_option_private_title,
        R.string.publishing_option_private_description
    )

    data object PublishingOptionPublic : PublishingOption(
        R.drawable.icon_publish_option_public,
        R.string.publishing_option_public_title,
        R.string.publishing_option_public_description
    )

    data object PublishingOptionAnonymous : PublishingOption(
        R.drawable.icon_publish_option_anonymous,
        R.string.publishing_option_anonymous_title,
        R.string.publishing_option_anonymous_description
    )

    data object PublishingOptionSend : PublishingOption(
        R.drawable.icon_publish_option_send,
        R.string.publishing_option_send_title,
        R.string.publishing_option_send_description
    )
}
