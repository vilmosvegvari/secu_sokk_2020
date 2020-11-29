package hu.bme.webshop.security.services

import hu.bme.webshop.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {
	@Autowired
	var userRepository: UserRepository? = null

	@Transactional
	@Throws(UsernameNotFoundException::class)
	override fun loadUserByUsername(username: String): UserDetails {
		val user = userRepository!!.findByUsername(username)
			.orElseThrow { UsernameNotFoundException("User Not Found with username: $username") }
		return UserDetailsImpl.build(user)
	}
}