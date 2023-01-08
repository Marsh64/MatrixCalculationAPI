package shared.expression

import kotlinx.serialization.Serializable

// to store expressions in ExpressionHolder
@Serializable
data class ExpressionDto(
    val operation: Operation,
    val leftExpr: ExpressionDto? = null,
    val rightExpr: ExpressionDto? = null,
    // instead of matrices use their indexes in argsList (list with all matrices used in expression) in ExpressionHolder
    val leftTableIndex: Int? = null,
    val rightTableIndex: Int? = null
)