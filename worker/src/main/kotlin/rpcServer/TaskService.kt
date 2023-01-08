package rpcServer


import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import matrix.MatrixImplList
import matrix.asMatrix
import shared.Logger
import shared.expression.Operation
import shared.expression.TaskHolder

object TaskService {

    private val logger = Logger()

    fun calculateTask(taskMessage: String): String {
        val taskHolder = Json.decodeFromString<TaskHolder<Float>>(taskMessage)//TODO поправить


        logger.info("received task: (${taskHolder.operation.name}), ${taskHolder.arg1}, ${taskHolder.arg2}")

        val result = when (taskHolder.operation) {
            Operation.PLUS -> (taskHolder.arg1!!.asMatrix() + taskHolder.arg2!!.asMatrix()).asDto()
            Operation.MINUS -> (taskHolder.arg1!!.asMatrix() - taskHolder.arg2!!.asMatrix()).asDto()
            Operation.INVERSE -> taskHolder.arg1!!.asMatrix().inverse().asDto()
            Operation.TRANSPOSE -> taskHolder.arg1!!.asMatrix().transpose().asDto()
            Operation.TIMES -> (taskHolder.arg1!!.asMatrix() * taskHolder.arg2!!.asMatrix()).asDto()
            else-> throw IllegalArgumentException("TaskService.calculateTask: Invalid operation ${taskHolder.operation}")
        }
        logger.info("result matrix: $result")

        return Json.encodeToString(result)
    }
}