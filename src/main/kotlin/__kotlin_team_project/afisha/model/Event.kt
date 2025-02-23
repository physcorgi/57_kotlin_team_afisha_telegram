package __kotlin_team_project.afisha.model

import java.time.LocalDateTime

data class Event(
    val id: Long = 0,
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var location: String = "",
    var price: Double = 0.0,
    var dateTime: LocalDateTime = LocalDateTime.now()
) 