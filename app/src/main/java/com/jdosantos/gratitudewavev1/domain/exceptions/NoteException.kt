package com.jdosantos.gratitudewavev1.domain.exceptions

sealed class NoteException(message: String) : Exception(message) {

    class SaveError : NoteException("No se pudo guardar la nota")
    class LimitForToday : NoteException("Alcanzaste el limite diario de notas publicadas.")
    class LimitGenerateByIAForToday : NoteException("Alcanzaste el limite diario de notas generadas por IA.")
    class LimitByIAForToday : NoteException("Alcanzaste el límite diario de notas publicadas por IA.")
    class GenerateError : NoteException("No se pudo generar la nota")

    data class WaitTime(val minutes: Long) : NoteException("Debes esperar $minutes minutos antes de generar otra nota.")

}