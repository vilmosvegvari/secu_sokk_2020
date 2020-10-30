package hu.bme.webshop.user

import hu.bme.webshop.models.User
import hu.bme.webshop.user.IUserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(val iUserService: IUserService) {

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	fun all(): MutableIterable<User> {
		return iUserService.findAll()
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun findById(@PathVariable(value = "id") id: Long): User? {
		return iUserService.findById(id)
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun deleteById(@PathVariable(value = "id") id: Long): Boolean {
		return iUserService.delete(id)
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	fun me(): User? {
		return iUserService.me()
	}
}