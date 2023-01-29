package uk.co.culturebook.add_new

import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.co.culturebook.add_new.data.InfoData
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.add_new.title_type.composables.TitleAndTypeRoute
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.nav.Route

class AddNewViewModel : ViewModel() {
    private val _culture = MutableStateFlow<Culture?>(null)
    private val _elementType = MutableStateFlow<ElementType?>(null)
    private val _title = MutableStateFlow<String?>(null)
    private val _infoData = MutableStateFlow<InfoData?>(null)

    fun registerCulture(culture: Culture) { _culture.value = culture }
    fun registerTitleAndType(elementType: ElementType, title: String) {
        _elementType.value = elementType
        _title.value = title
    }
    fun registerInfo(infoData: InfoData) { _infoData.value = infoData }

}