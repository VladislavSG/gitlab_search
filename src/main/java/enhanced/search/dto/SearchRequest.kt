package enhanced.search.dto

//import org.gitlab4j.api.Constants

data class SearchRequest(
    //var type: Constants.SearchScope = Constants.SearchScope.MERGE_REQUESTS,
    var searchString: String = "",
    var groupId: Long? = null,
    var groupType: String? = null,
    var projectId: Long? = null,
    var branches: Set<String> = setOf(),
    var branchMask: String = ""
)