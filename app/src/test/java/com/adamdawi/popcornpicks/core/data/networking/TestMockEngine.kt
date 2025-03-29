@file:OptIn(InternalAPI::class)

package com.adamdawi.popcornpicks.core.data.networking

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.util.InternalAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class TestMockEngine(
    override val dispatcher: CoroutineDispatcher,
    mockEngineConfig: MockEngineConfig,
    override val config: HttpClientEngineConfig
): HttpClientEngine {
    val mockEngine = MockEngine(mockEngineConfig)

    override val coroutineContext: CoroutineContext
        get() = mockEngine.coroutineContext + dispatcher

    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        return withContext(coroutineContext){
            mockEngine.execute(data)
        }
    }

    override fun close() {
        mockEngine.close()
    }
}