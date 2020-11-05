class CAFFCredits(val creation_date: CreationDate, val creator: String) : CAFFData(){
    val creator_len: ULong
        get() = creator.length.toULong()

    override fun getLength(): ULong {
        return creation_date.getLength() + 8uL + creator_len
    }

    override fun asByteList(): List<Byte> {
        val buf = ArrayList<Byte>()
        buf.addAll(creation_date.asByteList())
        buf.addULong(creator_len)
        buf.addString(creator)

        return buf
    }

    override fun toString(): String {
        return "CAFFCredits(creationDate=$creation_date, creator='$creator', creator_len=$creator_len)"
    }
}