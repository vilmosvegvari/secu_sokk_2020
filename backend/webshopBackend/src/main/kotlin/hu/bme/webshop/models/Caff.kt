package hu.bme.webshop.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Caff")
@Table(name = "caffs")
class Caff (
	val name: String,
	val date: LocalDateTime,
	val creator: String
) {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	var id: Long = 0

	@OneToMany(
		mappedBy = "caff",
		cascade = [CascadeType.ALL]
	)
	private var tags = mutableListOf<Tag>()
}
