package com.jdosantos.gratitudewavev1.domain.usecase.notes

import androidx.paging.PagingData
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesByUserPagingUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(): Flow<PagingData<Note>> = noteRepository.getNotesPaging()

}