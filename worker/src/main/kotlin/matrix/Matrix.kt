package matrix
import TB.matrix.TypicalBasis

data class Cell(val row: Int, val column: Int)

interface Matrix<E>{
    val rows: Int
    val columns: Int
    val basis: TypicalBasis<E>

    operator fun get(row: Int, column: Int): E
    operator fun get(cell: Cell): E

    operator fun set(row: Int, column: Int, value: E)
    operator fun set(cell: Cell, value: E)

    operator fun plusAssign(other: Matrix<E>){
        if (columns != other.columns || rows != other.rows) throw IllegalArgumentException()
        var a: E
        var b: E
        if (columns < 1 || rows < 1) return
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                a = this[i, j]
                b = other[i, j]
                this[i, j] =
                    with(basis){
                        a + b
                    }
            }
        }
    }
    operator fun minusAssign(other: Matrix<E>){
        if (columns != other.columns || rows != other.rows) throw IllegalArgumentException()
        var a: E
        var b: E
        if (columns < 1 || rows < 1) return
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                a = this[i, j]
                b = other[i, j]
                this[i, j] =
                    with(basis){
                        a - b
                    }
            }
        }
    }

}

