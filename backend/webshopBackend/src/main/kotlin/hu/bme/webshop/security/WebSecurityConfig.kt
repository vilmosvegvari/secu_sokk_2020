package hu.bme.webshop.security

import hu.bme.webshop.security.jwt.AuthEntryPointJwt
import hu.bme.webshop.security.jwt.AuthTokenFilter
import hu.bme.webshop.security.services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
	@Autowired
	var userDetailsService: UserDetailsServiceImpl? = null

	@Autowired
	private val unauthorizedHandler: AuthEntryPointJwt? = null

	@Bean
	fun authenticationJwtTokenFilter(): AuthTokenFilter {
		return AuthTokenFilter()
	}

	@Throws(Exception::class)
	public override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
	}

	@Bean
	@Throws(Exception::class)
	override fun authenticationManagerBean(): AuthenticationManager {
		return super.authenticationManagerBean()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http.headers().frameOptions().sameOrigin() //TODO: remove in prod
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers("/h2-console/**").permitAll() //TODO: remove in prod
				.antMatchers("/api/download/gif/**").permitAll()
				.antMatchers("/api/download/thumbnail/**").permitAll()
			.anyRequest().authenticated()
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
	}
}