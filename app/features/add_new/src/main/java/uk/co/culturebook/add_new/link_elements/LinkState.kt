package uk.co.culturebook.add_new.link_elements

import uk.co.culturebook.data.models.cultural.Element
import java.util.UUID

sealed interface LinkState {
    object Idle : LinkState
    object Loading : LinkState
    object Error : LinkState
    data class ElementsFetched(val elements: List<Element>) : LinkState
    data class ElementsLinked(val elements: List<Element>) : LinkState
}