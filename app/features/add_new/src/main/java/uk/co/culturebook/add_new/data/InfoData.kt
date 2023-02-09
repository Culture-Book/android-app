package uk.co.culturebook.add_new.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.co.culturebook.data.models.cultural.Empty
import uk.co.culturebook.data.models.cultural.EventType
import uk.co.culturebook.data.models.cultural.MediaFile
import java.util.*

@Stable
class InfoData {
    var elementType by mutableStateOf("")
    var background by mutableStateOf("")
    var linkedElements by mutableStateOf<List<UUID>>(emptyList())
    var files by mutableStateOf<List<MediaFile>>(emptyList())
    var eventType by mutableStateOf(EventType.Empty)
}
