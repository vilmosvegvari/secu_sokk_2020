import java.time.LocalDateTime

class CreationDate: ConvertToByteArray {

    companion object {
        fun now(): CreationDate{
            val curDate = CreationDate()
            curDate.now()

            return curDate
        }
    }

    fun getLength(): ULong {
        return 2uL + 1uL + 1uL + 1uL + 1uL;
    }

    var year: UShort = 0u
    var month: UByte = 0u
    var day: UByte = 0u
    var hour: UByte = 0u
    var minute: UByte = 0u

    fun now(){
        val now = LocalDateTime.now()
        year = now.year.toUShort()
        month = now.monthValue.toUByte()
        day = now.dayOfMonth.toUByte()
        hour = now.hour.toUByte()
        minute = now.minute.toUByte()
    }

    override fun asByteList(): List<Byte> {
        val buf = ArrayList<Byte>()
        buf.addUShort(year)
        buf.add(month.toByte())
        buf.add(day.toByte())
        buf.add(hour.toByte())
        buf.add(minute.toByte())

        return buf
    }

    override fun toString(): String {
        return "$year.$month.$day $hour:$minute"
    }
}