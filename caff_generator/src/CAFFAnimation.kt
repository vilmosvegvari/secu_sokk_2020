import kotlin.random.Random

class CAFFAnimation(var duration: ULong = Random.nextLong(1L, 5000L).toULong(),
                    var ciff: CIFF
) : CAFFData() {
    override fun getLength(): ULong {
        return 8uL + ciff.getLength()
    }

    override fun asByteList(): List<Byte> {
        val buf = ArrayList<Byte>()

        buf.addULong(duration)
        buf.addAll(ciff.asByteList())

        return buf
    }
}