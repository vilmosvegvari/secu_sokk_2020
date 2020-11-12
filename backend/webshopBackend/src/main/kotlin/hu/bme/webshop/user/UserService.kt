package hu.bme.webshop.user

import hu.bme.webshop.models.User
import hu.bme.webshop.security.services.UserDetailsProvider
import org.springframework.stereotype.Service

@Service
class UserService(
	val userService: UserDetailsProvider,
	val userRepository: UserRepository
	//val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
) {
	fun me(): User {
		return userRepository.findById(userService.getUser().id).get()
	}
}