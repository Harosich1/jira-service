package deep.jira.repository

import deep.jira.model.Task
import deep.jira.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : MongoRepository<Task, Int>{
    fun findById(id: String): Task?
    fun findAllByCompanyToken(companyToken: String): List<Task>?
    fun findAllByUserContractorOrUserApplicant(userContractor: User): List<Task>?
}