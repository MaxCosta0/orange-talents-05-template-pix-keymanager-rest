package br.com.zupacademy.maxley.pix.lista

import br.com.zupacademy.maxley.KeyManagerListaGrpcSErviceGrpc
import br.com.zupacademy.maxley.ListaChavePixRequest
import br.com.zupacademy.maxley.ListaChavePixResponse
import br.com.zupacademy.maxley.TipoDeConta
import br.com.zupacademy.maxley.pix.TipoContaBancaria
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Controller("/api/clientes")
class ListaChavesPixController(
    val listaChavePixClient: KeyManagerListaGrpcSErviceGrpc.KeyManagerListaGrpcSErviceBlockingStub
) {

    @Get("/{clientId}/pix")
    fun lista (
        @QueryValue clientId: String
    ): HttpResponse<Any> {

        val grpcResponse = listaChavePixClient.lista(
            ListaChavePixRequest.newBuilder()
                .setClientId(clientId)
                .build()
        )

        val restResponse: List<ChavePixRestResponse> = grpcResponse.chavesPixList.map { chavePixGrpc ->
            ChavePixRestResponse(
                pixId = chavePixGrpc.pixId,
                clientId = chavePixGrpc.clientId,
                valorDaChave = chavePixGrpc.chave,
                tipoDaConta = when (chavePixGrpc.tipoDeConta) {
                    TipoDeConta.CONTA_CORRENTE -> TipoContaBancaria.CONTA_CORRENTE
                    TipoDeConta.CONTA_POUPANCA -> TipoContaBancaria.CONTA_POUPANCA
                    else -> TipoContaBancaria.UNKNOWN_TIPO_CONTA
                },
                criadaEm = chavePixGrpc.criadaEm.let {
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(
                            it.seconds,
                            it.nanos.toLong()
                        ),
                        ZoneOffset.UTC
                    )
                }
            )
        }

        return HttpResponse.ok(restResponse)
    }
}