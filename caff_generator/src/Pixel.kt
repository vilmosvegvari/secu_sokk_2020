
data class Pixel (
    var red: UByte,
    var green: UByte,
    var blue: UByte
) {
    override fun toString(): String {
        return "{$red, $green, $blue}"
    }
}