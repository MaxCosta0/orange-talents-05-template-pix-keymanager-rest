package br.com.zupacademy.maxley.pix.consulta

import br.com.zupacademy.maxley.*
import io.grpc.Status
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject

@MicronautTest
internal class ConsultaChavePixControllerTest(
    private val grpcClient: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub,
) {

//    @field:Inject
//    lateinit var grpcClient: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object{
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()
        val chaveAleatoria = UUID.randomUUID().toString()
        val GET_URI = "api/clientes/$clientId/pix/$pixId"
    }

    /*
    * 1 - Happy Path
    * 2 - Chave nao existente
    * */

    @Test
    fun `deve consultar chave pix`() {
        //Cenario
        Mockito.`when`(grpcClient.consulta(consultaChavePixRequest()))
            .thenReturn(consultaChavePixRestResponse())

        //Ação
        val response = httpClient.toBlocking().exchange(
            GET_URI,
            ConsultaChavePixRestResponse::class.java
        )

        //Validação
        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            assertNotNull(body())
            assertEquals(pixId, body()!!.pixId)
            assertEquals(clientId, body()!!.clienteId)
            assertEquals(chaveAleatoria, body()!!.chave)
        }
    }

    @Test
    fun `nao deve consultar chave quando inexistente`() {
        //Cenario
        Mockito.`when`(grpcClient.consulta(Mockito.any()))
            .thenThrow(Status.NOT_FOUND
                .withDescription("Chave Pix nao encontrada")
                .asRuntimeException()
            )

        //Ação
        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(
                GET_URI,
                ConsultaChavePixRestResponse::class.java
            )
        }

        //Validação
        with(exception) {
            assertEquals(HttpStatus.NOT_FOUND.code, status.code)
            assertEquals("Chave Pix nao encontrada", message)
        }
    }

//    @Factory
//    @Replaces(factory = KeyManagerGrpcFactory::class)
//    class MockitoConsultaFactory {
//        @Singleton
//        fun consultaClient(): KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub? {
//            return Mockito.mock(
//                KeyManagerConsultaGrpcServiceGrpc.
//                KeyManagerConsultaGrpcServiceBlockingStub::class.java
//            )
//        }
//    }

    fun consultaChavePixRequest() = ConsultaChavePixRequest.newBuilder()
        .setPixId(ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
            .setPixId(pixId)
            .setClientId(clientId)
            .build()
        )
        .build()

    fun consultaChavePixRestResponse(): ConsultaChavePixResponse? {
        return ConsultaChavePixResponse.newBuilder()
            .setClienteId(clientId)
            .setPixId(pixId)
            .setChave(ConsultaChavePixResponse.ChavePix.newBuilder()
                .setTipo(TipoDeChave.ALEATORIA)
                .setChave(chaveAleatoria)
                .setConta(ConsultaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(TipoDeConta.CONTA_CORRENTE)
                    .setInstituicao("ITAU UNIBANCO")
                    .setNomeDoTitular("Rafal Ponte")
                    .setCpfDoTitular("02467781054")
                    .setAgencia("0001")
                    .setNumeroDaConta("291900")
                    .build()
                )
                .build()
            )
            .build()
    }
}