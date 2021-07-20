package br.com.zupacademy.maxley.pix.remove

import br.com.zupacademy.maxley.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.maxley.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.QueryValue

@Controller("/api/clientes")
class RemoveChavePixController(
    private val removeChavePixGrpcClient: KeyManagerRemoveGrpcServiceGrpc
    .KeyManagerRemoveGrpcServiceBlockingStub
) {

    @Delete("/{clientId}/{pixId}")
    fun remove(
        @QueryValue clientId: String,
        @QueryValue pixId: String
    ): HttpResponse<Any> {

        removeChavePixGrpcClient.remove(
            RemoveChavePixRequest.newBuilder()
                .setClienteId(clientId)
                .setPixId(pixId)
                .build()
        )

        return HttpResponse.ok()
    }
}