package matrix
import TB.matrix.*
import shared.matrix.ElementType
import shared.matrix.MatrixImplListDto
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.random.Random

class MatrixImplList<E> : Matrix<E> {
    override val rows: Int
    override val columns: Int
    override val basis: TypicalBasis<E> //Базисные операции, нулевой и нейтральный элемент для типа <E>
    private val matrix: MutableList<MutableList<E>>

    companion object{
        //Создание единичной матрицы для типа <E>
        fun <E> eye(r : Int, bss: TypicalBasis<E>) : MatrixImplList<E>{
            val res = MatrixImplList<E>(r, r, bss)
            for (i in 0 until r){
                res[i, i] = bss.eNeutral
            }

            return res
        }

        fun randomIntMatrixImplList(r: Int, c: Int): MatrixImplList<Int> =
            MatrixImplList(r, c, IntTypicalBasis()){_ -> Random.nextInt(-100000, 100000)}

        fun randomFloatMatrixImplList(r: Int, c: Int): MatrixImplList<Float> =
            MatrixImplList(r, c, FloatTypicalBasis()){_ -> Random.nextDouble(-100000.0, 100000.0).toFloat() }

        fun <E> CreateMatrixImplList (r: Int, c: Int, e: E, bss: TypicalBasis<E>) : MatrixImplList<E>{
            return MatrixImplList(r, c, e, bss)
        }

        fun <E> CreateMatrixImplList (r: Int, c: Int, bss: TypicalBasis<E>) : MatrixImplList<E>{
            return MatrixImplList(r, c, bss)
        }

        fun <E> CreateMatrixImplList (r: Int, c: Int, arr : Array<E>, bss: TypicalBasis<E>) : MatrixImplList<E>{
            return MatrixImplList(r, c, arr, bss)
        }
    }
    constructor(r: Int, c: Int, e: E, operators: TypicalBasis<E>){
        rows = r
        columns = c
        basis = operators
        matrix = MutableList(rows){ _ -> MutableList(columns){ _ -> e } }
    }

    //Создаение нулевой матрицы
    constructor(r: Int, c: Int, basis: TypicalBasis<E>){
        rows = r
        columns = c
        this.basis = basis
        matrix = MutableList(rows){ _ -> MutableList(columns){ _ -> with(basis){eNull} } }
    }
    constructor(r: Int, c: Int, bss: TypicalBasis<E>, init: (index: Int) -> E){
        rows = r
        columns = c
        basis = bss
        matrix = MutableList(rows){ _ -> MutableList(columns, init) }
    }

    constructor(r: Int, c: Int, bss: TypicalBasis<E>, arr: MutableList<MutableList<E>>){
        rows = r
        columns = c
        basis = bss
        matrix = arr.toTypedArray().copyOf().toMutableList()
    }
    constructor(other: MatrixImplList<E>){
        rows = other.rows
        columns = other.columns
        basis = other.basis
        matrix = MutableList(rows){index1 -> MutableList(columns){index2 -> other[index1,index2]} }
    }
    constructor(r: Int, c: Int, arr : Array<E>, bss: TypicalBasis<E>){
        if (r * c != arr.size) throw IllegalArgumentException()

        val a = arr.iterator()
        rows = r
        columns = c
        basis = bss
        matrix = MutableList(rows){ _ -> MutableList(columns){ _ -> a.next() } }
    }
    constructor(r: Int, c: Int, arr : List<E>, bss: TypicalBasis<E>){
        if (r * c != arr.size) throw IllegalArgumentException()

        val a = arr.iterator()
        rows = r
        columns = c
        basis = bss
        matrix = MutableList(rows){ _ -> MutableList(columns){ _ -> a.next() } }
    }


    override fun get(row: Int, column: Int): E{
        if (row >= rows || rows < 0 || column >= columns || columns < 0) throw IllegalArgumentException()
        return matrix[row][column]
    }
    override fun get(cell: Cell): E = get (cell.row, cell.column)
    override fun set(row: Int, column: Int, value: E) {
        if (row >= rows || rows < 0 || column >= columns || columns < 0) throw IllegalArgumentException()
        matrix[row][column] = value
    }
    override fun set(cell: Cell, value: E) = set(cell.row, cell.column, value)

