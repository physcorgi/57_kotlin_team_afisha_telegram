package __kotlin_team_project.afisha.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController {

    private val users = mutableMapOf<Long, String>() // Временное хранилище пользователей

    @PostMapping("/register")
    // нужен id пользователя и как к нему обращаться (пример id = 777 username = 18yearsoldyandexcloudmiddlebackenddeveloper. FCS+CU 28')
    fun register(@RequestParam userId: Long, @RequestParam username: String): ResponseEntity<String> {
        if (users.containsKey(userId)) {
            return ResponseEntity.badRequest().body("User with that ID already exists")
        }
        users[userId] = username
        return ResponseEntity.ok("User successfully registered")
    }

    @GetMapping("/user")
    fun getUser(@RequestParam userId: Long): ResponseEntity<String> {
        return users[userId]?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/delete")
    fun deleteUser(@RequestParam userId: Long): ResponseEntity<String> {
        return if (users.remove(userId) != null) {
            ResponseEntity.ok("User deleted successfully")
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

/*
package kotlin_team_project.afisha.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val userRepository: UserRepository) {

    @PostMapping("/register")
    // нужен id пользователя и как к нему обращаться (пример id = 777 username = 18yearsoldyandexcloudmiddlebackenddeveloper. FCS+CU 28')
    fun register(@RequestParam userId: Long, @RequestParam username: String): ResponseEntity<String> {
        if (userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("User with that ID already exists")
        }
        val user = User(userId, username)
        userRepository.save(user)
        return ResponseEntity.ok("User successfully registered")
    }

    @GetMapping("/user")
    fun getUser(@RequestParam userId: Long): ResponseEntity<User> {
        return userRepository.findById(userId).orElseGet { ResponseEntity.notFound().build() }
    }

    @DeleteMapping("/delete")
    fun deleteUser(@RequestParam userId: Long): ResponseEntity<String> {
        return if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId)
            ResponseEntity.ok("User deleted successfully")
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
*/