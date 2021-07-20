package br.com.zupacademy.maxley.pix.consulta

import br.com.zupacademy.maxley.*
import br.com.zupacademy.maxley.pix.TipoContaBancaria
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller("/api/clientes")
class ConsultaChavePixController(
    val consultaChavePixClient: KeyManagerConsultaGrpcServiceGrpc
    .KeyManagerConsultaGrpcServiceBlockingStub
) {

    @Get("/{clientId}/pix/{pixId}")
    fun consulta(
        @QueryValue clientId: String,
        @QueryValue pixId: String
    ): HttpResponse<Any>{

        val consultaChavePixGrpcResponse = consultaChavePixClient.consulta(
            ConsultaChavePixRequest.newBuilder()
                .setPixId(
                    ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClientId(clientId)
                        .setPixId(pixId)
                        .build()
                )
                .build()
        )

        val response = consultaChavePixGrpcResponse.toConsultaChavePixRestResponse()

        return HttpResponse.ok(response)
    }
}

fun ConsultaChavePixResponse.toConsultaChavePixRestResponse(): ConsultaChavePixRestResponse {
    return ConsultaChavePixRestResponse(
        clienteId = this.clienteId,
        pixId = this.pixId,
        tipoChavePix = when (this.chave.tipo) {
            TipoDeChave.ALEATORIA -> TipoChavePix.ALEATORIA
            TipoDeChave.CPF -> TipoChavePix.CPF
            TipoDeChave.EMAIL -> TipoChavePix.EMAIL
            TipoDeChave.CELULAR -> TipoChavePix.CELULAR
            else -> TipoChavePix.UNKNOWN_TIPO_CHAVE
        },
        chave = this.chave.chave,
        titular = this.chave.conta.nomeDoTitular,
        cpfTitular = this.chave.conta.cpfDoTitular,
        instituicaoFinanceira = this.chave.conta.instituicao,
        agencia = this.chave.conta.agencia,
        numeroDaConta = this.chave.conta.numeroDaConta,
        tipoContaBancaria = when (this.chave.conta.tipo) {
            TipoDeConta.CONTA_CORRENTE -> TipoContaBancaria.CONTA_CORRENTE
            TipoDeConta.CONTA_POUPANCA -> TipoContaBancaria.CONTA_POUPANCA
            else -> TipoContaBancaria.UNKNOWN_TIPO_CONTA
        }
    )
}