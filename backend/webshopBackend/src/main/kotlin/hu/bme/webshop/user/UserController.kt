package hu.bme.webshop.user

import hu.bme.webshop.models.User
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	fun me(): User? {
		return userService.me()
	}
}