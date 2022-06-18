package cryptography

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        when (val input = readln()) {
            "exit" -> {
                println("Bye!")
                break
            }
            "hide" -> {
                hide()
            }
            "show" -> {
                show()
            }
            else -> {
                println("Wrong task: $input")
            }
        }
    }
}

