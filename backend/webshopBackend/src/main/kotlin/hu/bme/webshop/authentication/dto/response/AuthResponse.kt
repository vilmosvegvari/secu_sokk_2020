package hu.bme.webshop.authentication.dto.response

import java.util.*

class AuthResponse(
	var token: String,
	var id: Long,
	var username: String,
	val isAdmin: Boolean,
	val expirationDate: Date
)