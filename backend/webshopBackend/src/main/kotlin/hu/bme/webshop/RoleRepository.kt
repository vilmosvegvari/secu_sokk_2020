package hu.bme.webshop

import hu.bme.webshop.models.ERole
import hu.bme.webshop.models.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : CrudRepository<Role, Long> {
	fun findByName(name: ERole?): Optional<Role>
}