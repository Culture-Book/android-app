package uk.co.culturebook.add_new.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.co.culturebook.data.models.cultural.ElementType
import java.util.*

@Stable
class TypeData {
    var parentElement by mutableStateOf<UUID?>(null)
    var name by mutableStateOf("")
    var type by mutableStateOf<ElementType?>(null)
}