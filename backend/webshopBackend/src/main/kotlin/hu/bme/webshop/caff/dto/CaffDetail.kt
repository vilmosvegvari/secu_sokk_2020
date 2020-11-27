package hu.bme.webshop.caff.dto

import hu.bme.webshop.models.Comment

class CaffDetail() {
    var id: Long = 0
    lateinit var name: String
    lateinit var creator: String
    lateinit var gif: String
    lateinit var download: String
    var numAnim: Long = 0
    var filesize: Long = 0
    lateinit var userName: String
    var tags: List<String> = ArrayList()
    var captions: List<String> = ArrayList()
    var comments: List<Comment> = ArrayList()
}
