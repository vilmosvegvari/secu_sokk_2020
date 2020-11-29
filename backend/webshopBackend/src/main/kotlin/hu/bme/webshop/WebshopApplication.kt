package hu.bme.webshop

import hu.bme.webshop.upload.FileSystemStorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication
@EnableAsync
class WebshopApplication(){

	@Bean
	fun init(fileSystemStorageService: FileSystemStorageService): CommandLineRunner{
		return CommandLineRunner {
//			fileSystemStorageService.deleteAll()
			fileSystemStorageService.init()
		}
	}
}

fun main(args: Array<String>) {
	runApplication<WebshopApplication>(*args)
}
