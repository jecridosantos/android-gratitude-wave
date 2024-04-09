package com.jdosantos.gratitudewavev1.ui.view.main.goals

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.app.model.Goals
import com.jdosantos.gratitudewavev1.app.usecase.goals.GetGoalsByUser
import com.jdosantos.gratitudewavev1.app.usecase.goals.SaveGoalsByUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoalsByUser: GetGoalsByUser,
    private val saveGoalsByUser: SaveGoalsByUser
) : ViewModel() {
    var goals by mutableStateOf(Goals())
        private set

    fun getGoals() {
        getGoalsByUser.execute {
            goals = it
        }
    }

    fun saveChallenge(challenge: String, onSuccess: () -> Unit, onError: () -> Unit) {
        goals = goals.copy(challenge = challenge)
        if (challenge.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val goalsToSave: Goals = Goals().copy(
                        id = goals.id,
                        uid = goals.uid,
                        challenge = challenge,
                    )

                    saveGoalsByUser.execute(goalsToSave, {
                        Log.d("SUCCESS SAVE", "Se guardo exitosamente")
                        onSuccess()
                    }) { error ->
                        Log.d("ERROR SAVE", error)
                    }


                } catch (e: Exception) {
                    Log.d("ERROR SAVE", "Error at save new note ${e.localizedMessage}")
                }
            }
        } else {
            onError()
        }
    }

}