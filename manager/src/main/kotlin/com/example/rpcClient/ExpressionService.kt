package rpcClient

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import shared.Logger
import shared.expression.ExpressionDto
import shared.expression.ExpressionHolder
import shared.expression.TaskHolder
import shared.matrix.MatrixImplListDto
import java.util.concurrent.CompletableFuture

object ExpressionService {

    //send all tasks and wait for final result
    //не получилось сделать с дженериками из-за рефлекии на sendtask и sendTaskMessage, у которых из-за шаблонов нет конкретного типа
    suspend fun compute(expr: ExpressionHolder<Float>): MatrixImplListDto<Float> {
        // will store final result
        val result = CompletableDeferred<MatrixImplListDto<Float>>()
        val tasksList = expr.toTaskList(resTo = result)

        Logger.info("ExpressionService.compute: Started sending tasks")
        tasksList.map{task: TaskHolder<Float> ->
            Manager.sendTask(task)
        }
        Logger.info("ExpressionService.compute: Ended sending tasks")

        return result.await()
    }

    //recursive turns expression(task-tree) to list of tasks
    private fun <E> ExpressionHolder<E>.toTaskList(
        resTo: CompletableDeferred<MatrixImplListDto<E>>? = null
    ): List<TaskHolder<E>> {
        val tasksList: MutableList<TaskHolder<E>> = mutableListOf()
        traverseExprRec(this@toTaskList.rootExpr, tasksList, resTo, this@toTaskList.argsList)

        return tasksList
    }

    //traverse helper
    private fun <E> traverseExprRec(
        expr: ExpressionDto, // current expression
        tasksList: MutableList<TaskHolder<E>>, //necessary result
        resTo: CompletableDeferred<MatrixImplListDto<E>>? = null, // where to put result after finishing
        argsList: List<MatrixImplListDto<E>> // list of args from ExpressionHolder
    ) {
        if (expr.leftExpr == null && expr.rightExpr == null) {
            // both args are regular tables
            val newTask =
                TaskHolder(
                    expr.operation,
                    expr.leftTableIndex?.let { argsList[it] },
                    expr.rightTableIndex?.let { argsList[it] },
                    resultTo = resTo
                )
            tasksList.add(newTask)
        } else {
            // at least one arg is expression, has to be computed earlier
            val leftFuture = expr.leftExpr?.let {
                val future = CompletableDeferred<MatrixImplListDto<E>>()
                traverseExprRec(it, tasksList, future, argsList)
                future
            }
            val rightFuture = expr.rightExpr?.let {
                val future = CompletableDeferred<MatrixImplListDto<E>>()
                traverseExprRec(it, tasksList, future, argsList)
                future
            }
            val newTask = TaskHolder(
                expr.operation,
                expr.leftTableIndex?.let { argsList[it] },
                expr.rightTableIndex?.let { argsList[it] },
                leftFuture,
                rightFuture,
                resTo
            )

            tasksList.add(newTask)
        }
    }
}