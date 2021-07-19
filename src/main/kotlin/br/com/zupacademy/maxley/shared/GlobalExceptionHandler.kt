package br.com.zupacademy.maxley.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<*> {
        val status = exception.status.code
        val description = exception.status.description
        val (httpStatus, message) = when (status) {
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description)
            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, "Dados invalidos")
            Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, description)
            else -> Pair(HttpStatus.INTERNAL_SERVER_ERROR,
                "Nao foi possivel completar a requisicao. Erro desconhecido")
        }

        return HttpResponse.status<JsonError>(httpStatus)
            .body(JsonError(message))
    }

}