package shared.expression

import kotlinx.serialization.Serializable
import shared.matrix.MatrixImplListDto

// will be serialized and sent to manager
@Serializable
data class ExpressionHolder<E>(
    // list of all (regular) tables used in expression
    val argsList: List<MatrixImplListDto<E>>,
    @Serializable
    val rootExpr: ExpressionDto
)