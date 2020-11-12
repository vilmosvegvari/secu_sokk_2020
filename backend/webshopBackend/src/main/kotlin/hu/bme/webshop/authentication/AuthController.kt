package hu.bme.webshop.authentication

import hu.bme.webshop.models.User
import hu.bme.webshop.authentication.dto.request.LoginRequest
import hu.bme.webshop.authentication.dto.request.RolesRequest
import hu.bme.webshop.authentication.dto.request.SignupRequest
import hu.bme.webshop.authentication.dto.request.SignupResponse
import hu.bme.webshop.authentication.dto.response.MessageResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(
	val authService: AuthService
) {
	@PostMapping("/login")
	fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest?): ResponseEntity<*> {
		return ResponseEntity.ok(authService.authenticateUser(loginRequest))
	}

	@PostMapping("/signup")
	fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest?): ResponseEntity<*> {
		val signupResponse =  SignupResponse()
		return try {
			val newUser: User = authService.registerUser(signUpRequest)
			signupResponse.user = newUser
			signupResponse.message = "User registered successfully"
			val loginRequest = LoginRequest(signUpRequest!!.username, signUpRequest.password)
			val loginResponse = authService.authenticateUser(loginRequest)
			signupResponse.token = loginResponse.accessToken
 			ResponseEntity.ok(signupResponse)
		} catch (e: Exception) {
			signupResponse.message = e.message!!
			ResponseEntity.badRequest().body(signupResponse)
		}
	}

	@PostMapping("/set-roles/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun setAdmin(@PathVariable(value = "id") id: Long, @RequestBody rolesRequest: @Valid RolesRequest?): ResponseEntity<*> {
		return try {
			authService.setAdmin(id, rolesRequest)
			ResponseEntity.ok(MessageResponse("User roles updated successfully!"))
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}

	/* TODO: logout
	@PostMapping("/logout")
	fun logout(@RequestBody logoutRequest: @Valid LogoutRequest?): ResponseEntity<*> {

	}
	 */
}
