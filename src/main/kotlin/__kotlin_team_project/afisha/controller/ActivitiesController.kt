package __kotlin_team_project.afisha.controller

import __kotlin_team_project.afisha.repository.EventRepository
import __kotlin_team_project.afisha.model.Event
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/events")
class ActivitiesController(private val eventRepository: EventRepository) {

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<Event>> {
        val events = eventRepository.findAll()
        return if (events.isNotEmpty()) {
            ResponseEntity(events, HttpStatus.OK)
        } else {
            ResponseEntity(emptyList(), HttpStatus.OK)
        }
    }

    @PostMapping
    fun createEvent(@Valid @RequestBody event: Event): ResponseEntity<Event> {
        return try {
            ResponseEntity(eventRepository.save(event), HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/search/by-name")
    fun searchByName(@RequestParam name: String): ResponseEntity<List<Event>> {
        if (name.isBlank()) {
            return ResponseEntity.badRequest().build()
        }
        val events = eventRepository.findByNameContainingIgnoreCase(name)
        return if (events.isNotEmpty()) ResponseEntity.ok(events) else ResponseEntity.notFound().build()
    }

    @GetMapping("/search/by-category")
    fun searchByCategory(@RequestParam category: String): ResponseEntity<List<Event>> {
        if (category.isBlank()) {
            return ResponseEntity.badRequest().build()
        }
        val events = eventRepository.findByCategoryContainingIgnoreCase(category)
        return if (events.isNotEmpty()) ResponseEntity.ok(events) else ResponseEntity.notFound().build()
    }
}