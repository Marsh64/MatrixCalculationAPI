package timerTests

import matrix.MatrixImplList
import timerTests.Timer.timeForOperation

object ComparisonTimer {
    fun checkTimes(size1:Int, size2: Int, method: (matrix, matrix) -> matrix): MutableMap<Int, Long>{
        val map = mutableMapOf<Int, Long>()
        for (i in size1..size2){
            val m1 = matrix.randomFloatMatrixImplList(i, i)
            val m2 = matrix.randomFloatMatrixImplList(i, i)
            map[i] = timeForOperation(m1, m2, method)
        }

        return map
    }

}