    override fun equals(other: Any?) : Boolean =
        other is MatrixImplList<*> &&
            rows == other.rows &&
            columns == other.columns &&
            this.toString() == other.toString()

    fun toArray(): ArrayList<E>{
        //val arr = matrix
        //val res = ArrayList<E>(rows * columns){index -> matrix[index / columns][index % columns]}
        val res = ArrayList<E>(rows * columns)
        var k = 0
        for (i in 0 until rows){
            for (j in 0 until columns){
                res.add(this[i, j])
            }
        }

        return res;
    }

//    override fun toString(): String{
//        var string = ""
//        for (i in 0 until rows) {
//            string += "["
//            for (j in 0 until columns) {
//                string += String.format("%${3 + 6}.2f", this[i, j])
//            }
//            string += "  ]\n"
//        }
//        return string
//    }

    override fun toString(): String{
        var res = ""
        for (i in 0 until rows){
            res += "| "
            for (j in 0 until columns){
                res += matrix[i][j].toString() + ' '
            }
            res += "|\n"
        }

        return res
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        return result
    }

    operator fun plus (other: Matrix<E>) : MatrixImplList<E>{
        val res = MatrixImplList<E>(this)
        res += other
        return res
    }

//    operator fun plus (other: Matrix<E>) : MatrixImplList<E>{
//        val res = MatrixImplList<E>(this.rows, this.columns, this.basis)
//
//        val threadPool = Executors.newFixedThreadPool(2)
//
//        for (i in 0 until rows){
//            threadPool.submit{
//                for (j in 0 until columns){
//                    res[i, j] = with(basis){
//                        matrix[i][j] + other[i, j]
//                    }
//                }
//            }
//
//        }
//
//        threadPool.shutdown()
//        threadPool.awaitTermination(1000000000, TimeUnit.MILLISECONDS)
//        return res
//    }

    operator fun minus (other: Matrix<E>) : MatrixImplList<E>{
        val res = MatrixImplList<E>(this)
        res -= other
        return res
    }

    operator fun times (other: Matrix<E>) : MatrixImplList<E>{
        if (this.columns != other.rows) throw IllegalArgumentException()

        val res = CreateMatrixImplList(rows, other.columns, other.basis)
        val threadPool = Executors.newFixedThreadPool(2)

        for (i in 0 until rows){
            threadPool.submit{
                for (j in 0 until other.columns){
                    for (k in 0 until columns){
                        res[i, j] = with(basis){
                            res[i, j] + matrix[i][k] * other[k, j]
                        }
                    }
                }
            }
        }

        threadPool.shutdown()
        threadPool.awaitTermination(1000000000, TimeUnit.MILLISECONDS)
        return res
    }

    operator fun times (e: E): MatrixImplList<E>{
        val res = MatrixImplList<E>(this)
        for (i in 0 until rows){
            for (j in 0 until columns){
                res[i, j] = with(basis){
                    res[i, j] * e
                }
            }
        }

        return res
    }

    //Получение транспонированной матрицы
    fun transpose() : MatrixImplList<E>{
        val res = MatrixImplList<E>(columns, rows, this[0, 0], basis)
        for (i in 0 until rows){
            for (j in 0 until columns){
                res[j, i] = this[i, j]
            }
        }

        return res
    }

    //Получение матрицы без i строки и j столбца
    private fun getMat(r: Int, c : Int, m : MatrixImplList<E>) : MatrixImplList<E>{
        val res = MatrixImplList(m.rows - 1, m.columns - 1, m[0, 0], m.basis)
        var di = 0
        var dj = 0
        for (i in 0 until m.rows - 1){
            if (i >= r) di = 1
            dj = 0
            for (j in 0 until m.columns - 1){
                if (j >= c) dj = 1
                res[i , j] = m[i + di, j + dj]

            }
        }

        return res
    }

