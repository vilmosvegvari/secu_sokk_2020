package hu.bme.webshop.admin
import hu.bme.webshop.models.User
import hu.bme.webshop.security.services.UserDetailsProvider
import hu.bme.webshop.user.UserRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
	val userRepository: UserRepository
	//val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
) {

	fun findAll(): List<User> {
		//logger.info("UserId ${userService.getUser().id} get all users")
		return userRepository.findAll().filter { !it.isDeleted }
	}

	fun findById(userId: Long): User? {
		//logger.info("UserId ${userService.getUser().id} get userId ${userId} by id")
		return userRepository.findById(userId).get()
	}

	fun delete(userId: Long) {
		val user = userRepository.findById(userId).get()
		user.isDeleted = true
		userRepository.save(user)
	}
}