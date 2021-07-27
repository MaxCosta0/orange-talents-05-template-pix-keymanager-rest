package br.com.zupacademy.maxley.pix.registra

import br.com.zupacademy.maxley.*
import br.com.zupacademy.maxley.pix.TipoChavePix
import br.com.zupacademy.maxley.pix.TipoContaBancaria
import io.grpc.Status
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject

@MicronautTest
internal class RegistraChavePixControllerTest(
    private val grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub,
) {

//    @field:Inject
//    lateinit var grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object {
        const val CLIENT_ID_CONHECIDO = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val PIX_ID = UUID.randomUUID().toString()
        const val POST_URI  = "/api/clientes/$CLIENT_ID_CONHECIDO/pix"
    }

    @Test
    fun `deve cadastrar nova chave pix`() {
        //Cenario
        Mockito.`when`(grpcClient.registra(chavePixGrpcRequest()))
            .thenReturn(chavePixGrpcResponse())

        //Ação
        val response = httpClient
            .toBlocking()
            .exchange(
                HttpRequest.POST(POST_URI, novaChavePixRequest()),
                NovaChavePixRequest::class.java
            )

        //Validação
            with(response){
                assertEquals(HttpStatus.CREATED.code, this.status.code)
                assertTrue(this.headers.contains("location"))
                assertTrue(this.header("location")!!.contains(PIX_ID))
            }
    }

    @Test
    fun `nao deve cadastrar chave pix quando ja estiver cadastrada`() {
        //Cenario
        val chavePixExistente = novaChavePixRequest()

        Mockito.`when`(grpcClient.registra(chavePixGrpcRequest()))
            .thenThrow(Status.ALREADY_EXISTS
                .withDescription("Chave pix '${chavePixExistente.valorDaChave}' ja existe")
                .asRuntimeException())

        //Ação
        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(
                HttpRequest.POST(POST_URI, chavePixExistente),
                NovaChavePixRequest::class.java
            )
        }

        //Validação
        with(exception) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.code, this.status.code)
            assertNotNull(this.status)
            assertEquals("Chave pix '${chavePixExistente.valorDaChave}' ja existe", this.message)
        }
    }

    @Test
    fun `nao deve cadastrar chave pix quando argumentos invalidos localmente`() {
        //Cenario
        val chaveInvalida = NovaChavePixRequest(
            null,
            "",
            null
        )

        //Ação
        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(
                HttpRequest.POST(POST_URI, chaveInvalida),
                NovaChavePixRequest::class.java
            )
        }

        //Validação
        with(exception){
            assertEquals(HttpStatus.BAD_REQUEST.code, this.status.code)
        }
    }

//    @Test
//    fun `nao deve cadastrar chave pix quando argumentos invalidos no pix grpc`() {
//        //Cenario
//        Mockito.`when`(grpcClient.registra(chavePixGrpcRequest()))
//            .thenThrow(Status.INVALID_ARGUMENT
//                .withDescription("Dados invalidos")
//                .asRuntimeException())
//
//        //Ação
//        val exception = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking()
//                .exchange(
//                    HttpRequest.POST(POST_URI, novaChavePixRequest()),
//                    NovaChavePixRequest::class.java
//                )
//        }
//
//        //Validação
//        with(exception) {
//            assertEquals(HttpStatus.BAD_REQUEST.code, this.status.code)
//            assertEquals("Dados invalidos", this.message)
//        }
//    }


    fun novaChavePixRequest(chave: String = "maxleyCosta@mail.com") =
        NovaChavePixRequest(
            tipoChavePix = TipoChavePix.EMAIL,
            valorDaChave = chave,
            tipoContaBancaria = TipoContaBancaria.CONTA_CORRENTE
        )

    fun chavePixGrpcRequest() = with(novaChavePixRequest()){
                RegistraChavePixRequest.newBuilder()
                    .setClientId(CLIENT_ID_CONHECIDO)
                    .setTipoDeChave(TipoDeChave.valueOf(tipoChavePix!!.name))
                    .setChave(valorDaChave)
                    .setTipoDeConta(TipoDeConta.valueOf(tipoContaBancaria!!.name))
                    .build()
    }

    fun chavePixGrpcResponse(): RegistraChavePixResponse = RegistraChavePixResponse.newBuilder()
        .setPixId(PIX_ID)
        .setClienteId(CLIENT_ID_CONHECIDO)
        .build()

}