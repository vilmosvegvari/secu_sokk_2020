package hu.bme.webshop.caff
import hu.bme.webshop.authentication.dto.response.MessageResponse
import hu.bme.webshop.models.ERole
import hu.bme.webshop.security.services.UserDetailsProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins =["\${webshop.app.origin}"])
@RestController
@RequestMapping("/api/caff")
class CaffController(val caffService: CaffService, val userService: UserDetailsProvider) {
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	fun all(): ResponseEntity<*> {
		return try {
			ResponseEntity.ok(caffService.findAll())
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	fun delete(@PathVariable(value = "id") id: Long): ResponseEntity<Any>{
		val result: Boolean
		val currentUser = userService.getUser()
		if(currentUser.getRoles().any { it.getName() == ERole.ROLE_ADMIN }){
			result = caffService.delete(id)
		} else {
			result = caffService.deleteIfSameUser(id, currentUser)
		}

		return if(result){
			ResponseEntity.ok(mapOf("message" to "ok"))
		} else {
			ResponseEntity.notFound().build<Any>()
		}
	}
}