package hu.bme.webshop.caff

import hu.bme.webshop.authentication.dto.response.MessageResponse
import hu.bme.webshop.caff.dto.CaffDetail
import hu.bme.webshop.models.ERole
import hu.bme.webshop.models.ETagType
import hu.bme.webshop.security.services.UserDetailsProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["\${webshop.app.origin}"])
@RestController
@RequestMapping("/api/caff")
class CaffController(val caffService: CaffService, val userService: UserDetailsProvider) {
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun all(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(caffService.findAll())
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(MessageResponse(e.message!!))
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun delete(@PathVariable(value = "id") id: Long): ResponseEntity<Any> {
        val result: Boolean
        val currentUser = userService.getUser()
        if (currentUser.getRoles().any { it.getName() == ERole.ROLE_ADMIN }) {
            result = caffService.delete(id)
        } else {
            result = caffService.deleteIfSameUser(id, currentUser)
        }

        return if (result) {
            ResponseEntity.ok(mapOf("message" to "ok"))
        } else {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun details(@PathVariable(value = "id") id: Long): ResponseEntity<Any> {
        val caff = caffService.findById(id) ?: return ResponseEntity.notFound().build<Any>()

        val detailResponse = CaffDetail()
        val tags = ArrayList<String>()
        val captions = ArrayList<String>()

        caff.tags.forEach {
            when (it.type) {
				ETagType.TAG -> tags.add(it.name)
				ETagType.CAPTION -> captions.add(it.name)
            }
        }

        detailResponse.apply {
            this.id = caff.id
            name = caff.name
            creator = caff.creator ?: ""
            gif = "/download/gif/${caff.id}"
            download = "/download/${caff.id}"
            numAnim = caff.numAnim
            filesize = caff.filesize
            userName = caff.userName
            this.tags = tags
            this.captions = captions
            this.comments = caff.comments
        }

        return ResponseEntity.ok(detailResponse)
    }

    @PostMapping("/details/{id}/comment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun storeComment(@PathVariable(value = "id") id: Long, @RequestParam("message") message: String): ResponseEntity<Any> {
        val user = userService.getUser()

        return if (caffService.addComment(id, user, message)) {
            ResponseEntity.ok(mapOf("message" to "ok"))
        } else {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @DeleteMapping("/details/{caffId}/comment/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deleteComment(@PathVariable(value = "caffId") caffId: Long, @PathVariable(value = "commentId") commentId: Long): ResponseEntity<Any> {
        val result: Boolean
        val currentUser = userService.getUser()

        result = if (currentUser.getRoles().any { it.getName() == ERole.ROLE_ADMIN }) {
            caffService.deleteComment(caffId, commentId)
        } else {
            caffService.deleteCommentIfSameUser(caffId, commentId, currentUser)
        }

        return if (result) {
            ResponseEntity.ok(mapOf("message" to "ok"))
        } else {
            ResponseEntity.notFound().build<Any>()
        }
    }
}