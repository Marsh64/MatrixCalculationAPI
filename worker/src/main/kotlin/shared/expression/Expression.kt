package shared.expression

import shared.matrix.MatrixImplListDto

class Expression<T> private constructor() {

    // type T describe type elements of matrices

    var operation: Operation = Operation.NONE
    var opType: OperationType = OperationType.NONE
    // if (left|right) arg is table then (left|right)Table is not null
    // if (left|right) arg is expression then (left|right)Table is not null
    var leftExpr: Expression<T>? = null
    var rightExpr: Expression<T>? = null
    var leftMatrix: MatrixImplListDto<T>? = null
    var rightMatrix: MatrixImplListDto<T>? = null

    // define opType
    private fun defineOpType(op: Operation) : OperationType {
        return when (op) {
            Operation.NONE -> OperationType.NONE
            Operation.PLUS -> OperationType.binary
            Operation.MINUS -> OperationType.binary
            Operation.TRANSPOSE -> OperationType.unary
            Operation.INVERSE -> OperationType.unary
            Operation.TIMES -> OperationType.binary
        }
    }

    // the only public constructor
    constructor(init: Expression<T>.() -> Expression<T>) : this() {
        val expr = init()
        this.operation = expr.operation
        this.opType = expr.opType
        this.leftExpr = expr.leftExpr
        this.rightExpr = expr.rightExpr
        this.leftMatrix = expr.leftMatrix
        this.rightMatrix = expr.rightMatrix
    }

    private constructor(operation: Operation, leftExpr: Expression<T>, rightExpr: Expression<T>) : this() {
        this.operation = operation
        this.opType = defineOpType(this.operation)
        if (opType == OperationType.unary){
            throw IllegalArgumentException("Expression: incorrect type operation, need binary")
        }
        this.leftExpr = leftExpr
        this.rightExpr = rightExpr
    }

    private constructor(operation: Operation, leftMatrix: MatrixImplListDto<T>, rightMatrix: MatrixImplListDto<T>) : this() {
        this.operation = operation
        this.opType = defineOpType(this.operation)
        if (opType == OperationType.unary){
            throw IllegalArgumentException("Expression: incorrect type operation, need binary")
        }
        this.leftMatrix = leftMatrix
        this.rightMatrix = rightMatrix
    }

    private constructor(operation: Operation, leftExpr: Expression<T>, rightMatrix: MatrixImplListDto<T>) : this() {
        this.operation = operation
        this.opType = defineOpType(this.operation)
        if (opType == OperationType.unary){
            throw IllegalArgumentException("Expression: incorrect type operation, need binary")
        }
        this.leftExpr = leftExpr
        this.rightMatrix = rightMatrix
    }

    private constructor(operation: Operation, leftMatrix: MatrixImplListDto<T>, rightExpr: Expression<T>) : this() {
        this.operation = operation
        this.opType = defineOpType(this.operation)
        if (opType == OperationType.unary){
            throw IllegalArgumentException("Expression: incorrect type operation, need binary")
        }
        this.leftMatrix = leftMatrix
        this.rightExpr = rightExpr
    }

    private constructor(operation: Operation, matrix: MatrixImplListDto<T>) : this() {
        this.operation = operation
        this.opType = defineOpType(this.operation)
        if (opType == OperationType.binary){
            throw IllegalArgumentException("Expression: incorrect type operation, need unary")
        }
        this.leftMatrix = matrix
    }

    private constructor(operation: Operation, expr: Expression<T>) : this() {
        this.operation = operation
        this.opType = defineOpType(this.operation)
        if (opType == OperationType.binary){
            throw IllegalArgumentException("Expression: incorrect type operation, need unary")
        }
        this.leftExpr = expr
    }

    /*
    table operation table
    expression operation table
    table operation expression
    expression operation expression

    operation(unary) matrix
    expression(unary) matrix
     */

    operator fun MatrixImplListDto<T>.plus(matrix: MatrixImplListDto<T>) = Expression(Operation.PLUS, this@plus, matrix)
    operator fun plus(matrix: MatrixImplListDto<T>) = Expression(Operation.PLUS, this, matrix)
    operator fun MatrixImplListDto<T>.plus(expr: Expression<T>) = Expression(Operation.PLUS, this@plus, expr)
    operator fun plus(expr: Expression<T>) = Expression(Operation.PLUS, this, expr)

