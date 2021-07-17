package br.com.zupacademy.maxley.pix.registra

import br.com.zupacademy.maxley.RegistraChavePixRequest
import br.com.zupacademy.maxley.TipoDeChave
import br.com.zupacademy.maxley.TipoDeConta
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class NovaChavePixRequest(
    @field:NotNull val tipoDeChave: TipoDeChave,
    @field:NotBlank @field:Size(max = 77) val valorDaChave: String,
    @field:NotNull val tipoDeConta: TipoDeConta
) {
    fun toRegistraChavePixRequest(clientId: UUID): RegistraChavePixRequest? {
        return RegistraChavePixRequest.newBuilder()
            .setClientId(clientId.toString())
            .setTipoDeChave(tipoDeChave)
            .setChave(valorDaChave)
            .setTipoDeConta(tipoDeConta)
            .build()
    }
}