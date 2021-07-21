package br.com.zupacademy.maxley.pix.lista

import br.com.zupacademy.maxley.pix.TipoContaBancaria
import com.google.protobuf.Timestamp
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class ChavePixRestResponse(
    val pixId: String,
    val clientId: String,
    val valorDaChave: String,
    val tipoDaConta: TipoContaBancaria,
    val criadaEm: LocalDateTime
)