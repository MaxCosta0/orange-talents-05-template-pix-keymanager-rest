package br.com.zupacademy.maxley.pix.registra

import br.com.zupacademy.maxley.KeyManagerServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/clientes")
class RegistraChavePixController(
 private val registraChavePixClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Post("/{clientId}/pix")
    fun registra(
        @QueryValue clientId: UUID,
        @Valid @Body request: NovaChavePixRequest
    ) : HttpResponse<Any>? {

        logger.info("Cadastrando nova chave \n$request")

        val response = registraChavePixClient.registra(request.toRegistraChavePixGrpcRequest(clientId))

        return HttpResponse.created(
            HttpResponse.uri("/api/clientes/$clientId/pix/${response.pixId}")
        )
    }
}