package hu.bme.webshop.security.jwt

import hu.bme.webshop.security.services.UserDetailsServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthTokenFilter : OncePerRequestFilter() {
	@Autowired
	private val jwtUtils = JwtUtils()

	@Autowired
	private val userDetailsService = UserDetailsServiceImpl()

	@Throws(ServletException::class, IOException::class)
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		try {
			val jwt = parseJwt(request)
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				val username = jwtUtils.getUserNameFromJwtToken(jwt)
				val userDetails = userDetailsService.loadUserByUsername(username)
				val authentication = UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.authorities
				)
				authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
				SecurityContextHolder.getContext().authentication = authentication
			}
		} catch (e: Exception) {
			Companion.logger.error("Cannot set user authentication: {}", e)
		}
		filterChain.doFilter(request, response)
	}

	private fun parseJwt(request: HttpServletRequest): String? {
		val headerAuth = request.getHeader("Authorization")
		return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			headerAuth.substring(7, headerAuth.length)
		} else null
	}

	companion object {
		private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)
	}
}