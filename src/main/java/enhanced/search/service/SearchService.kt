package enhanced.search.service

import enhanced.search.dto.Branch
import enhanced.search.dto.Group
import enhanced.search.dto.GroupType
import enhanced.search.dto.Repository
import org.springframework.stereotype.Service

@Service("searchService")
class SearchService {

    fun getGroups() = listOf(
        Group(1, "Group 1"),
        Group(2, "Group 2"),
        Group(3, "Group 3"),
        Group(4, "Group 4"),
    )

    fun getGroupTypes() = listOf(
        GroupType(1, "Group Type 1"),
        GroupType(2, "Group Type 2"),
        GroupType(3, "Group Type 3"),
        GroupType(4, "Group Type 4"),
    )

    fun getRepositories() = listOf(
        Repository(1, "Repository 1"),
        Repository(2, "Repository 2"),
        Repository(3, "Repository 3"),
        Repository(4, "Repository 4"),
    )

    fun getBranches() = listOf(
        Branch(1, "Branch 1"),
        Branch(2, "Branch 2"),
        Branch(3, "Branch 3"),
        Branch(4, "Branch 4"),
    )
}