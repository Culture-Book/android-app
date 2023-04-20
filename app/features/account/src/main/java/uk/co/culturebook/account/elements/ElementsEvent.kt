package uk.co.culturebook.account.elements

import java.util.UUID

sealed interface ElementsEvent {
    object FetchElements : ElementsEvent
    object FetchContributions : ElementsEvent
    object FetchCultures : ElementsEvent
    object Idle : ElementsEvent
    data class DeleteElement(val uuid: UUID) : ElementsEvent
    data class DeleteContribution(val uuid: UUID) : ElementsEvent
    data class DeleteCulture(val uuid: UUID) : ElementsEvent
    data class ConfirmDeleteElement(val uuid: UUID) : ElementsEvent
    data class ConfirmDeleteContribution(val uuid: UUID) : ElementsEvent
    data class ConfirmDeleteCulture(val uuid: UUID) : ElementsEvent
}