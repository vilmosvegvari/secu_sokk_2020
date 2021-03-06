package hu.bme.webshop.models

import hu.bme.webshop.models.Role
import hu.bme.webshop.user.dto.UserResponse
import javax.persistence.*

@Entity(name = "User")
@Table(name = "users")
class User(
	val username: String,
	val password: String
) {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	var id: Long = 0

	var isDeleted: Boolean = false

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "user_roles",
		joinColumns = [JoinColumn(name = "user_id")],
		inverseJoinColumns = [JoinColumn(name = "role_id")]
	)
	private var roles: Set<Role> = HashSet<Role>()

	fun getRoles(): Set<Role> {
		return roles
	}

	fun setRoles(roles: Set<Role>) {
		this.roles = roles
	}

	fun toUserResponse(): UserResponse {
		val isAdmin = roles.any { it.getName()!! == ERole.ROLE_ADMIN }
		return UserResponse(
			id = id,
			isAdmin = isAdmin,
			username = username,
			isDeleted = isDeleted
		)
	}
}