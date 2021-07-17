package br.com.zupacademy.maxley.pix.registra

import br.com.zupacademy.maxley.KeyManagerServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/api/clientes")
class RegistraChavePixController(
 private val registraChavePixClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {
//    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Post("/{clientId}/pix")
    fun registra(
        @QueryValue clientId: UUID,
        @Body request: NovaChavePixRequest
    ) : HttpResponse<Any> {

        val response = registraChavePixClient.registra(request.toRegistraChavePixRequest(clientId))

        return HttpResponse.ok(request)
    }
}