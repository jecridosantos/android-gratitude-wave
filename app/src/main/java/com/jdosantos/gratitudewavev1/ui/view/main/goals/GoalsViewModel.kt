package com.jdosantos.gratitudewavev1.ui.view.main.goals

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.Goals
import com.jdosantos.gratitudewavev1.domain.usecase.goals.GetGoalsByUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.goals.SaveGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoalsByUserUseCase: GetGoalsByUserUseCase,
    private val saveGoalsUseCase: SaveGoalsUseCase
) : ViewModel() {

    private val tag = this::class.java.simpleName
    var goals by mutableStateOf(Goals())
        private set

    fun getGoals() {
        getGoalsByUserUseCase.execute(
            callback = { goals = it },
            onError = {
                Log.e(tag, "getGoals - getGoalsByUserUseCase")
            }
        )
    }

    fun saveChallenge(challenge: String, callback: (Boolean) -> Unit) {
        goals = goals.copy(challenge = challenge)
        if (challenge.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val goalsToSave: Goals = Goals().copy(
                        id = goals.id,
                        uid = goals.uid,
                        challenge = challenge,
                    )

                    saveGoalsUseCase.execute(goalsToSave, callback)


                } catch (e: Exception) {
                    Log.e(tag, "saveChallenge - saveGoalsUseCase - error ${e.localizedMessage}")
                }
            }
        }
    }

}