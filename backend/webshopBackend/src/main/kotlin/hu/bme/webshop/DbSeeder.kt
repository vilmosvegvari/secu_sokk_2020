package hu.bme.webshop

import hu.bme.webshop.models.ERole
import hu.bme.webshop.models.Role
import hu.bme.webshop.models.User
import hu.bme.webshop.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DbSeeder(
	val userRepository: UserRepository,
	val roleRepository: RoleRepository
	//val logger: Logger = LoggerFactory.getLogger(DbSeeder::class.java)
) : CommandLineRunner {
	@Autowired
	var encoder: PasswordEncoder? = null

	override fun run(vararg p0: String?) {

		if (!roleRepository.existsById(1)) {
			roleRepository.save(Role(ERole.ROLE_ADMIN))
			roleRepository.save(Role(ERole.ROLE_MODERATOR))
			roleRepository.save(Role(ERole.ROLE_USER))
		}

		if (!userRepository.findByUsername("admin").isPresent) {
			val user = User(
				"admin",
				encoder!!.encode("admin")
			)

			val roles: MutableSet<Role> = HashSet()
			val adminRole: Role = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow {
					//logger.info("Error: ${ERole.ROLE_USER} User role is not found.")
					throw RuntimeException("Error: User role is not found.")
				}
			roles.add(adminRole)
			user.setRoles(roles)
			userRepository.save(user)
		}
		//logger.info(" -- Database has been initialized")
	}
}