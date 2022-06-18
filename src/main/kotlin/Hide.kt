package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.experimental.xor

// Hide text in image
// Return true if operation is successful
// Return false if exception has occurred
fun hide(): Boolean {

    // Read filenames
    val names = hideRead()
    val inputImageFileName = names[0]
    val outputImageFileName = names[1]
    var message = names[2]
    val password = names[3]

    // Encrypt message
    message = encryptMessage(message, password)

    // Check if input file exists and can be opened
    try {
        ImageIO.read(File(inputImageFileName))
    } catch (e: Exception) {
        println("Can't read input file")
        return false
    }

    // Open Image
    val image: BufferedImage = ImageIO.read(File(inputImageFileName))

    // Check if message can be stored in image
    if (message.toByteArray().size * 8 > image.width * image.height) {
        println("The input image is not large enough to hold this message.")
        return false
    }

    // Encrypt message and save file
    var success = true
    try {
        val outImage = hideMessageInImage(image, message)
        ImageIO.write(outImage, "png", File(outputImageFileName))
    } catch (e: Exception) {
        println(e.message)
        success = false
    }

    return if (success) {
        // Save image
        println("Message saved in ${File(outputImageFileName).name} image.")
        true
    } else {
        false
    }
}

fun hideRead(): Array<String> {

    // Read input file
    println("Input image file:")
    val inputImageFileName = readln()

    // Read output file
    println("Output image file:")
    val outputImageFileName = readln()

    // Read message to hide
    println("Message to hide:")
    val message = readln()

    // Read password
    println("Password:")
    val password = readln()

    return arrayOf(inputImageFileName, outputImageFileName, message, password)
}

// Encrypt message with password
fun encryptMessage(message: String, password: String): String {
    val messageBytes = message.toByteArray()
    val passBytes = password.toByteArray()

    for (i in messageBytes.indices) {
        messageBytes[i] = messageBytes[i] xor passBytes[i % passBytes.size]
    }

    return messageBytes.toString(Charsets.UTF_8)
}

// Hide messageInp in inpImage
fun hideMessageInImage(inpImage: BufferedImage, messageInp: String): BufferedImage {

    // Create blank image
    val image = BufferedImage(inpImage.width, inpImage.height, BufferedImage.TYPE_INT_RGB)

    // Copy inpImage to image
    val graphics = image.createGraphics()
    graphics.drawImage(inpImage, 0, 0, null)
    graphics.dispose()

    // Convert message to list of bits
    val bits = toBitsArray(messageInp)

    var j = 0
    Loop@ for (y in 0 until image.height) {
        for (x in 0 until image.width) {

            // Get pixel color
            val col = Color(image.getRGB(x, y))

            val r = col.red
            val g = col.green

            // col.blue and 11111110b + bits[j]
            val b = (col.blue and 1.inv()) + bits[j]

            // Set pixel color
            image.setRGB(x, y, Color(r, g, b).rgb)

            j++

            if (j == bits.size) break@Loop
        }
    }

    return image
}

// Convert String to List of Bits
fun toBitsArray(messageInp: String): MutableList<Byte> {

    // Add (0, 0, 3) to the end of message
    val message = messageInp + "\u0000\u0000\u0003"

    // Convert message to array of bytes
    val byteArr = message.toByteArray()

    // Convert message to binary
    val binaryArr = byteToBinary(byteArr)

    // Convert to array of bits
    val bits = mutableListOf<Byte>()
    binaryArr.forEach { str -> str.forEach { char -> bits.add(char.toString().toByte()) } }

    return bits
}

// Convert array of bytes to list of bytes in binary
fun byteToBinary(byteArr: ByteArray): MutableList<String> {
    val binaryArray = mutableListOf<String>()

    for (i in byteArr.indices) {
        val byte = byteArr[i]
        var str = byte.toString(2)
        str = str.padStart(8, '0')
        binaryArray.add(str)
    }

    return binaryArray
}





