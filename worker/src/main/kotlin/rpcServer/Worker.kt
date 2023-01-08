package rpcServer

import com.rabbitmq.client.*
import shared.Logger


//as RpcServer
class Worker {

    private var connection: Connection
    private var channel: Channel
    private val rpcQueueName = "task_queue"//передавать через gradle.properties
    private val logger = Logger()

    init{
        val factory = ConnectionFactory()
        factory.host = "172.17.0.1" //"172.17.0.1" передавать через gradle.properties
        factory.port = 5672//передавать через gradle.properties
        connection = factory.newConnection()
        channel = connection.createChannel()

        channel.queueDeclare(rpcQueueName, false, false, false, null)
        channel.queuePurge(rpcQueueName) //purges the contents of the given queue
        channel.basicQos(1) //accept only one unpack-ed message at a time // передавать через gradle.properties

        logger.info(" [x] Awaiting RPC requests")
    }

    fun startConsuming(){
        val deliverCallback = DeliverCallback{ _: String?, delivery: Delivery ->
            val replyProps = AMQP.BasicProperties.Builder()
                .correlationId(delivery.properties.correlationId)
                .build()
            var response: String = "";
            try {
                val taskMessage = String(delivery.body, charset("UTF-8"))
                response += TaskService.calculateTask(taskMessage)

            } catch(e: RuntimeException){
                logger.info("Worker.startConsuming:" + e.stackTraceToString())
            } finally {
                logger.info("Worker.startConsuming: start publishing response")
                channel.basicPublish("", delivery.properties.replyTo, replyProps, response.toByteArray())
                channel.basicAck(delivery.envelope.deliveryTag, false)
            }
        }

        channel.basicConsume(rpcQueueName, false, deliverCallback){_ -> }
    }
}