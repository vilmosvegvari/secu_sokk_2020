class CAFFBlock(
        val id: BlockID = BlockID.HEADER,
        val data: CAFFData
): ConvertToByteArray {
    val length: ULong
        get() = data.getLength()

    override fun asByteList(): List<Byte> {
        val buf = ArrayList<Byte>()

        buf.add(id.value.toByte())
        buf.addULong(length)
        buf.addAll(data.asByteList())

        return buf
    }
}