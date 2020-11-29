package hu.bme.webshop.user

import hu.bme.webshop.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Long> {
	fun findByUsername(username: String): Optional<User>

	fun existsByUsername(username: String?): Boolean?
}