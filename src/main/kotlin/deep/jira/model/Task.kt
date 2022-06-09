package deep.jira.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "TASK")
class Task(
    var name: String,
    var description: String,
    var userApplicant: User,
    var userContractor: User,
    var status: String,
    var companyToken: String
) {
    @Id
    var id: String = ObjectId.get().toString()
}