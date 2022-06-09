package deep.jira.controller

import deep.jira.dto.TaskCreateDTO
import deep.jira.dto.TaskParametersDTO
import deep.jira.service.TaskService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import java.net.http.HttpRequest
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/task")
class TaskController(
    private val taskService: TaskService
) {
    @PostMapping(path=["/create"])
    fun create(@RequestBody taskCreateDTO: TaskCreateDTO) = taskService.createTask(taskCreateDTO)

    @PostMapping(path=["/change-status"])
    fun changeTaskParameters(@RequestBody taskParametersDTO: TaskParametersDTO) = taskService.changeTaskParameters(taskParametersDTO)

    @GetMapping(path=["/get-all/{token}"])
    fun getAllTasks(@PathVariable token: String) = taskService.getAllTasks(token)

    @GetMapping(path=["/get-all/current-user"])
    fun getAllTaskForCurrentUser(request: HttpServletRequest) = taskService.getAllTaskForCurrentUser(request)

}