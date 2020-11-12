package hu.bme.webshop.caff

import hu.bme.webshop.models.Caff
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CaffRepository : CrudRepository<Caff, Long> {
}