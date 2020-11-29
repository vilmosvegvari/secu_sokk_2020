package hu.bme.webshop.models

import javax.persistence.*

@Entity
@Table(name = "roles")
class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private var name: ERole? = null

	constructor()
	constructor(name: ERole?) {
		this.name = name
	}

	fun getName(): ERole? {
		return name
	}

	fun setName(name: ERole?) {
		this.name = name
	}
}