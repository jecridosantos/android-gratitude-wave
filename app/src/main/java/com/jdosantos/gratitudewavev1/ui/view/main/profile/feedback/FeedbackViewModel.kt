package com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.Feedback
import com.jdosantos.gratitudewavev1.domain.usecase.feedback.SaveFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val saveFeedbackUseCase: SaveFeedbackUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    var feedback by mutableStateOf(Feedback())
        private set

    fun save(message: String, callback: (Boolean) -> Unit) {
        if (message.isEmpty() && feedback.checks!!.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                feedback = feedback.copy(message = message)
                saveFeedbackUseCase.execute(
                    feedback,
                    callback = { success ->
                        callback.invoke(success)
                        feedback = feedback.copy(checks = mutableListOf())
                    })
            } catch (e: Exception) {
                feedback = feedback.copy(checks = mutableListOf())
                Log.e(tag, "save - error: ${e.localizedMessage}")
                callback.invoke(false)
            }
        }
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