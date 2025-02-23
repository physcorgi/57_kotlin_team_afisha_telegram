package __kotlin_team_project.afisha.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserDto(
    @field:NotBlank(message = "Имя пользователя не может быть пустым")
    val username: String,
    
    @field:NotBlank(message = "Имя не может быть пустым")
    val firstName: String,
    
    @field:Email(message = "Некорректный email адрес")
    val email: String?
) 