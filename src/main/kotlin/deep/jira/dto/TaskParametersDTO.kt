package deep.jira.dto

import deep.jira.model.User

class TaskParametersDTO(
    var id: String,
    var name: String,
    var status: String?,
    var userContractor: User?,
    var description: String = "",
    var companyToken: String
    )