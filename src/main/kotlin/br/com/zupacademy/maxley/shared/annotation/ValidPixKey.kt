package br.com.zupacademy.maxley.shared.annotation

import br.com.zupacademy.maxley.pix.registra.NovaChavePixRequest
import javax.validation.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "chave pix invalida",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, NovaChavePixRequest> {
    override fun isValid(value: NovaChavePixRequest?, context: ConstraintValidatorContext?): Boolean {

        if (value?.tipoChavePix == null) {
            return true
        }

        return value.tipoChavePix.valida(value.valorDaChave)
    }

}
