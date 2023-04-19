package uk.co.culturebook.base

import uk.co.culturebook.data.models.authentication.PasswordReset
import uk.co.culturebook.data.models.authentication.PasswordResetRequest
import uk.co.culturebook.data.models.authentication.PublicJWT
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.UserSession
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.data.remote.interfaces.HttpClient

class MockTestAuth : HttpClient {
    override fun <T : HttpClient> createService(service: Class<T>): T {
        TODO("Not yet implemented")
    }

}