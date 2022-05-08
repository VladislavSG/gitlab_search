package enhanced.search.dto

import enhanced.search.utils.ScopeType

data class SearchRequest(
    var searchString: String = "",
    var groupId: Long = -1L,
    var groupType: String = "",
    var projectId: Long = -1L,
    var branches: Set<String> = setOf(),
    var branchMask: String = ""
) {
    fun getScope() : ScopeType {
        if (projectId != -1L) {
            return ScopeType.PROJECT
        }
        if (groupId != -1L) {
            return ScopeType.GROUP
        }
        if (groupType.isNotEmpty()) {
            return ScopeType.GROUP_TYPE
        }
        return ScopeType.GLOBAL
    }
}