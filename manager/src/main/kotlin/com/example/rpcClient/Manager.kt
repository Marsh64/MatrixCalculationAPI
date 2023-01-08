package rpcClient

import com.rabbitmq.client.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.Logger
import shared.expression.TaskHolder
import shared.matrix.MatrixImplListDto
import java.util.*
import java.util.concurrent.CompletableFuture

object Manager {

    private var connection: Connection
    private var channel: Channel
    private val requestQueueName = "task_queue" //передавать через gradle.properties, должна совпадать с workerской

    init {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.port = 5672
        connection = factory.newConnection()
        channel = connection.createChannel()
        //replyQueueName = channel.queueDeclare().queue
    }

    //sending message to rabbitmq queue
    //resultTo - empty place to put result there
    private fun sendTaskMessage(message: String, resultTo: CompletableDeferred<MatrixImplListDto<Float>>?) {

        val corrId = UUID.randomUUID().toString() //taskMessage identifier
        val replyQueueName = channel.queueDeclare().queue //name of ?

        val props = AMQP.BasicProperties.Builder()
            .correlationId(corrId)
            .replyTo(replyQueueName)
            .build()

        //publish taskHolder in queue
        Logger.info("Manager.sendTaskMessage: start publishing task")
        channel.basicPublish("", requestQueueName, props, message.toByteArray(charset("UTF-8")))

        //val response = ArrayBlockingQueue<String>(1)
        val deliverCallback = DeliverCallback { _: String?, delivery: Delivery ->
            if (delivery.properties.correlationId == corrId) {
                val completed = Json.decodeFromString<MatrixImplListDto<Float>>(
                    String(delivery.body, charset("UTF-8"))
                )
                resultTo?.complete(completed)
            }
        }

        Logger.info("Manager.sendTaskMessage: start consuming response")
        // register consumer for response
        channel.basicConsume(replyQueueName, true, deliverCallback) { _ -> }
    }

    suspend fun sendTask(taskHolder: TaskHolder<Float>){
        taskHolder.arg1Future?.let { taskHolder.arg1 = it.await() }
        taskHolder.arg2Future?.let { taskHolder.arg2 = it.await() }

        //sending messages to queue
        val taskHolderString = Json.encodeToString(taskHolder)
        sendTaskMessage(taskHolderString, taskHolder.resultTo)
    }
}
