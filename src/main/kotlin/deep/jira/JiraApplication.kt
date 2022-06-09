package deep.jira

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JiraApplication

fun main(args: Array<String>) {
	runApplication<JiraApplication>(*args)
}
