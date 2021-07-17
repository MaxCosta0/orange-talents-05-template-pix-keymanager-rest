package br.com.zupacademy.maxley.shared.grpc

import br.com.zupacademy.maxley.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
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
}