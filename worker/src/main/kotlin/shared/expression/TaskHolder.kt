package shared.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import shared.matrix.MatrixImplListDto
import java.util.concurrent.CompletableFuture

@Serializable
class TaskHolder<E>(
    val operation: Operation,
    @Serializable
    var arg1: MatrixImplListDto<E>?,
    @Serializable
    var arg2: MatrixImplListDto<E>?,
    @Transient
    val arg1Future: CompletableFuture<MatrixImplListDto<E>>? = null,
    @Transient
    val arg2Future: CompletableFuture<MatrixImplListDto<E>>? = null,
    @Transient
    val resultTo : CompletableFuture<MatrixImplListDto<E>>? = null
)