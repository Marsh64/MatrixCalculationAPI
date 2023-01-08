package matrix

import kotlin.random.Random

class MatrixEasy private constructor(){
    private var rows: Int = 0
    private var cols: Int = 0
    private var matrix: Array<DoubleArray> = Array(rows){ DoubleArray(cols) }

    //constructor: zero and random matrix
    constructor(r: Int, c: Int, keyRandom: Boolean = false): this(){
        if (r <= 0 || c <= 0) {
            throw IllegalArgumentException("Constructor: incorrect count of rows or columns")
        }

        rows = r
        cols = c

        if (keyRandom){
            matrix = Array(rows) {
                DoubleArray(cols) {
                    Random.nextDouble(1000.0, 10000.0)
                }
            }
        }else{
            matrix = Array(rows) { DoubleArray(cols) }
        }
    }

    //constructor: matrix by array
    constructor(array: Array<DoubleArray>): this(){
        if (!array.isEmpty()){
            rows = array.size
            cols = array[0].size
            matrix = array.copyOf()
        }
    }

    //constructor: matrix copy
    constructor(other: MatrixEasy): this(){
        rows = other.rows
        cols = other.cols
        matrix = other.matrix.copyOf()
    }

    operator fun get(rowIndex: Int, colIndex: Int): Double {
        if (rowIndex < 0 || colIndex < 0 || rowIndex >= rows || colIndex >= cols) {
            throw IllegalArgumentException("Matrix.get: Index out of bound")
        } else {
            return matrix[rowIndex][colIndex]
        }
    }

    operator fun set(rowIndex: Int, colIndex: Int, value: Number) {
        if (rowIndex < 0 || colIndex < 0 || rowIndex >= rows || colIndex >= cols) {
            throw IllegalArgumentException("Matrix.set: Index out of bound")
        } else {
            matrix[rowIndex][colIndex] = value.toDouble()
        }
    }

    operator fun plus(other: MatrixEasy): MatrixEasy{
        if (this.rows != other.rows || this.cols != other.cols){
            throw IllegalArgumentException("Matrix.plus: matrices with different size")
        }

        return MatrixEasy(
            Array(rows){ it1 ->
                DoubleArray(cols){ it2 ->
                    this[it1, it2] + other[it1, it2]
                }
            }
        )
    }


    override fun toString(): String{
        var string = ""
        for (i in 0 until rows) {
            string += "["
            for (j in 0 until cols) {
                string += String.format("%${3 + 6}.2f", this[i, j])
            }
            string += "  ]\n"
        }
        return string
    }

}