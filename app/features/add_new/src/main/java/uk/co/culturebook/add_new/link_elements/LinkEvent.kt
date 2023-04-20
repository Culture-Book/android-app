package uk.co.culturebook.add_new.link_elements

import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import java.util.UUID

sealed interface LinkEvent {
    data class FavouriteElement(val uuid: UUID?) : LinkEvent
    data class BlockElement(val uuid: UUID?) : LinkEvent
    data class FetchElements(val searchCriteria: SearchCriteriaState) : LinkEvent
    object LinkElements : LinkEvent
    data class LinkElement(val element: Element) : LinkEvent
}