    //Подсчет детерминанта по опредлению в 1 потоке
    fun determinantDef() : E{
        if (columns != rows) throw IllegalArgumentException()
        //Получение матрицы без i строки и j столбца
//        fun getMat(r: Int, c : Int, m : MatrixImplList<E>) : MatrixImplList<E>{
//            val res = MatrixImplList(m.rows - 1, m.columns - 1, m[0, 0], m.ops)
//
//            for (i in 0 until m.rows){
//                if (i == r) continue
//                else{
//                    for (j in 0 until m.columns){
//                        if (j == c) continue
//                        else{
//                            if ((i > r) && (j > c))
//                                res[i - 1, j - 1] = m[i, j]
//                            else if (i > r)
//                                res[i - 1, j] = m[i, j]
//                            else if (j > r)
//                                res[i, j - 1] = m[i, j]
//                            else
//                                res[i, j] = m[i, j]
//                        }
//                    }
//                }
//            }
//
//            return res
//        }

        //Рекурсивное вычисление определителя
        fun determinant(mas : MatrixImplList<E>, m : Int): E{
            var k = 1 //(-1) в степени i
            val n = m - 1
            var d: E = with(basis){
                mas[0,0] - mas[0, 0]
            }
            if (m == 1){
                d = mas[0, 0]
                return d
            }
            if (m == 2){
                d = with(basis){
                    mas[0, 0] * mas[1, 1] - mas[1, 0] * mas[0, 1]
                }
                return d
            }
            if (m > 2){
                for (i in 0 until m){
                    val p = getMat(i, 0, mas)
                    d = with(basis){
                        if (k == -1)
                            d - mas[i, 0] * determinant(p, n)
                        else
                            d + mas[i, 0] * determinant(p, n)
                    }
                    k = -k
                }
            }

            return d
        }

        return determinant(this, this.rows)
    }

    //Получение обратной матрицы
    fun inverse() : MatrixImplList<E>{
        if (rows != columns) throw IllegalArgumentException()
        val det = this.determinantDef()

        val neutralElem = with(basis){
            matrix[0][0] - matrix[0][0]
        }
        if (det == neutralElem) throw IllegalArgumentException()
        val tempRes = MatrixImplList(this)

        for (i in 0 until rows){
            for (j in 0 until columns){
                val k = getMat(i, j, this).determinantDef()
                if ((i + j) % 2 == 0)
                    tempRes[i, j] = with(basis){
                        k / det
                    }
                else {
                    tempRes[i, j] = with(basis){
                        neutralElem - k / det
                    }
                }
            }
        }

        return tempRes.transpose()
    }

    //Применение функции к матрице
    fun <R> map(bssR: TypicalBasis<R>, init: (E) -> R) : MatrixImplList<R>{
        val arr = this.toArray().map(init)
        return MatrixImplList(rows, columns, arr, bssR)
    }

    fun pow(p: Int): MatrixImplList<E>{
        if (columns != rows) throw IllegalArgumentException()
        if (p < 0) throw IllegalArgumentException()

        if (p == 0)
            return MatrixImplList.eye(rows, basis)

        var res = MatrixImplList(this)

        fun recPow(matrix: MatrixImplList<E>, p: Int) : MatrixImplList<E>{
            return if (p > 0)
                matrix * recPow(matrix, p - 1)
            else
                eye(this.rows, basis)
        }

        return recPow(this, p)
    }

    fun asDto(): MatrixImplListDto<E> {
        val type = when(basis){
            is IntTypicalBasis -> ElementType.int
            is FloatTypicalBasis -> ElementType.float
            else -> throw IllegalArgumentException("MatrixImplList.asDto(): illegal value of basis")
        }

        return MatrixImplListDto<E>(rows, columns, type, matrix)
    }
}


fun <E> MatrixImplListDto<E>.asMatrix(): MatrixImplList<E>{
    val basis = when(type){
        ElementType.int -> IntTypicalBasis()
        ElementType.float -> FloatTypicalBasis()
        else -> throw IllegalArgumentException("MatrixImplListDto.asMatrix(): illegal value of type")
    }
    val res = MatrixImplList<E>(rows, columns, basis as TypicalBasis<E>, matrix)

    return res
}

