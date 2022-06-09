package deep.jira.model

import org.bson.types.ObjectId

class Role(
    var id: String,
    var name: String,
    var isGeneralStatisticAvailable: Boolean,
    var isProcessCreatorAvailable: Boolean,
    var isJiraAvailable: Boolean,
    var isAddingStaffAvailable: Boolean,
    var companyToken: String = ""
)