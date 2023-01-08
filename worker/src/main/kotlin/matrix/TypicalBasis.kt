package TB.matrix

//Интерфейс с различными операторами для типа данных <E> и его реализации
interface TypicalBasis<E> {
    operator fun E.plus(other: E): E
    operator fun E.minus(other: E): E
    operator fun E.times(other: E): E
    operator fun E.div(other: E): E
    operator fun E.rem(other: E): E

    val eNull : E
    val eNeutral : E
}

class IntTypicalBasis: TypicalBasis<Int> {
    override operator fun Int.plus(other: Int)
            = this.plus(other)

    override operator fun Int.minus(other: Int)
            = this.minus(other)

    override operator fun Int.times(other: Int)
            = this.times(other)

    override operator fun Int.div(other: Int)
            = this.div(other)

    override operator fun Int.rem(other: Int)
            = this.rem(other)

    override val eNull: Int = 0
    override val eNeutral: Int = 1
}

class FloatTypicalBasis: TypicalBasis<Float>{
    override operator fun Float.plus(other: Float)
            = this.plus(other)

    override operator fun Float.minus(other: Float)
            = this.minus(other)

    override operator fun Float.times(other: Float)
            = this.times(other)

    override operator fun Float.div(other: Float)
            = this.div(other)

    override operator fun Float.rem(other: Float)
            = this.rem(other)

    override val eNull: Float = 0f
    override val eNeutral: Float = 1f
}