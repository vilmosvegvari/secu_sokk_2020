package hu.bme.webshop.caff

import hu.bme.webshop.models.Caff
import hu.bme.webshop.models.Comment
import hu.bme.webshop.models.ECaffStatus
import hu.bme.webshop.models.User
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CaffService(
        val caffRepository: CaffRepository,
        val commentRepository: CommentRepository
        //val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
) {

    fun findAll(): MutableIterable<Caff> {
        //logger.info("UserId ${userService.getUser().id} get all users")
        return caffRepository.findAll()
    }

    fun findById(caffId: Long): Caff? {
        //logger.info("UserId ${userService.getUser().id} get userId ${userId} by id")

        return if(caffRepository.existsById(caffId)){
            val caff = caffRepository.findById(caffId).get()
            return if (caff.status == ECaffStatus.OK){
                caff
            } else {
                null
            }
        } else {
            null
        }
    }

    fun addComment(caffId: Long, user: User, message: String): Boolean {
        if (caffRepository.existsById(caffId)) {
            val caff = caffRepository.findById(caffId).get()
            caff.comments.add(Comment(user = user, date = LocalDateTime.now(), message = message, caff = caff))
            caffRepository.save(caff)
            return true
        }
        return false
    }

    fun deleteCommentIfSameUser(caffId: Long, commentId: Long, user: User): Boolean {
        if (commentRepository.existsById(commentId)) {
            val comment = commentRepository.findById(commentId).get()
            if (comment.caff.id == caffId && comment.user == user){
                commentRepository.delete(comment)
                return true
            }
        }
        return false
    }

    fun deleteIfSameUser(caffId: Long, user: User): Boolean {
        if (caffRepository.existsById(caffId) && caffRepository.findById(caffId).get().user == user) {
            caffRepository.deleteById(caffId)
            return true
        }
        return false
    }

    fun delete(caffId: Long): Boolean {
        if (caffRepository.existsById(caffId)) {
            caffRepository.deleteById(caffId)
            return true
        }
        return false
    }
}