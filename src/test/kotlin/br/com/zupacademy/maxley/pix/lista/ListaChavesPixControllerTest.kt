package br.com.zupacademy.maxley.pix.lista

import br.com.zupacademy.maxley.*
import br.com.zupacademy.maxley.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavesPixControllerTest() {

    @field:Inject
    lateinit var grpcClient: KeyManagerListaGrpcSErviceGrpc.KeyManagerListaGrpcSErviceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object{
        const val CLIENT_ID = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val CHAVE_PIX_RANDOM = UUID.randomUUID().toString()
        val PIX_ID = UUID.randomUUID().toString()
        const val GET_URI = "api/clientes/$CLIENT_ID/pix"
    }

    /*
    * 1 - Happy Path
    * 2 - Cliente nao possui chaves pix
    * */

    @Test
    fun `deve listar chaves pix de um cliente`() {
        //Cenario
        Mockito.`when`(grpcClient.lista(listaChavePixGrpcRequest()))
            .thenReturn(listaChavePixGrpcResponse())

        //Ação
        val response = httpClient.toBlocking().exchange(
            GET_URI,
            List::class.java
        )

        //Validação
        with(response){
            assertEquals(HttpStatus.OK.code, status.code)
            assertNotNull(body())
            assertEquals(1, body().size)
        }
    }

    @Test
    fun `deve retornar lista vazia quando cliente nao possui chaves`() {
        //Cenario
        Mockito.`when`(grpcClient.lista(listaChavePixGrpcRequest()))
            .thenReturn(ListaChavePixResponse.newBuilder().build())

        //Ação
        val response = httpClient.toBlocking().exchange(
            GET_URI,
            List::class.java
        )

        //Validação
        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            assertNotNull(body())
            assertEquals(0, body().size)
        }
    }

    private fun listaChavePixGrpcRequest(): ListaChavePixRequest? {
        return ListaChavePixRequest.newBuilder()
            .setClientId(CLIENT_ID)
            .build()
    }

    private fun listaChavePixGrpcResponse(): ListaChavePixResponse? {
        return ListaChavePixResponse.newBuilder().addChavesPix(
                    ListaChavePixResponse.ChavePixResponse.newBuilder()
                        .setPixId(PIX_ID)
                        .setClientId(CLIENT_ID)
                        .setTipoDeChave(TipoDeChave.ALEATORIA)
                        .setChave(CHAVE_PIX_RANDOM)
                        .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
                        .setCriadaEm(Timestamp.newBuilder().setNanos(12345).setSeconds(12345).build())
                        .build()
            ).build()
    }

//    private fun listaChavePixRestResponse(): List<ChavePixRestResponse> {
//        return listOf(
//            ChavePixRestResponse(
//                pixId = PIX_ID,
//                clientId = CLIENT_ID,
//                valorDaChave = CHAVE_PIX_RANDOM,
//                tipoDaConta = TipoContaBancaria.CONTA_CORRENTE,
//                criadaEm = LocalDateTime.now()
//        ))
//    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    class MockitoListaFactory{
        @Singleton
        fun listaChaves(): KeyManagerListaGrpcSErviceGrpc.KeyManagerListaGrpcSErviceBlockingStub {
            return Mockito.mock(KeyManagerListaGrpcSErviceGrpc.KeyManagerListaGrpcSErviceBlockingStub::class.java)
        }
    }
}