import TB.matrix.FloatTypicalBasis
import TB.matrix.IntTypicalBasis
import kotlinx.serialization.json.Json
import matrix.MatrixImplList
import rpcServer.Worker
import shared.expression.Expression
import kotlinx.serialization.encodeToString
import timerTests.ComparisonTimer
import java.util.Timer
import kotlin.time.Duration.Companion.nanoseconds

fun main(args: Array<String>) {
//    println("Hello World!")
//
//    val m1 = MatrixEasy(3, 5, true)
//    println(m1)
//    val m2 = MatrixEasy(3, 5, true)
//    println(m2)
//    println(m1 + m2)
////
    val M1 = MatrixImplList.eye(2, FloatTypicalBasis()).asDto()
    val M2 = MatrixImplList(2, 2, FloatTypicalBasis(), mutableListOf<MutableList<Float>>(mutableListOf(2f, 2f), mutableListOf(10f, 10f))).asDto()
    val M3 =  MatrixImplList(2, 2, FloatTypicalBasis(), mutableListOf<MutableList<Float>>(mutableListOf(1f, 2f), mutableListOf(3f, 4f))).asDto()

////    val expr1 = Expression{
////        transpose(M1 + M3 + (M2 - M1)) + M2 - inverse(transpose(M2 + inverse(M2) - M1))
////    }
//
//    val expr2 = Expression{
//        transpose((M1 * M3) + M2 * M3 - transpose(M2 + inverse(M1)))
//    }
////
    val expr3 = Expression{
        (M1 + (M2 + M3)) + M3 - (M2 + M3)
    }

    val werw = Expression{
        (M1 * M2) + inverse(M1 + M3 - transpose(M1 + M1))
    }
////
////
//    val exprH2 = expr2.asExpressionHolder()
//    val exH2String = Json.encodeToString(exprH2)
//    //println(M1)
//    println(exH2String)
//    println(expr2.asExpressionHolder())

    val worker = Worker()
    worker.startConsuming()

//    val m1 = MatrixImplList.eye(2, FloatTypicalBasis())
//    val m2 = MatrixImplList(2, 2, FloatTypicalBasis(), mutableListOf<MutableList<Float>>(mutableListOf(1f, 2f), mutableListOf(3f, 4f)))
//    println(m1 * m2)


//    println(ComparisonTimer.checkTimes(10, 1000) { m1: MatrixImplList<Float>, m2: MatrixImplList<Float> ->
//        m1 + m2
//    }.values.toList())

//    val M1 = MatrixImplList.eye(2, FloatTypicalBasis())
//    val M2 = MatrixImplList(2, 2, FloatTypicalBasis(), mutableListOf<MutableList<Float>>(mutableListOf(1f, 2f), mutableListOf(3f, 4f)))
//
//    println(M1 + M2)

//    println(System.currentTimeMillis().nanoseconds)
//    println(System.currentTimeMillis())
}
