class CIFFHeader : ConvertToByteArray {
    private val magic: String = "CIFF"

    //magic + header_size + content_size + width + height + caption + tags
    private val header_size: ULong
        get() {
            var tagSize = 0
            for (tag in tags) {
                tagSize += tag.length
            }

            return (4 + 8 + 8 + 8 + 8 + caption.length + tagSize).toULong()
        }

    private val content_size: ULong
        get() {
            return width * height * 3uL
        }

    var width: ULong = 0uL

    var height: ULong = 0uL

    var caption: String = ""
        set(value) {
            field = "$value\n"
        }

    private val tags: ArrayList<String> = ArrayList()

    fun getLength(): ULong {
        return header_size + content_size
    }

    fun addTag(tag: String) {
        tags.add("$tag\u0000")
    }

    override fun asByteList(): List<Byte> {
        val myList = ArrayList<Byte>()
        myList.addString(magic)
        myList.addULong(header_size)
        myList.addULong(content_size)
        myList.addULong(width)
        myList.addULong(height)
        myList.addString(caption)
        for (tag in tags){
            myList.addString(tag)
        }

        return myList
    }

    override fun toString(): String {
        return "CIFFHeader(magic='$magic', width=$width, height=$height, caption='$caption', tags=$tags, header_size=$header_size, content_size=$content_size)"
    }
}