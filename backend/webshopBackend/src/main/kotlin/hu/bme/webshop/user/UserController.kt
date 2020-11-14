package hu.bme.webshop.user

import hu.bme.webshop.authentication.dto.response.MessageResponse
import hu.bme.webshop.models.User
import hu.bme.webshop.user.dto.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins =["\${webshop.app.origin}"])
@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	fun me(): ResponseEntity<*> {
		return try {
			ResponseEntity.ok(userService.me())
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}
}