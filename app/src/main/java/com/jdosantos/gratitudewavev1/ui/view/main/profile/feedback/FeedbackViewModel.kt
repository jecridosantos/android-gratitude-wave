package com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.Feedback
import com.jdosantos.gratitudewavev1.domain.usecase.feedback.SaveFeedbackUseCase
import com.jdosantos.gratitudewavev1.domain.handles.LocalizedMessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val saveFeedbackUseCase: SaveFeedbackUseCase,
    private val localizedMessageManager: LocalizedMessageManager
) : ViewModel() {
    private val tag = this::class.java.simpleName
    var feedback by mutableStateOf(Feedback())
        private set

    private val _feedbackSuccess = MutableStateFlow(false)
    val feedbackSuccess: StateFlow<Boolean> = _feedbackSuccess

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun save(message: String) {
        if (message.isEmpty() && feedback.checks!!.isEmpty()) return
        val result = runCatching {
            saveFeedbackUseCase.execute(
                feedback,
                callback = { success ->
                    _feedbackSuccess.value = success
                    _toastMessage.value =
                        localizedMessageManager.getMessage(if (success) LocalizedMessageManager.MessageKey.SUCCESS_FEEDBACK else LocalizedMessageManager.MessageKey.ERROR_FEEDBACK)
                }
            )
        }

        result.onFailure { e ->
            Log.e(tag, "save - error: ${e.localizedMessage}")
            _toastMessage.value =
                localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.ERROR_FEEDBACK)
        }

        feedback = feedback.copy(checks = mutableListOf())
    }

    fun addCheck(value: String, checked: Boolean) {
        if (checked) {
            if (!feedback.checks!!.contains(value)) {
                feedback.checks!!.add(value)
            }
        } else {
            if (feedback.checks!!.contains(value)) {
                feedback.checks!!.remove(value)
            }
        }
        feedback = feedback.copy(checks = feedback.checks!!.toMutableList())
    }

}