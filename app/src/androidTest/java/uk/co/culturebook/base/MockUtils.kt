package uk.co.culturebook.base

import java.lang.reflect.Proxy
import kotlin.reflect.KFunction

data class MockWith(val method: String, val result: Any?)

infix fun <T> KFunction<T>.returns(result: Any) = MockWith(name, result)

inline fun <reified T : Any> mock(vararg mockedMethods: MockWith): T =
    Proxy.newProxyInstance(
        T::class.java.classLoader,
        arrayOf(T::class.java)
    ) { _, method, args ->
        val mockedMethod = mockedMethods.find { it.method == method.name }

        when {
            mockedMethod != null -> mockedMethod.result
            else -> method.invoke(
                T::class.java
                    .getDeclaredConstructor()
                    .newInstance(T::class.java.getDeclaredConstructor().parameters),
                *args
            )
        }
    } as T