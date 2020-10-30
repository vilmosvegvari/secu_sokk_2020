package hu.bme.webshop.authentication.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class SignupRequest {
	var username: @NotBlank @Size(min = 3, max = 20) String? = null
	var password: @NotBlank @Size(min = 6, max = 40) String? = null
}