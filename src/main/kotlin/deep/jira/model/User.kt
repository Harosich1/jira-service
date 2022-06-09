package deep.jira.model

class User (
    var id: String,
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var code: String,
    var role: Role,
    var companyToken: String? = ""
)