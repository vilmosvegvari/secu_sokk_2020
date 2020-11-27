package hu.bme.webshop.upload

import hu.bme.webshop.caff.CaffRepository
import hu.bme.webshop.models.Caff
import hu.bme.webshop.models.ECaffStatus
import hu.bme.webshop.security.services.UserDetailsProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@CrossOrigin(origins = ["\${webshop.app.origin}"])
@Controller
@RequestMapping("/api/upload")
class UploadController @Autowired constructor(
        private val fileSystemStorageService: FileSystemStorageService,
        private val userService: UserDetailsProvider,
        private val caffParserService: CaffParserService,
        private val caffRepository: CaffRepository
) {

    @PutMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        //Store the file and get stored name
        val filesize = file.size
        val originalFilename = file.originalFilename!!.substringBeforeLast('.')
        val filename = fileSystemStorageService.storeCaff(file)

        //Store in db
        val caff = Caff(name = originalFilename, status = ECaffStatus.PROCESSING, filename = filename, filesize = filesize, user = userService.getUser())
        caffRepository.save(caff)

        //Start parser service
        caffParserService.processCaff(filename)

        //Return response
        return ResponseEntity.ok(caff)
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException?): ResponseEntity<*>? {
        return ResponseEntity.notFound().build<Any>()
    }
}