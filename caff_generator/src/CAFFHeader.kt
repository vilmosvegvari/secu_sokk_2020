class CAFFHeader : CAFFData() {
    val magic: String = "CAFF"
    val header_size: ULong
        get() = 4uL + 8uL + 8uL
    var num_anim: ULong = 0uL

    override fun getLength(): ULong {
        return (magic.length + 8 + 8).toULong()
    }

    override fun asByteList(): List<Byte> {
        val buf = ArrayList<Byte>()
        buf.addString(magic)
        buf.addULong(header_size)
        buf.addULong(num_anim)

        return buf
    }

    override fun toString(): String {
        return "CAFFHeader(magic='$magic', num_anim=$num_anim, header_size=$header_size)"
    }
}