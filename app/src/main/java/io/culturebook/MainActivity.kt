package io.culturebook

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.culturebook.data.SharedPrefs
import io.culturebook.data.models.authentication.User
import io.culturebook.data.remote.interfaces.ApiInterface
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.remote.retrofit.getAuthenticatedRetrofitClient
import io.culturebook.data.remote.retrofit.getAuthenticationRetrofitClient
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.data.sharedPreferences
import io.culturebook.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val authInterface = getAuthenticationRetrofitClient()
    private lateinit var authedInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        authedInterface = getAuthenticatedRetrofitClient(this)
        val userRepository = UserRepository(authInterface, authedInterface)
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(userRepository)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(userRepository: UserRepository) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = email.value, onValueChange = { email.value = it })
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = password.value, onValueChange = { password.value = it })
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    when (val reg = userRepository.register(
                        User(
                            email = email.value,
                            password = password.value
                        )
                    )) {
                        is ApiResponse.Success -> {
                            context.sharedPreferences.edit()
                                .putString(SharedPrefs.UserSession.key, reg.data.jwt).apply()
                        }
                        else -> TODO()
                    }
                }
            }) {
            Text(text = "Register")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    when (val reg = userRepository.login(
                        User(
                            email = email.value,
                            password = password.value
                        )
                    )) {
                        is ApiResponse.Success -> {
                            context.sharedPreferences.edit()
                                .putString(SharedPrefs.UserSession.key, reg.data.jwt).apply()
                        }
                        else -> {}
                    }
                }
            }) {
            Text(text = "Login")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    when (val reg = userRepository.getUser()) {
                        is ApiResponse.Success -> {
                            Toast.makeText(context, reg.data.email, Toast.LENGTH_LONG).show()
                        }
                        else -> {}
                    }
                }
            }) {
            Text(text = "Get User")
        }
    }

}