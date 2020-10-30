package hu.bme.webshop.user

import hu.bme.webshop.models.User

interface IUserService {
	fun findAll(): MutableIterable<User>
	fun findById(userId: Long): User?
	fun me(): User?
	fun delete(userId: Long): Boolean
}