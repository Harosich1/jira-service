package deep.jira.dto

import deep.jira.model.User

class TaskCreateDTO(
    var name: String,
    var description: String,
    var userApplicant: User,
    var userContractor: User,
    var status: String,
    var companyToken: String
)