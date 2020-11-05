fun main(args: Array<String>) {

    val size = 1000uL

    val ciff1 = CIFF().also {
        it.header = CIFFHeader().also {
            it.caption = "CIFF1"
            it.width = size
            it.height = size
            it.addTag("tag1")
            it.addTag("tag2")
        }
        it.gradient(Colors.RED)
    }

    val ciff2 = CIFF().also {
        it.header = CIFFHeader().also {
            it.caption = "CIFF2"
            it.width = size
            it.height = size
            it.addTag("tag3")
            it.addTag("tag4")
        }
        it.gradient(Colors.GREEN)
    }

    println(ciff1.header)
    println(ciff2.header)

    val caff = CAFF(credits = CAFFCredits(creation_date = CreationDate.now(), creator = "FERI"))
    caff.addCIFF(ciff = ciff1, duration = 500u)
    caff.addCIFF(ciff = ciff2, duration = 500u)
    caff.addCIFF(ciff = ciff1, duration = 500u)
    caff.addCIFF(ciff = ciff2, duration = 500u)
    caff.addCIFF(ciff = ciff1, duration = 500u)
    caff.addCIFF(ciff = ciff2, duration = 500u)
    caff.addCIFF(ciff = ciff1, duration = 500u)
    caff.addCIFF(ciff = ciff2, duration = 500u)

    println(caff)

    caff.writeToFile("final.caff")
}