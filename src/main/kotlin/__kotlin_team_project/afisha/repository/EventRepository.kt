package __kotlin_team_project.afisha.repository

import __kotlin_team_project.afisha.model.Event
import org.springframework.stereotype.Repository

@Repository
class EventRepository {
    private val events = mutableListOf<Event>()

    fun findAll(): List<Event> = events.toList()

    fun findByNameContainingIgnoreCase(name: String): List<Event> =
        events.filter { it.name.contains(name, ignoreCase = true) }

    fun findByCategoryContainingIgnoreCase(category: String): List<Event> =
        events.filter { it.category.contains(category, ignoreCase = true) }

    fun save(event: Event) {
        events.add(event)
    }
}