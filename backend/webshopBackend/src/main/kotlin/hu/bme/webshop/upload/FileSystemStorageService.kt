package hu.bme.webshop.upload

import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileSystemStorageService{
    val caffLocationPath: Path = Path.of(caffLocation)
    val generatedLocationPath: Path = Path.of(generatedLocation)

    companion object {
        const val caffLocation: String = "files/caff"
        const val generatedLocation: String = "files/generated"
    }

    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun storeCaff(file: MultipartFile?): String {
        val filename = (1..20)
                .map { kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

        try {
            if (file!!.isEmpty) {
                throw StorageException("Failed to store empty file.")
            }

            val destinationFile: Path = caffLocationPath.resolve(
                    Paths.get("${filename}.caff"))
                    .normalize().toAbsolutePath()

            if (destinationFile.parent != caffLocationPath.toAbsolutePath()) {
                // This is a security check
                throw StorageException(
                        "Cannot store file outside current directory.")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING)
            }

        } catch (e: IOException) {
            throw StorageException("Failed to store file.", e)
        }

        return filename
    }

    fun deleteAll() {
        FileSystemUtils.deleteRecursively(caffLocationPath.toFile())
        FileSystemUtils.deleteRecursively(generatedLocationPath.toFile())
    }

    fun init() {
        try {
            if (!Files.exists(caffLocationPath)) Files.createDirectories(caffLocationPath)
            if (!Files.exists(generatedLocationPath)) Files.createDirectories(generatedLocationPath)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }

}