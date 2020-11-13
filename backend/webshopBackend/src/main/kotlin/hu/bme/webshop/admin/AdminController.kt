package hu.bme.webshop.admin
import hu.bme.webshop.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse

@RestController
@RequestMapping("/api/admin")
class AdminController(val adminService: AdminService) {

	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN')")
	fun all(): List<User> {
		return adminService.findAll()
	}

	@GetMapping("/user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun findById(@PathVariable(value = "id") id: Long): User? {
		return adminService.findById(id)
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun deleteById(@PathVariable(value = "id") id: Long): ResponseEntity<*> {
		adminService.delete(id)
		return ResponseEntity.ok(HttpStatus.OK)
	}}