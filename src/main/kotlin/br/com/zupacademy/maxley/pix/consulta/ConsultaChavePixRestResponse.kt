package br.com.zupacademy.maxley.pix.consulta

import br.com.zupacademy.maxley.pix.TipoChavePix
import br.com.zupacademy.maxley.pix.TipoContaBancaria

data class ConsultaChavePixRestResponse(
    val clienteId: String?,
    val pixId: String?,
    val tipoChavePix: TipoChavePix?,
    val chave: String?,
    val titular: String?,
    val cpfTitular: String?,
    val instituicaoFinanceira: String?,
    val agencia: String?,
    val numeroDaConta: String?,
    val tipoContaBancaria: TipoContaBancaria?
) {

}
