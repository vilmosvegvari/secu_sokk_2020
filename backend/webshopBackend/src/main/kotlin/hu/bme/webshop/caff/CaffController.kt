package hu.bme.webshop.caff
import hu.bme.webshop.authentication.dto.response.MessageResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins =["\${webshop.app.origin}"])
@RestController
@RequestMapping("/api/caff")
class CaffController(val caffService: CaffService) {
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	fun all(): ResponseEntity<*> {
		return try {
			ResponseEntity.ok(caffService.findAll())
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}

}