package hu.bme.webshop.caff

import hu.bme.webshop.models.Caff
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CaffRepository : CrudRepository<Caff, Long> {

    fun findByFilename(filename: String): Optional<Caff>

}