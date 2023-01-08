package shared.matrix

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable
data class MatrixImplListDto<E>(
    val rows: Int,
    val columns: Int,
    @Serializable(with = TypesSerializer::class)
    val type: ElementType,
    val matrix: MutableList<MutableList<E>>
){
    override fun toString(): String{
        var string = ""
        for (i in matrix.indices) {
            string += "["
            for (j in matrix[0].indices) {
                string += String.format("%${3 + 6}.2f", matrix[i][j])
            }
            string += "  ]\n"
        }
        return string
    }
}

object TypesSerializer : KSerializer<ElementType> {
    fun fromValue(value: Int) = ElementType.values().find { it.num == value }!!
    override val descriptor = PrimitiveSerialDescriptor("ElementType", PrimitiveKind.INT)
    override fun deserialize(decoder: Decoder) = fromValue(decoder.decodeInt())
    override fun serialize(encoder: Encoder, value: ElementType) = encoder.encodeInt(value.num)
}
