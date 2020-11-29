package hu.bme.webshop.upload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import hu.bme.webshop.models.Caff
import hu.bme.webshop.models.ECaffStatus
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
class RawCaff {
    lateinit var ciff_s: List<Ciff>
    lateinit var creator: String
    lateinit var date: Date

    fun getLocalDateTime(): LocalDateTime {
        return LocalDateTime.of(date.year.toInt(), date.month.toInt(), date.day.toInt(), date.hour.toInt(), date.minute.toInt())
    }

    class Ciff {
        lateinit var caption: String
        var duration: Long = 0
        lateinit var size: Size
        lateinit var tags: List<String>
    }

    class Size {
        var width: Long = 0
        var height: Long = 0
    }

    class Date {
        var day: Long = 0
        var hour: Long = 0
        var minute: Long = 0
        var month: Long = 0
        var year: Long = 0
    }
}