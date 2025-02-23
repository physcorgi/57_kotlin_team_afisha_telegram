package __kotlin_team_project.afisha.controller

import __kotlin_team_project.afisha.entity.UserEntity
import __kotlin_team_project.afisha.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val userRepository: UserRepository) {

    @PostMapping("/register")
    fun register(@RequestParam userId: Long, @RequestParam username: String): ResponseEntity<String> {
        if (userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("Пользователь с таким ID уже существует")
        }
        val user = UserEntity(userId, username)
        userRepository.save(user)
        return ResponseEntity.ok("Пользователь успешно зарегистрирован")
    }

    @GetMapping("/user")
    fun getUser(@RequestParam userId: Long): ResponseEntity<UserEntity> {
        return userRepository.findById(userId)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/delete")
    fun deleteUser(@RequestParam userId: Long): ResponseEntity<String> {
        return if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId)
            ResponseEntity.ok("Пользователь успешно удален")
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