package br.com.zupacademy.maxley.shared.grpc

import br.com.zupacademy.maxley.KeyManagerConsultaGrpcServiceGrpc
import br.com.zupacademy.maxley.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.maxley.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keymanager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun consultaChave() = KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)
}