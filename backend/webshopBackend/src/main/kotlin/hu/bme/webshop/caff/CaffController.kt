package hu.bme.webshop.caff
import hu.bme.webshop.models.Caff
import hu.bme.webshop.models.User
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/caff")
class CaffController(val caffService: CaffService) {

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	fun all(): MutableIterable<Caff> {
		return caffService.findAll()
	}

}