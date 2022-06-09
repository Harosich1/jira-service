package deep.jira.service

import deep.jira.model.exception.AlreadyHasNameException
import deep.jira.dto.TaskParametersDTO
import deep.jira.dto.TaskCreateDTO
import deep.jira.model.Task
import deep.jira.model.User
import deep.jira.model.exception.ObjectDoNotExists
import deep.jira.repository.TaskRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

interface TaskService {
    fun createTask(taskCreateDTO: TaskCreateDTO)
    fun changeTaskParameters(taskParametersDTO: TaskParametersDTO)
    fun getAllTasks(token: String): List<Task>
    fun getAllTaskForCurrentUser(request: HttpServletRequest): List<Task>
}

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val authenticationBaseUrl: String,
    private val restTemplate: RestTemplate
) : TaskService {

    override fun createTask(taskCreateDTO: TaskCreateDTO) {
        taskRepository.save(Task(
            taskCreateDTO.name,
            taskCreateDTO.description,
            taskCreateDTO.userApplicant,
            taskCreateDTO.userContractor,
            taskCreateDTO.status,
            taskCreateDTO.companyToken
        ))
    }

    override fun changeTaskParameters(taskParametersDTO: TaskParametersDTO) {
        taskRepository.findById(taskParametersDTO.id).also   {
            task ->
                task ?: throw ObjectDoNotExists()

                taskParametersDTO.status?.let{ status ->
                    task.status = status
                }
                taskParametersDTO.userContractor?.let { user ->
                    task.userContractor = user
                }
                if(taskParametersDTO.description.isNotBlank()) task.description = taskParametersDTO.description
                taskRepository.save(task)
            }
        }

    override fun getAllTasks(token: String): List<Task> = taskRepository.findAllByCompanyToken(token) ?: emptyList()
    override fun getAllTaskForCurrentUser(request: HttpServletRequest): List<Task> {
        var listUsers: List<Task> = emptyList()

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + request.getHeader(HttpHeaders.AUTHORIZATION).split(" ".toRegex()).toTypedArray()[1].trim { it <= ' ' });
        val requestEntity: HttpEntity<Void> = HttpEntity(headers)

        try {
            val entity = restTemplate.exchange(
                "$authenticationBaseUrl/user/get-current-user",
                HttpMethod.GET,
                requestEntity,
                User::class.java
            )
            if(entity.statusCodeValue == 200) {
                entity.body?.let {
                    user ->
                    listUsers = taskRepository.findAllByUserContractorOrUserApplicant(user) ?: emptyList()
                }

            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.stackTrace.toString())
        }
        return listUsers
    }
}