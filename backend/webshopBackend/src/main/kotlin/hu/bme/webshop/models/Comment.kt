package hu.bme.webshop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Comment")
@Table(name = "comments")
class Comment(
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,
        val date: LocalDateTime,
        val message: String,
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "caff_id", nullable = false)
        val caff: Caff
){

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    val userName: String
    get() = user.username
}