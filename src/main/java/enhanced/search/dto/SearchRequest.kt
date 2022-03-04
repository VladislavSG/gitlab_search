package enhanced.search.dto

data class SearchRequest(
    var searchString: String = "",
    var useRegExp: Boolean = false,
    var groupId: Long = -1,
    var groupTypeId: Long = -1,
    var repositoryId: Long = -1,
    var branches: Set<Long> = setOf()
)