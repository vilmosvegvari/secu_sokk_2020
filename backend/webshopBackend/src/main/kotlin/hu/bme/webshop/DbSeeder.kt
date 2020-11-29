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
			roleRepository.save(Role(ERole.ROLE_USER))
		}


		if (!userRepository.findByUsername("admin").isPresent) {
			val pass = "$2a$10\$hjWWQgBuf23.TspkFf5OseL4IX6Dgap.JYpg2V0fJVBqbJPuqqfaS"
			val user = User(
				"admin",
				encoder!!.encode(pass)
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