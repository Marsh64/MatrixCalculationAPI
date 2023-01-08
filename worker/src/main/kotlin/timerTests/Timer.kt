package timerTests

import matrix.MatrixImplList
import matrix.asMatrix
import shared.expression.Operation
import shared.expression.TaskHolder
import kotlin.time.Duration.Companion.nanoseconds

typealias matrix = MatrixImplList<Float>

object Timer {
    fun timeForOperation(m1: matrix, m2: matrix, method: matrix.(matrix) -> matrix): Long{
        val start = System.currentTimeMillis()
        System.currentTimeMillis().nanoseconds
        m1.method(m2)
        val finish = System.currentTimeMillis()
        return finish - start
    }

    fun <E> timeForTaskHolder(task: TaskHolder<E>): Long{
        val start = System.currentTimeMillis()
        apply {
            when (task.operation) {
                Operation.PLUS -> (task.arg1!!.asMatrix() + task.arg2!!.asMatrix()).asDto()
                Operation.MINUS -> (task.arg1!!.asMatrix() - task.arg2!!.asMatrix()).asDto()
                Operation.INVERSE -> task.arg1!!.asMatrix().inverse().asDto()
                Operation.TRANSPOSE -> task.arg1!!.asMatrix().transpose().asDto()
                Operation.TIMES -> (task.arg1!!.asMatrix() * task.arg2!!.asMatrix()).asDto()
                else-> throw IllegalArgumentException("TaskService.calculateTask: Invalid operation ${task.operation}")
            }
        }
        val finish = System.currentTimeMillis()
        return finish - start
    }
}