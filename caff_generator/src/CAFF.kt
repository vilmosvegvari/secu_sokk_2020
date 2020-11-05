import java.io.File

class CAFF(val credits: CAFFCredits) {
    var animations: ArrayList<CAFFAnimation> = ArrayList()

    val header: CAFFHeader = CAFFHeader()

    fun addCIFF(duration: ULong = 1000uL, ciff: CIFF) {
        animations.add(CAFFAnimation(duration, ciff))
        header.num_anim++
    }

    fun writeToFile(fileName: String) {
        val buf = ArrayList<Byte>()

        buf.addAll(CAFFBlock(
                id = BlockID.HEADER,
                data = header
        ).asByteList())

        buf.addAll(CAFFBlock(
                id = BlockID.CREDITS,
                data = credits
        ).asByteList())

        for (animation in animations) {
            buf.addAll(CAFFBlock(
                    id = BlockID.ANIMATION,
                    data = animation
            ).asByteList())
        }

        File(fileName).writeBytes(buf.toByteArray())
    }

    override fun toString(): String {
        return "CAFF(header=$header, credits=$credits, animations=$animations)"
    }
}