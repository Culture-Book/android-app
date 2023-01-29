package uk.co.culturebook.add_new.data

import uk.co.culturebook.data.models.cultural.EventType
import uk.co.culturebook.data.models.cultural.MediaFile

data class InfoData(val background: String, val files: List<MediaFile>, val eventType: EventType? = null)
