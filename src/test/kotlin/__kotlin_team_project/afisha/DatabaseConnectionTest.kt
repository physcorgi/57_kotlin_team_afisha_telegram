package __kotlin_team_project.afisha

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import javax.sql.DataSource

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Test
    fun `test database connection`() {
        val connection = dataSource.connection
        assert(connection.isValid(1))
        connection.close()
    }
} 