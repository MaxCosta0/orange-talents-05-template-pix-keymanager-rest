package br.com.zupacademy.maxley.shared.grpc

import br.com.zupacademy.maxley.KeyManagerConsultaGrpcServiceGrpc
import br.com.zupacademy.maxley.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.maxley.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.stub.AbstractBlockingStub
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory {

    @Singleton
    fun registraChave(
        @GrpcChannel("keymanager") channel: ManagedChannel
    ): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub? {
        return KeyManagerServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removeChave(
        @GrpcChannel("keymanager") channel: ManagedChannel
    ): KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub? {
        return KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun consultaChave(
        @GrpcChannel("keymanager") channel: ManagedChannel
    ): KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub? {
        return KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)
    }
}