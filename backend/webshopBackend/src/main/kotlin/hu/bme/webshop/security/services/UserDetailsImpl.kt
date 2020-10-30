package hu.bme.webshop.security.services

import com.fasterxml.jackson.annotation.JsonIgnore
import hu.bme.webshop.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

class UserDetailsImpl(
	val id: Long,
	private val username: String,
	@field:JsonIgnore
	private val password: String,
	private val authorities: Collection<GrantedAuthority>
) : UserDetails {

	override fun getAuthorities() = authorities

	override fun getPassword() = password

	override fun getUsername() = username

	override fun isAccountNonExpired() = true

	override fun isAccountNonLocked() = true

	override fun isCredentialsNonExpired() = true

	override fun isEnabled() = true

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val user = other as UserDetailsImpl
		return Objects.equals(id, user.id)
	}

	companion object {
		private const val serialVersionUID = 1L
		fun build(user: User): UserDetailsImpl {
			val authorities: List<GrantedAuthority> = user.getRoles().stream()
				.map { role -> SimpleGrantedAuthority(role!!.getName()!!.name) }
				.collect(Collectors.toList())
			return UserDetailsImpl(
				user.id,
				user.username,
				user.password,
				authorities
			)
		}
	}

}