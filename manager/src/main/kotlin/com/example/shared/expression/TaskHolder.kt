package shared.expression

import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import shared.matrix.MatrixImplListDto

@Serializable
class TaskHolder<E>(
    val operation: Operation,
    @Serializable
    var arg1: MatrixImplListDto<E>?,
    @Serializable
    var arg2: MatrixImplListDto<E>?,
    @Transient
    val arg1Future: CompletableDeferred<MatrixImplListDto<E>>? = null,
    @Transient
    val arg2Future: CompletableDeferred<MatrixImplListDto<E>>? = null,
    @Transient
    val resultTo : CompletableDeferred<MatrixImplListDto<E>>? = null
)