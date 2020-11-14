package hu.bme.webshop.security.jwt

import hu.bme.webshop.security.services.UserDetailsImpl
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.SignatureException
import java.util.*

@Component
class JwtUtils {
	@Value("\${webshop.app.jwtSecret}")
	private val jwtSecret: String? = null

	@Value("\${webshop.app.jwtExpirationMs}")
	private val jwtExpirationMs = 0
	fun generateJwtToken(authentication: Authentication): Pair<String, Date> {
		val userPrincipal = authentication.principal as UserDetailsImpl
		val expirationDate = Date(Date().time + jwtExpirationMs)
		return Pair<String, Date>(Jwts.builder()
			.setSubject(userPrincipal.username)
			.setIssuedAt(Date())
			.setExpiration(expirationDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact(), expirationDate)
	}

	fun getUserNameFromJwtToken(token: String?): String {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
	}

	fun validateJwtToken(authToken: String?): Boolean {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
			return true
		} catch (e: SignatureException) {
			logger.error("Invalid JWT signature: {}", e.message)
		} catch (e: MalformedJwtException) {
			logger.error("Invalid JWT token: {}", e.message)
		} catch (e: UnsupportedJwtException) {
			logger.error("JWT token is unsupported: {}", e.message)
		} catch (e: IllegalArgumentException) {
			logger.error("JWT claims string is empty: {}", e.message)
		}
		//TODO:  catch (e: ExpiredJwtException) {
		//			logger.error("JWT token is expired: {}", e.message)
		//		}
		return false
	}

	companion object {
		private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
	}
}