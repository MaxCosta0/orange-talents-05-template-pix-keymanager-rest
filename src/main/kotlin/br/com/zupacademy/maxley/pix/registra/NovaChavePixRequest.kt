package br.com.zupacademy.maxley.pix.registra

import br.com.zupacademy.maxley.RegistraChavePixRequest
import br.com.zupacademy.maxley.TipoDeChave
import br.com.zupacademy.maxley.TipoDeConta
import br.com.zupacademy.maxley.pix.TipoChavePix
import br.com.zupacademy.maxley.pix.TipoContaBancaria
import br.com.zupacademy.maxley.shared.annotation.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class NovaChavePixRequest(
    @field:NotNull val tipoChavePix: TipoChavePix?,
    @field:Size(max = 77) val valorDaChave: String?,
    @field:NotNull val tipoContaBancaria: TipoContaBancaria?
) {
    fun toRegistraChavePixGrpcRequest(clientId: UUID): RegistraChavePixRequest? {
        return RegistraChavePixRequest.newBuilder()
            .setClientId(clientId.toString())
            .setTipoDeChave(TipoDeChave.valueOf(tipoChavePix!!.name))
            .setChave(valorDaChave ?: "")
            .setTipoDeConta(TipoDeConta.valueOf(tipoContaBancaria!!.name))
            .build()
    }
}