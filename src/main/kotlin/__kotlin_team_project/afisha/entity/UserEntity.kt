package __kotlin_team_project.afisha.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    
    @field:NotBlank(message = "Имя пользователя не может быть пустым")
    @Column(nullable = false)
    var firstName: String,
    
    @Column(unique = true)
    var username: String? = null,
    
    @Column
    var email: String? = null
)