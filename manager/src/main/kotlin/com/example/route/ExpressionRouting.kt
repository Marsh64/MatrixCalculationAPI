package com.example.route

import com.example.rpcClient.ResultsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import rpcClient.ExpressionService
import shared.Logger
import shared.expression.ExpressionHolder
import shared.matrix.MatrixImplListDto

fun Route.expressionRouting() {

    route("/example"){
        get{
            val type = call.request.queryParameters["type"]
            Logger.info("Route.expressionRouting(/example?type=$type): received expression via POST")
            val exampleExpressionHolderString = when(type){
                "int" -> "{\"argsList\":[{\"rows\":2,\"columns\":2,\"type\":0,\"matrix\":[[1,0],[0,1]]}],\"rootExpr\":{\"operation\":\"PLUS\",\"leftTableIndex\":0,\"rightTableIndex\":0}}"
                "float" -> "{\"argsList\":[{\"rows\":2,\"columns\":2,\"type\":1,\"matrix\":[[1.0,0.0],[0.0,1.0]]},{\"rows\":2,\"columns\":2,\"type\":1,\"matrix\":[[1.0,2.0],[3.0,4.0]]}],\"rootExpr\":{\"operation\":\"MINUS\",\"leftTableIndex\":0,\"rightTableIndex\":1}}"
                else -> throw IllegalArgumentException("Route.expressionRouting(/example?type=$type): type is unknown")
            }

            call.respondText(exampleExpressionHolderString)
            call.response.status(HttpStatusCode.OK)
        }
    }

    route("/expression") {
        post {
            val type = call.request.queryParameters["type"]
            Logger.info("Route.expressionRouting(/expression?type=$type): received expression via POST")

            val id: Int
            when(type){
//                "int" -> {
//                    val expr = call.receive<ExpressionHolder<Int>>()
//                    val deferredResult = async {
//                        val ret = ExpressionService.compute(expr)
//                        Logger.info("Route.expressionRouting(/expression?type=$type): expression via POST is sent")
//                        ret
//                    }
//                    id = ResultsRepository.putInt(deferredResult)
//                }
                "float" -> {
                    val expr = call.receive<ExpressionHolder<Float>>()
                    val deferredResult = async {
                        val ret = ExpressionService.compute(expr)
                        Logger.info("Route.expressionRouting(/expression?type=$type): expression via POST is sent")
                        ret
                    }
                    id = ResultsRepository.putFloat(deferredResult)
                }
                else ->  id = -1
            }

            call.respond(id)
            call.response.status(HttpStatusCode.Created)
            Logger.info("Route.expressionRouting(/expression?type=$type): sent response to POST")
        }

        get {
            val type = call.request.queryParameters["type"]
            val corrId = call.request.queryParameters["id"]?.toInt()!!
            Logger.info("Route.expressionRouting(/expression?type=$type): received GET request for results")

            when(type){
//                "int" -> {
//                    val result = ResultsRepository.getInt(corrId) // waiting for result here
//                    call.respond<MatrixImplListDto<Int>>(result)
//                }
                "float" -> {
                    val result = ResultsRepository.getFloat(corrId) // waiting for result here
                    call.respond(result.toString())
                    call.response.status(HttpStatusCode.OK)
                }
                else -> throw IllegalArgumentException("Route.expressionRouting(/expression?type=$type): type is unknown")
            }
            Logger.info("Route.expressionRouting(/expression?type=$type): sent response to GET")
        }
    }
}