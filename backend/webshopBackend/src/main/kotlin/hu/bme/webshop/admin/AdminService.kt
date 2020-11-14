package hu.bme.webshop.admin
import hu.bme.webshop.models.User
import hu.bme.webshop.security.services.UserDetailsProvider
import hu.bme.webshop.user.UserRepository
import hu.bme.webshop.user.dto.UserResponse
import org.springframework.stereotype.Service

@Service
class AdminService(
	val userRepository: UserRepository
	//val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
) {

	fun findAll(): MutableIterable<User> {
		//logger.info("UserId ${userService.getUser().id} get all users")
		return userRepository.findAll()
	}

	fun findById(userId: Long): UserResponse {
		//logger.info("UserId ${userService.getUser().id} get userId ${userId} by id")
		return userRepository.findById(userId).get().toUserResponse()
	}

	fun delete(userId: Long): Long {
		val user = userRepository.findById(userId).get()
		user.isDeleted = true
		userRepository.save(user)
		return user.id
	}
}