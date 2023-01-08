package shared.matrix

import kotlinx.serialization.Serializable

@Serializable
enum class ElementType(val num: Int){
    int(0), float(1)
}