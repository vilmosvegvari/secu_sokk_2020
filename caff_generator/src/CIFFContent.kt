class CIFFContent : ConvertToByteArray {
    var pixels: ArrayList<Pixel> = ArrayList()

    fun getLength(): ULong {
        return pixels.size.toULong() * 3uL
    }

    override fun asByteList(): List<Byte> {
        val myList = List<Byte>(pixels.size * 3) { i ->
            if (i % 3 == 0)
                pixels[i / 3].blue.toByte()
            else if (i % 3 == 1)
                pixels[i / 3].red.toByte()
            else
                pixels[i / 3].green.toByte()
        }

        return myList
    }

    override fun toString(): String {
        return "CIFFContent(pixels=$pixels)"
    }
}