package hu.bme.webshop.authentication.dto.response

class JwtResponse(var accessToken: String, var id: Long, var username: String, val roles: List<String>) {
	var tokenType = "Bearer"
}