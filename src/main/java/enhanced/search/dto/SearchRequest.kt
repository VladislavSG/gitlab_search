package enhanced.search.dto

data class SearchRequest(
    //TODO: type of request from enum {project, issue, merge request, milestones, users}
    var searchString: String = "",
    var groupId: Long? = null,
    var groupType: String? = null,
    var projectId: Long? = null,
    var branches: Set<Long> = setOf(),
    var branchMask: String = ""
)