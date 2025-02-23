package __kotlin_team_project.afisha.repository

import __kotlin_team_project.afisha.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findByNameContainingIgnoreCase(name: String): List<Event>
    fun findByCategoryContainingIgnoreCase(category: String): List<Event>
}
