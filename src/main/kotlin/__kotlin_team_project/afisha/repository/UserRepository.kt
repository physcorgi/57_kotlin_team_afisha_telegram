package __kotlin_team_project.afisha.repository

import __kotlin_team_project.afisha.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    override fun existsById(userId: Long): Boolean
    override fun findById(userId: Long): Optional<UserEntity>
}