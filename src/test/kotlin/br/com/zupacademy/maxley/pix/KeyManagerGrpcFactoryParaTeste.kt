package br.com.zupacademy.maxley.pix

import br.com.zupacademy.maxley.KeyManagerConsultaGrpcServiceGrpc
import br.com.zupacademy.maxley.KeyManagerListaGrpcSErviceGrpc
import br.com.zupacademy.maxley.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.maxley.KeyManagerServiceGrpc
import br.com.zupacademy.maxley.shared.grpc.KeyManagerGrpcFactory
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.grpc.annotation.GrpcChannel
import org.mockito.Mockito
import javax.inject.Singleton

@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
class KeyManagerGrpcFactoryParaTeste(@GrpcChannel("localhost:50051") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = Mockito.mock(KeyManagerServiceGrpc.newBlockingStub(channel)::class.java)

    @Singleton
    fun removeChave() = Mockito.mock(KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)::class.java)

    @Singleton
    fun consultaChave() = Mockito.mock(KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)::class.java)

    @Singleton
    fun listaChaves() = Mockito.mock(KeyManagerListaGrpcSErviceGrpc.newBlockingStub(channel)::class.java)
}