package hu.bme.webshop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Caff")
@Table(name = "caffs")
class Caff(
		val name: String,
		var date: LocalDateTime? = null,
		var creator: String? = null,
		var status: ECaffStatus,
		val filename: String,
		var numAnim: Long = 0,
		val filesize: Long,
		@JsonIgnore
		@ManyToOne
		@JoinColumn(name = "user_id")
		val user: User
) {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	var id: Long = 0

	@OneToMany(
		mappedBy = "caff",
		cascade = [CascadeType.ALL]
	)
	var tags = mutableListOf<Tag>()

	@OneToMany(
		mappedBy = "caff",
		cascade = [CascadeType.ALL]
	)
	var comments = mutableListOf<Comment>()

	val thumbnailUrl: String
	get(){
		return "/download/thumbnail/$id"
	}

	val userName: String
	get(){
		return user.username
	}

	val userId: String
	get() {
		return "${user.id}"
	}
}
