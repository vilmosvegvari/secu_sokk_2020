package hu.bme.webshop.user.dto

import java.time.LocalDateTime

class UserResponse (
	val id: Long,
	val isAdmin: Boolean,
	val isDeleted: Boolean,
	val username: String
)