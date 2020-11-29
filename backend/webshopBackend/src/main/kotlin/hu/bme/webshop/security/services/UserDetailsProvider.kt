package hu.bme.webshop.security.services

import hu.bme.webshop.models.User
import hu.bme.webshop.security.services.UserDetailsImpl
import hu.bme.webshop.user.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserDetailsProvider(private val userRepository: UserRepository) {

	fun getUser(): User {
		val tmpUser = userRepository.findById(getUserDetail()!!.id)
		if (getUserDetail() == null || tmpUser.isEmpty) throw RuntimeException("USER_IS_NOT_LOGGED_IN")
		return tmpUser.get()
	}

	private fun getUserDetail(): UserDetailsImpl? {
		return if (SecurityContextHolder.getContext()?.authentication?.principal is String) {
			null
		} else {
			SecurityContextHolder.getContext()?.authentication?.principal as UserDetailsImpl?
		}
	}
}
