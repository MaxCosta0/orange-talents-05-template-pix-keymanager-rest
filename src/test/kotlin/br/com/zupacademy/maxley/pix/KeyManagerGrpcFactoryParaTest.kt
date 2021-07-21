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
import javax.inject.Singleton

@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
class KeyManagerGrpcFactoryParaTest(@GrpcChannel("keymanager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun consultaChave() = KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeyManagerListaGrpcSErviceGrpc.newBlockingStub(channel)
}