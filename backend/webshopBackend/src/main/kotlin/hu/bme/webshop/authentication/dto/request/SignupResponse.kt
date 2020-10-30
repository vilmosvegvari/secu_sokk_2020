package hu.bme.webshop.authentication.dto.request

import hu.bme.webshop.models.User

class SignupResponse {
	var user: User? = null
	var message: String? = null
}