    operator fun MatrixImplListDto<T>.minus(matrix: MatrixImplListDto<T>) = Expression(Operation.MINUS, this@minus, matrix)
    operator fun minus(matrix: MatrixImplListDto<T>) = Expression(Operation.MINUS, this, matrix)
    operator fun MatrixImplListDto<T>.minus(expr: Expression<T>) = Expression(Operation.MINUS, this@minus, expr)
    operator fun minus(expr: Expression<T>) = Expression(Operation.MINUS, this, expr)

    infix fun transpose(matrix: MatrixImplListDto<T>) = Expression(Operation.TRANSPOSE, matrix)
    infix fun transpose(expr: Expression<T>) = Expression(Operation.TRANSPOSE, expr)

    infix fun inverse(matrix: MatrixImplListDto<T>) = Expression(Operation.INVERSE, matrix)
    infix fun inverse(expr: Expression<T>) = Expression(Operation.INVERSE, expr)

    operator fun MatrixImplListDto<T>.times(matrix: MatrixImplListDto<T>) = Expression(Operation.TIMES, this@times, matrix)
    operator fun times(matrix: MatrixImplListDto<T>) = Expression(Operation.TIMES, this, matrix)
    operator fun MatrixImplListDto<T>.times(expr: Expression<T>) = Expression(Operation.TIMES, this@times, expr)
    operator fun times(expr: Expression<T>) = Expression(Operation.TIMES, this, expr)


//    infix fun Table.union(table: Table) = Expression(Operation.UNION, this@union, table)
//    infix fun union(table: Table) = Expression(Operation.UNION, this, table)
//    infix fun Table.union(expr: Expression) = Expression(Operation.UNION, this@union, expr)
//    infix fun union(expr: Expression) = Expression(Operation.UNION, this, expr)
//
//    infix fun Table.difference(table: Table) = Expression(Operation.DIFFERENCE, this@difference, table)
//    infix fun difference(table: Table) = Expression(Operation.DIFFERENCE, this, table)
//    infix fun Table.difference(expr: Expression) = Expression(Operation.DIFFERENCE, this@difference, expr)
//    infix fun difference(expr: Expression) = Expression(Operation.DIFFERENCE, this, expr)
//
//    infix fun Table.product(table: Table) = Expression(Operation.PRODUCT, this@product, table)
//    infix fun product(table: Table) = Expression(Operation.PRODUCT, this, table)
//    infix fun Table.product(expr: Expression) = Expression(Operation.PRODUCT, this@product, expr)
//    infix fun product(expr: Expression) = Expression(Operation.PRODUCT, this, expr)


    // pack Expression into ExpressionHolder
    // traverse the "tree" of expression
    fun asExpressionHolder() : ExpressionHolder<T> {
        val argsList = mutableListOf<MatrixImplListDto<T>>()
        val rootExpr = traverseExprRec(this, argsList)

        return ExpressionHolder(argsList, rootExpr)
    }

    // traverse helper
    private fun traverseExprRec(
        expr: Expression<T>,
        argsList: MutableList<MatrixImplListDto<T>>,
    ) : ExpressionDto {
        // getting components of ExpressionDto
        val leftExprDto = expr.leftExpr?.let {traverseExprRec(it, argsList)}
        val rightExprDto = expr.rightExpr?.let {traverseExprRec(it, argsList)}

        val leftMatrixDto = expr.leftMatrix?.let {
            if(it in argsList){
                argsList.indexOf(it)
            }else{
                argsList.add(it)
                argsList.size - 1
            }
        }
        val rightMatrixDto = expr.rightMatrix?.let {
            if(it in argsList){
                argsList.indexOf(it)
            }else{
                argsList.add(it)
                argsList.size - 1
            }
        }

        return ExpressionDto(expr.operation, leftExprDto, rightExprDto, leftMatrixDto, rightMatrixDto)
    }
}