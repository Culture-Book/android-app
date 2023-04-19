package uk.co.culturebook.data.remote.interfaces

import java.lang.reflect.Constructor

abstract class HttpClient {
    fun <T: HttpClient> createService(service: Class<T>): T {
        val constructor: Constructor<T> = service.getDeclaredConstructor()
        return constructor.newInstance(constructor.parameters)
    }
}