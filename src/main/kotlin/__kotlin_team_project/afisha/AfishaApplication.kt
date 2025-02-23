package __kotlin_team_project.afisha

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class AfishaApplication

fun main(args: Array<String>) {
	runApplication<AfishaApplication>(*args)
}
