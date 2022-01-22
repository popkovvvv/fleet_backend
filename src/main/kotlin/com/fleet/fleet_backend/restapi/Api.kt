package com.fleet.fleet_backend.restapi

import com.fleet.fleet_backend.core.usecases.UseCase
import com.fleet.fleet_backend.core.usecases.UseCaseRequest
import com.fleet.fleet_backend.core.usecases.UseCaseResponse

abstract class ApiRequest<T : UseCaseRequest, D : Any> {

    protected abstract val usecase: UseCase<T, UseCaseResponse>

    protected abstract fun toUseCaseRequest(data: JSONApiData<D>): T

    abstract suspend fun execute(data: JSONApiData<D>): JSONApiResponse

    protected fun withRequestError(code: String, payload: Map<String, String> = emptyMap()): JSONApiResponse.Error =
        JSONApiResponse.Error(400, listOf(JSONApiError("400", code, payload)))

    protected fun withNotFoundError(code: String): JSONApiResponse.Error =
        JSONApiResponse.Error(404, listOf(JSONApiError("404", code)))

    protected fun withAccessError(): JSONApiResponse.Error =
        JSONApiResponse.Error(401, listOf(JSONApiError("401", "NOT_AUTHORIZED")))

    protected fun <T> withSuccess(type: String, id: String?, data: T):
            JSONApiResponse.Success<T> = JSONApiResponse.Success(listOf(JSONApiData(type, id, data)))

    protected fun <T> withSuccess(data: List<JSONApiData<T>>): JSONApiResponse.Success<T> =
        JSONApiResponse.Success(data)

    protected fun <T> withSuccess(code: Int, data: List<JSONApiData<T>>): JSONApiResponse.Success<T> =
        JSONApiResponse.Success(data).withCode(code)
}

abstract class JSONApiRequest<T : UseCaseRequest, D : Any> : ApiRequest<T, D>()

sealed class JSONApiResponse {

    abstract var httpCode: Int

    data class Success<T>(
        var data: List<JSONApiData<T>>? = null,
    ) : JSONApiResponse() {
        override var httpCode: Int = 200

        fun withCode(httpCode: Int): Success<T> = apply {
            this.httpCode = httpCode
        }
    }

    data class Error(private val code: Int, val errors: List<JSONApiError>) : JSONApiResponse() {
        override var httpCode: Int = code
    }
}

data class JSONApiData<out T>(
    val type: String,
    val id: String? = null,
    val attributes: T,
)

data class JSONApiError(val status: String, val code: String, val payload: Map<String, String> = emptyMap())
