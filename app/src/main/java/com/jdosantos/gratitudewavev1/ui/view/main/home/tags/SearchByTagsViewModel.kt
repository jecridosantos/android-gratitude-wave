package com.jdosantos.gratitudewavev1.ui.view.main.home.tags

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchByTagsViewModel @Inject constructor(
    getTagsUseCase: GetTagsUseCase
) :ViewModel() {
    private val tag = this::class.java.simpleName
    private val _tags = MutableStateFlow<List<NoteTag>>(emptyList())
    val tags: StateFlow<List<NoteTag>> = _tags

    init {
        getTagsUseCase.execute(callback =  { tags ->
            _tags.value = tags
        }, onError = {
            Log.e(tag, "init - getTagsUseCase")
        })
    }
}