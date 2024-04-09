package com.jdosantos.gratitudewavev1.ui.view.main.home.tags

import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.usecase.GetTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchByTagsViewModel @Inject constructor(
    getTagsUseCase: GetTagsUseCase
) :ViewModel() {
    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags

    init {
        getTagsUseCase.execute() { tags ->
            _tags.value = tags
        }
    }
}