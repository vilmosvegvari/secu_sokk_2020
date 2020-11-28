package hu.bme.webshop.upload

import com.fasterxml.jackson.databind.ObjectMapper
import hu.bme.webshop.caff.CaffRepository
import hu.bme.webshop.models.ECaffStatus
import hu.bme.webshop.models.ETagType
import hu.bme.webshop.models.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Service
class CaffParserService {

    @Autowired
    private val caffRepository: CaffRepository? = null

    @Value("\${webshop.app.parserExecutable}")
    private val parser: String? = null

    @Async
    fun processCaff(filename: String) {

        //Call native parser
        val result = parseCaff(filename)

        //Get caff stored in db
        val caff = caffRepository!!.findByFilename(filename).get()

        //If parsing fails update caff status
        if (result != 0) {
            caff.status = ECaffStatus.BAD_FILE
            caffRepository.save(caff)
            return
        }

        //Update caff based on parsed JSON
        processParsedJSON(filename)?.also {
            caff.apply {
                creator = it.creator
                date = it.getLocalDateTime()
                status = ECaffStatus.OK
                numAnim = it.ciff_s.size.toLong()
                tags = ArrayList()

                for (ciff in it.ciff_s) {

                    if (!tags.any { it.name == ciff.caption }) {
                        tags.add(Tag(name = ciff.caption, caff = this, type = ETagType.CAPTION))
                    }

                    for (tag in ciff.tags) {
                        if (!tags.any { it.name == tag }) {
                            tags.add(Tag(name = tag, caff = this, type = ETagType.TAG))
                        }
                    }
                }
            }
        }
        caffRepository.save(caff)
    }

    fun parseCaff(filename: String): Int {
        val cmd = "$parser -f ${FileSystemStorageService.caffLocation}/${filename}.caff -o ${FileSystemStorageService.generatedLocation}"

        val parts = cmd.split("\\s".toRegex())

        val proc = try {
            ProcessBuilder(*parts.toTypedArray())
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start()
        } catch (e: IOException) {
            println("Error running parser! Check if parser executable is set properly!(current: $parser)")
            return -1
        }

        proc.waitFor(1, TimeUnit.MINUTES)
        return proc.exitValue()
    }

    fun processParsedJSON(filename: String): RawCaff? {
        val file = Paths.get("${FileSystemStorageService.generatedLocation}/${filename}.json").toFile()
        return if(file.canRead()){
            val json = file.readText()
            val objectMapper = ObjectMapper()
            objectMapper.readValue(json, RawCaff::class.java)
        } else {
            null
        }

    }
}