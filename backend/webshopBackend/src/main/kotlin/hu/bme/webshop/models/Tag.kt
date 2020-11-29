package hu.bme.webshop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import hu.bme.webshop.models.Role
import javax.persistence.*

@Entity(name = "Tag")
@Table(name = "tags")
class Tag(
	val name: String,
	val type: ETagType,
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "caff_id", nullable = false)
	val caff: Caff
) {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	var id: Long = 0

}