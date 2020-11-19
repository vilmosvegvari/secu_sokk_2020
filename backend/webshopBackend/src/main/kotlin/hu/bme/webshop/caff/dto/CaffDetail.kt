package hu.bme.webshop.caff.dto

class CaffDetail() {
    var id: Long = 0
    lateinit var name: String
    lateinit var creator: String
    var filesize: Long = 0
    lateinit var userName: String
    lateinit var tags: List<String>
    lateinit var captions: List<String>
    //TODO Comments
}
