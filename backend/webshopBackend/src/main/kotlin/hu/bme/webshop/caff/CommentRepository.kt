package hu.bme.webshop.caff

import hu.bme.webshop.models.Comment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: CrudRepository<Comment, Long> {
}