package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.PreferenceManager
import com.jdosantos.gratitudewavev1.domain.handles.LocalizedMessageManager
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.Step
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.models.UserPreferences
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.userPreferences.GetUserPreferencesUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.userPreferences.SaveUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val saveUserPreferencesUseCase: SaveUserPreferencesUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val preferenceManager: PreferenceManager,
    private val localizedStepManager: LocalizedStepManager,
    private val localizedMessageManager: LocalizedMessageManager,
    private val localizedInterestsManager: LocalizedInterestsManager,
    private val localizedProfessionsManager: LocalizedProfessionsManager,
    private val localizedRelationshipsManager: LocalizedRelationshipsManager
) : ViewModel() {

    private val _steps = MutableStateFlow<List<Step>>(emptyList())
    val steps: StateFlow<List<Step>> = _steps

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _totalSteps = MutableStateFlow(0)

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _stepVisibilities = MutableStateFlow(List(6) { false })
    val stepVisibilities: StateFlow<List<Boolean>> = _stepVisibilities

    private val _interestsList = MutableStateFlow<List<String>>(emptyList())
    val interestsList: StateFlow<List<String>> = _interestsList

    private val _professionsList = MutableStateFlow<List<String>>(emptyList())
    val professionsList: StateFlow<List<String>> = _professionsList

    private val _professionOtherWord = MutableStateFlow("otros")
    val professionOtherWord: StateFlow<String> = _professionOtherWord

    private val _relationshipsList = MutableStateFlow<List<String>>(emptyList())
    val relationshipsList: StateFlow<List<String>> = _relationshipsList

    var preferences by mutableStateOf(UserPreferences())
        private set

    private val user = MutableStateFlow(User())

    init {
        loadSteps()
        _interestsList.value = localizedInterestsManager.getInterests()
        _professionsList.value = localizedProfessionsManager.getProfessions()
       // _professionOtherWord.value = _professionsList.value.last()
        _relationshipsList.value = localizedRelationshipsManager.getRelationships()
    }

    private fun calculateProgress() {
        _progress.value = if (_totalSteps.value == 0) 0f else (_currentStep.value + 1).toFloat() / (_totalSteps.value).toFloat()
    }

    private fun updateStepVisibility() {
        val updatedStepVisibilities = List(_stepVisibilities.value.size) { index ->
            index == _currentStep.value
        }
        _stepVisibilities.value = updatedStepVisibilities
    }

    fun stepBack() {
        _currentStep.value--
        calculateProgress()
        updateStepVisibility()
    }

    fun stepNext() {
        _currentStep.value++
        calculateProgress()
        updateStepVisibility()
    }

    private fun loadSteps() {
        val currentSteps = localizedStepManager.getSteps()
        _steps.value = currentSteps
        _totalSteps.value = _steps.value.size
        _currentStep.value = 0
        calculateProgress()
        updateStepVisibility()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase.execute().onSuccess { value: User ->
                user.value = value
                preferences = preferences.copy(userId = user.value.id)
                onName(user.value.name)
            }
        }
    }

    fun saveUserPreferences() {
        viewModelScope.launch {
            saveUserPreferencesUseCase.invoke(preferences).onSuccess {
                preferences = it

            }.onFailure {
                _toastMessage.value =
                    localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.GENERIC_ERROR)
            }
        }

    }

    fun getUserPreferences() {
        viewModelScope.launch {
            getUserPreferencesUseCase.invoke().onSuccess {
                preferences = it
            }
        }
    }


    fun onAge(age: String) {
        preferences = preferences.copy(age = age.replace(" ", ""))
    }

    fun onCountry(country: String) {
        preferences = preferences.copy(country = country)
    }

    fun onInterests(interests: String) {
        preferences = preferences.copy(interests = interests)
    }

    fun onInterestsList(selectedInterests: List<String>) {
        preferences = preferences.copy(selectedInterests = selectedInterests)
    }

    fun onRelationshipsList(selectedRelationships: List<String>) {
        preferences = preferences.copy(selectedRelationships = selectedRelationships)
    }

    fun onProfession(profession: String) {
        preferences = preferences.copy(profession = profession)
    }

    fun onRelationship(relationship: String) {
        preferences = preferences.copy(relationship = relationship)
    }

    fun onAbout(about: String) {
        preferences = preferences.copy(about = about)
    }

    fun onSkip() {
        preferenceManager.setShowOnboarding(false)
    }

    fun onName(name: String) {
        preferences = preferences.copy(name = name)

    }

    fun resetCurrentStep() {
        _currentStep.value = 0
        calculateProgress()
        updateStepVisibility()
    }
}