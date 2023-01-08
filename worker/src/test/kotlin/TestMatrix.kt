import TB.matrix.*
import matrix.MatrixImplList
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame

class TestMatrix {

    @Test
    fun constructorsTest(){
        //val matrix1 = MatrixImplList<Int>()
        //assertEquals(matrix1.rows, 0)
        //assertEquals(matrix1.columns, 0)


        val matrix2 = MatrixImplList<Int>(5, 5, 2, IntTypicalBasis())
        assertEquals(matrix2.columns, 5)
        assertEquals(matrix2.rows, 5)
        assertEquals(matrix2[0, 0], 2)
        assertEquals(matrix2[2, 4], 2)

        val matrix3 = MatrixImplList<Int>(5, 5, IntTypicalBasis()) { it -> it }
        assertEquals(matrix3.columns, 5)
        assertEquals(matrix3.rows, 5)
        assertEquals(matrix3[0, 0], 0)
        assertEquals(matrix3[2, 4], 4)
        assertEquals(matrix3[1, 2], 2)

        val matrix4 = MatrixImplList<Int>(6, 7, IntTypicalBasis())
        assertEquals(matrix4.columns, 7)
        assertEquals(matrix4.rows, 6)
        assertEquals(matrix4[0, 0], 0)
        assertEquals(matrix4[2, 4], 0)

        val matrix5 = MatrixImplList<Int>(matrix3)
        assertEquals(matrix5.columns, 5)
        assertEquals(matrix5.rows, 5)
        assertEquals(matrix5[0, 0], 0)
        assertEquals(matrix5[2, 4], 4)
        assertEquals(matrix5[1, 2], 2)
        assertNotSame(matrix5, matrix3)

        val arr = arrayOf(1, 2, 3, 4, 5, 6)
        val matrix6 = MatrixImplList<Int>(3, 2, arr, IntTypicalBasis())
        assertEquals(matrix6.columns, 2)
        assertEquals(matrix6.rows, 3)
        assertEquals(matrix6[0, 0], 1)
        assertEquals(matrix6[1, 1], 4)
    }

    @Test
    fun operatorsTest(){
        val matrix0 = MatrixImplList<Int>(2, 3, IntTypicalBasis())
        val matrix1 = MatrixImplList<Int>(2, 3, IntTypicalBasis()) { it -> it }
        val matrix2 = MatrixImplList<Int>(2, 3, IntTypicalBasis()) { it -> it }
        val matrix3 = MatrixImplList<Int>(2, 3, IntTypicalBasis()) { it -> it * it }
        val matrix4 = MatrixImplList<Int>(3, 2, IntTypicalBasis()) { it -> it }
        val matrix5 = MatrixImplList<Int>(2, 3, IntTypicalBasis()) { it -> it * 2 }
        val matrix6 = MatrixImplList<Int>(2, 2, IntTypicalBasis())

        //Checking equals()
        assertEquals(matrix1, matrix2)
        assertNotEquals(matrix1, matrix3)
        assertNotEquals(matrix1, matrix4)

        //Checking plus()
        assertEquals(matrix1 + matrix2, matrix5)
        assertEquals(matrix0 + matrix1, matrix1)

        //Checking minus()
        assertEquals(matrix1, matrix5- matrix2)
        assertEquals(matrix1, matrix1 - matrix0)

        //Checking times
        assertEquals(matrix6, matrix0 * matrix4)
        val arr1 = arrayOf(1, 2, 3, 4, 5, 6)
        val arr2 = arrayOf(7, 8, 9, 1, 2, 3)
        val arr3 = arrayOf(31, 19, 85, 55)
        val m1 = MatrixImplList(2, 3, arr1, IntTypicalBasis())
        val m2 = MatrixImplList(3, 2, arr2, IntTypicalBasis())
        val m3 = MatrixImplList(2, 2, arr3, IntTypicalBasis())
        assertEquals(m1 * m2, m3)

        //Checking times(with element type <E>)
        val arr4 = arrayOf(1, 2, 3, 4, 5, 6)
        val m4 = MatrixImplList(2, 3, arr4, IntTypicalBasis())
        val arr5 = arrayOf(2, 4, 6, 8, 10, 12)
        val m5 = MatrixImplList(2, 3, arr5, IntTypicalBasis())
        assertEquals(m4 * 2, m5)

    }

    @Test
    fun functionsTest(){
        //Checking transpose
        val arr1 = arrayOf(1, 2, 3, 4, 5, 6)
        val m1 = MatrixImplList(2, 3, arr1, IntTypicalBasis())
        val arr2 = arrayOf(1, 4, 2, 5, 3, 6)
        val m2 = MatrixImplList(3, 2, arr2, IntTypicalBasis())
        assertEquals(m1.transpose(), m2)

        //Checking toString
        val newarr = m1.toArray()
        assertEquals(newarr.size, arr1.size)
        assertEquals(newarr[0], arr1[0])
        assertEquals(newarr[newarr.size - 1], arr1[arr1.size - 1])

        //Checking determinant
        val arrd1 = arrayOf(1, 2, 3, 4, 10, 6, 7, 8, 9)
        val md1 = MatrixImplList(3, 3, arrd1, IntTypicalBasis())
        assertEquals(md1.determinantDef(), -60)
        val arrd2 = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val md2 = MatrixImplList(3, 3, arrd2, IntTypicalBasis())
        assertEquals(md2.determinantDef(), 0)

        //Checking map
        val arr6 = arrayOf(1, 2, 3, 4)
        val m6 = MatrixImplList(2, 2, arr6, IntTypicalBasis())
        val arr7 = arrayOf(1f, 2f, 3f, 4f)
        val m7 = MatrixImplList(2, 2, arr7, FloatTypicalBasis())
        assertEquals(m6.map(FloatTypicalBasis()){a : Int -> a.toFloat()}, m7)

        //Checking inverse
        val arr8 = arrayOf(1f, 2f, 3f, 4f)
        val m8 = MatrixImplList(2, 2, arr8, FloatTypicalBasis())
        assertEquals(m8.inverse() * m8, MatrixImplList.eye(2, FloatTypicalBasis()))

        //Checkin pow
        val m9 = MatrixImplList(2, 2, arr6, IntTypicalBasis())
        val arr10 = arrayOf(199, 290, 435, 634)
        val m10 = MatrixImplList(2, 2, arr10, IntTypicalBasis())
        assertEquals(m9.pow(4), m10)
    }

}