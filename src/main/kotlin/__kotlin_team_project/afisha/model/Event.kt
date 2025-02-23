package __kotlin_team_project.afisha.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotBlank(message = "Название мероприятия не может быть пустым")
    @Column(nullable = false)
    var name: String = "",

    @field:NotBlank(message = "Категория не может быть пустой")
    @Column(nullable = false)
    var category: String = "",

    @Column(nullable = false)
    var description: String = "",

    @Column(nullable = false)
    var dateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var location: String = "",

    @Column(nullable = false)
    var price: Double = 0.0
) 