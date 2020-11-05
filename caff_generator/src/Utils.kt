
enum class Colors{
    RED,
    GREEN,
    BLUE
}

fun ArrayList<Byte>.addULong(value: ULong){
//    this.add(((value shr 56) and 65535u).toByte())
//    this.add(((value shr 48) and 65535u).toByte())
//    this.add(((value shr 40) and 65535u).toByte())
//    this.add(((value shr 32) and 65535u).toByte())
//    this.add(((value shr 24) and 65535u).toByte())
//    this.add(((value shr 16) and 65535u).toByte())
//    this.add(((value shr 8) and 65535u).toByte())
//    this.add((value and 65535u).toByte())

    this.add((value and 65535u).toByte())
    this.add(((value shr 8) and 65535u).toByte())
    this.add(((value shr 16) and 65535u).toByte())
    this.add(((value shr 24) and 65535u).toByte())
    this.add(((value shr 32) and 65535u).toByte())
    this.add(((value shr 40) and 65535u).toByte())
    this.add(((value shr 48) and 65535u).toByte())
    this.add(((value shr 56) and 65535u).toByte())
}

fun ArrayList<Byte>.addUShort(value: UShort){
    this.add((value and 65535u).toByte())
    this.add(((value.rotateRight(8) ) and 65535u).toByte())
}

fun ArrayList<Byte>.addString(value: String){
    for(c in value){
        this.add(c.toByte())
    }
}