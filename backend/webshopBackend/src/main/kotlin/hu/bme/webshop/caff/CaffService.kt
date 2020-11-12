package hu.bme.webshop.caff

import hu.bme.webshop.models.Caff
import org.springframework.stereotype.Service

@Service
class CaffService(
	val caffRepository: CaffRepository
	//val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
) {

	fun findAll(): MutableIterable<Caff> {
		//logger.info("UserId ${userService.getUser().id} get all users")
		return caffRepository.findAll()
	}

	fun findById(userId: Long): Caff? {
		//logger.info("UserId ${userService.getUser().id} get userId ${userId} by id")
		return caffRepository.findById(userId).get()
	}

	fun delete(userId: Long): Boolean {
		return false
	}
}