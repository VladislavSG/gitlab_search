package enhanced.search.dto

//import org.gitlab4j.api.Constants

data class SearchRequest(
    //var type: Constants.SearchScope = Constants.SearchScope.MERGE_REQUESTS,
    var searchString: String = "",
    var groupId: Long = -1L,
    var groupType: String? = null,
    var projectId: Long = -1L,
    var branches: Set<String> = setOf(),
    var branchMask: String = ""
) {
    fun isGlobal() : Boolean {
        return groupId == -1L ||
                groupType == null ||
                projectId == -1L
    }
}