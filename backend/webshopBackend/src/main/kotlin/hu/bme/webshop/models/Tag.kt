package hu.bme.webshop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import hu.bme.webshop.models.Role
import javax.persistence.*

@Entity(name = "Tag")
@Table(name = "tags")
class Tag(
	val name: String
) {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	var id: Long = 0

	@JsonIgnore
	@ManyToOne(cascade = [CascadeType.ALL])
	@JoinColumn(name = "caff_id")
	private var caff: Caff? = null
}