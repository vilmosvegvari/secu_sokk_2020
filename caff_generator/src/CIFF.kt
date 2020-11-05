import java.io.File
import kotlin.random.Random
import kotlin.random.nextUInt

class CIFF: ConvertToByteArray {
    var header: CIFFHeader = CIFFHeader()
    var content: CIFFContent = CIFFContent()

    fun getLength(): ULong {
        return header.getLength()
    }

    fun randomContent() {
        var i = 0uL
        val pixelNum = header.width * header.height
        while (i < pixelNum) {
            val red: UByte = Random.nextUInt(255u).toUByte()
            val green: UByte = Random.nextUInt(255u).toUByte()
            val blue: UByte = Random.nextUInt(255u).toUByte()
            content.pixels.add(Pixel(red, green, blue))
            ++i
        }
    }

    fun gradient(color: Colors) {
        var i = 0uL
        val pixelNum = header.width * header.height
        while (i < pixelNum) {
            when(color){
                Colors.RED -> content.pixels.add(Pixel((i / header.width * 255u / header.width).toUByte(), 0u, 0u))
                Colors.GREEN -> content.pixels.add(Pixel(0u, (i / header.width * 255u / header.width).toUByte(), 0u))
                Colors.BLUE -> content.pixels.add(Pixel(0u, 0u, (i / header.width * 255u / header.width).toUByte()))
//
//                Colors.RED -> content.pixels.add(Pixel(255u, 0u, 0u))
//                Colors.GREEN -> content.pixels.add(Pixel(0u, 255u, 0u))
//                Colors.BLUE -> content.pixels.add(Pixel(0u, 0u, 255u))
            }
//            val red: UByte = 128u
//            val green: UByte = 128u
//            val blue: UByte = 128u
//            content.pixels.add(Pixel(red, green, blue))
            ++i
        }
    }

    override fun asByteList(): List<Byte> {
        val buf = ArrayList<Byte>()
        buf.addAll(header.asByteList())
        buf.addAll(content.asByteList())

        return buf
    }

    override fun toString(): String {
        return "CIFF(header=$header, content=$content)"
    }

    fun writeToFile(fileName: String) {
        val file = File(fileName)
        file.writeBytes(asByteList().toByteArray())
    }
}