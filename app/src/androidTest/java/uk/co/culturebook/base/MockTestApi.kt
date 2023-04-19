package uk.co.culturebook.base

import uk.co.culturebook.data.remote.interfaces.HttpClient
import java.lang.reflect.Constructor

class MockTestApi : HttpClient {
    override fun <T : HttpClient> createService(service: Class<T>): T {
        val constructor: Constructor<T> = service.getDeclaredConstructor()
        return constructor.newInstance(constructor.parameters)
    }

}