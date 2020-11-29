package hu.bme.webshop.admin
import hu.bme.webshop.authentication.dto.response.MessageResponse
import hu.bme.webshop.models.User
import hu.bme.webshop.user.dto.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse

@CrossOrigin(origins =["\${webshop.app.origin}"])
@RestController
@RequestMapping("/api/admin")
class AdminController(val adminService: AdminService) {

	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN')")
	fun all(): ResponseEntity<*> {
		return try {
			ResponseEntity.ok(adminService.findAll())
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}

	@GetMapping("/user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun findById(@PathVariable(value = "id") id: Long): ResponseEntity<*> {
		return try {
			ResponseEntity.ok(adminService.findById(id))
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun deleteById(@PathVariable(value = "id") id: Long): ResponseEntity<*> {
		return try {
			ResponseEntity.ok(adminService.delete(id))
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(MessageResponse(e.message!!))
		}
	}}