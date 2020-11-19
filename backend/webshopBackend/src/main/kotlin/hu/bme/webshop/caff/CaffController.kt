package hu.bme.webshop.caff
import hu.bme.webshop.authentication.dto.response.MessageResponse
import hu.bme.webshop.caff.dto.CaffDetail
import hu.bme.webshop.models.ERole
import hu.bme.webshop.models.ETagType
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

	@GetMapping("/details/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	fun details(@PathVariable(value = "id") id: Long): ResponseEntity<Any>{
		val caff = caffService.findById(id) ?: return ResponseEntity.notFound().build<Any>()

		val detailResponse = CaffDetail()
		val tags = ArrayList<String>()
		val captions = ArrayList<String>()

		caff.tags.forEach{
			when(it.type){
				ETagType.TAG -> tags.add(it.name)
				ETagType.CAPTION -> captions.add(it.name)
			}
		}

		detailResponse.apply {
			this.id = caff.id
			name = caff.name
			creator = caff.creator ?: ""
			filesize = caff.filesize
			userName = userService.getUser().username
			this.tags = tags
			this.captions = captions
		}

		return ResponseEntity.ok(detailResponse)
	}

}