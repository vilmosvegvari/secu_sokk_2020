package hu.bme.webshop.download

import hu.bme.webshop.caff.CaffRepository
import hu.bme.webshop.caff.CaffService
import hu.bme.webshop.models.Caff
import hu.bme.webshop.models.ECaffStatus
import hu.bme.webshop.security.services.UserDetailsProvider
import hu.bme.webshop.upload.FileSystemStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.nio.file.Paths
import javax.servlet.http.HttpServletResponse

@CrossOrigin(origins = ["\${webshop.app.origin}"])
@Controller
@RequestMapping("/api/download")
class DownloadController @Autowired constructor(
        private val userService: UserDetailsProvider,
        val caffService: CaffService
) {

    @GetMapping(
            value = ["/{id}"],
            produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @PreAuthorize("hasAnyRole('USER')")
    @ResponseBody
    fun downloadCaff(@PathVariable(value = "id") id: Long, response: HttpServletResponse): ByteArray? {
        val caff = caffService.findById(id)
        if(caff != null){
            val file = Paths.get("${FileSystemStorageService.caffLocation}/${caff.filename}.caff").toFile()
            if (file.canRead()){
                response.addHeader("Content-Disposition", "attachment; filename=${caff.name}.caff")
                return file.readBytes()
            }
        }

        response.status = 404
        return null
    }

    @GetMapping(
            value = ["/thumbnail/{id}"],
            produces = [MediaType.IMAGE_PNG_VALUE]
    )
    @ResponseBody
    fun downloadThumbnail(@PathVariable(value = "id") id: Long, response: HttpServletResponse): ByteArray? {
        val caff = caffService.findById(id)
        if(caff != null){
            val file = Paths.get("${FileSystemStorageService.generatedLocation}/${caff.filename}.png").toFile()
            if (file.canRead()){
                return file.readBytes()
            }
        }

        response.status = 404
        return null
    }

    @GetMapping(
            value = ["/gif/{id}"],
            produces = [MediaType.IMAGE_GIF_VALUE]
    )
    @ResponseBody
    fun downloadGif(@PathVariable(value = "id") id: Long, response: HttpServletResponse): ByteArray? {
        val caff = caffService.findById(id)
        if(caff != null){
            val file = Paths.get("${FileSystemStorageService.generatedLocation}/${caff.filename}.gif").toFile()
            if (file.canRead()){
                return file.readBytes()
            }
        }

        response.status = 404
        return null

    }
}