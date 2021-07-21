package br.com.zupacademy.maxley.pix.remove

import br.com.zupacademy.maxley.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.maxley.RemoveChavePixRequest
import br.com.zupacademy.maxley.RemoveChavePixResponse
import br.com.zupacademy.maxley.pix.KeyManagerGrpcFactoryParaTest
import br.com.zupacademy.maxley.shared.grpc.KeyManagerGrpcFactory
import io.grpc.Status
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var grpcClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object{
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()
        val chave = UUID.randomUUID().toString()
        val DELETE_URI = "api/clientes/$clientId/$pixId"
    }

    /*
     * 1 - Happy Path - ok
     * 2 - Chave Pix inexistente
     */

    @Test
    fun `deve remover chave pix`() {
        //Cenario
        Mockito.`when`(grpcClient.remove(removeChavePixRequest()))
            .thenReturn(removeChavePixResponse())

        //Ação
        val response = httpClient.toBlocking().exchange(
            HttpRequest.DELETE(DELETE_URI, null),
            Any::class.java
        )

        //Validação
        assertEquals(HttpStatus.OK.code, response.status.code)
    }

    @Test
    fun `nao deve remover chave pix quando chave inexistente`() {
        //Cenario
        Mockito.`when`(grpcClient.remove(removeChavePixRequest()))
            .thenThrow(Status.NOT_FOUND
                .withDescription("Chave pix nao encontrada")
                .asRuntimeException()
            )

        //Ação
        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(
                HttpRequest.DELETE(DELETE_URI, null),
                Any::class.java
            )
        }

        //Validação
        with(exception) {
            assertEquals(HttpStatus.NOT_FOUND.code, this.status.code)
            assertEquals("Chave pix nao encontrada", this.message)
        }
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactoryParaTest::class)
    class MockGrpcFactory(){
        @Singleton
        fun removeClient(): KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub {
            return Mockito.mock(KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class.java)
        }
    }

    fun removeChavePixRequest(): RemoveChavePixRequest = RemoveChavePixRequest.newBuilder()
        .setPixId(pixId)
        .setClienteId(clientId)
        .build()

    fun removeChavePixResponse(): RemoveChavePixResponse = RemoveChavePixResponse.newBuilder()
        .setChave(chave)
        .build()

}