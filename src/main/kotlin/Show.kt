package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun show(): Boolean {
    println("Input image file:")
    val imageName = readln()
    println("Password:")
    val password = readln()

    try {
        ImageIO.read(File(imageName))
    } catch (e: Exception) {
        println("File can not be opened")
        return false
    }

    val image = ImageIO.read(File(imageName))
    var message = showMessageFromImage(image)
    message = message.removeRange(message.lastIndex - 2, message.length)
    message = decryptMessage(message, password)

    println("Message:\n$message")

    return true
}

// Decrypt message with password
fun decryptMessage(message: String, password: String): String {
    return encryptMessage(message, password)
}

fun showMessageFromImage(image: BufferedImage): String {

    // Create array of bytes
    val bytes = mutableListOf<Byte>()
    val bits = mutableListOf<Char>()

    var j = 1
    var byte = 0
    Loop@ for (y in 0 until image.height) {
        for (x in 0 until image.width) {

            // Check if border (0, 0, 3)
            if (
                bytes.size >= 2 &&
                bytes[bytes.lastIndex] == 3.toByte() &&
                bytes[bytes.lastIndex - 1] == 0.toByte() &&
                bytes[bytes.lastIndex - 2] == 0.toByte()
            ) break@Loop

            byte += (Color(image.getRGB(x, y)).blue.and(1))
            bits.add(((Color(image.getRGB(x, y)).blue and 1) + '0'.code).toChar())
            if (j == 8) {
                bytes.add(byte.toByte())
                byte = 0
                j = 0
            }
            byte = byte shl 1
            j++
        }
    }

    return bytes.toByteArray().toString(Charsets.UTF_8)
}

