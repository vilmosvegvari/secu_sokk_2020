package hu.bme.webshop.user

import hu.bme.webshop.models.User
import hu.bme.webshop.security.services.UserDetailsProvider
import hu.bme.webshop.security.services.UserDetailsServiceImpl
import hu.bme.webshop.user.IUserService
import hu.bme.webshop.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
	val userService: UserDetailsProvider,
	val userRepository: UserRepository
	//val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
) : IUserService {

	override fun findAll(): MutableIterable<User> {
		//logger.info("UserId ${userService.getUser().id} get all users")
		return userRepository.findAll()
	}

	override fun findById(userId: Long): User? {
		//logger.info("UserId ${userService.getUser().id} get userId ${userId} by id")
		return userRepository.findById(userId).get()
	}

	override fun delete(userId: Long): Boolean {
		//TODO: not implemented
		return false
	}

	override fun me(): User {
		return userRepository.findById(userService.getUser().id).get()
	}
}