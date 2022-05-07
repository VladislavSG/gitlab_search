package enhanced.search.ui

import enhanced.search.dto.*
import enhanced.search.service.GitlabGetService
import enhanced.search.service.SearchService
import enhanced.search.service.UserService
import enhanced.search.utils.findById
import enhanced.search.utils.makeResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class SearchController(
    @Autowired private val gitlabGetService: GitlabGetService,
    @Autowired private val userService: UserService,
) {

    @GetMapping("/")
    fun search(model: Model): String {
        val groupTypes = gitlabGetService.groupTypes
        val groups = gitlabGetService.groups
        val projects = gitlabGetService.projects
        val branches = gitlabGetService.branches

        updateModel(model, groupTypes, groups, projects, branches)

        return "search-main"
    }

    @GetMapping("/search")
    fun search(
        @ModelAttribute gotRequest: SearchRequest,
        model: Model
    ): String {
        println(gotRequest.toString())

        val groupTypes = gitlabGetService.groupTypes
        val groups = gitlabGetService.groups
        val projects = gitlabGetService.projects
        val branches = gitlabGetService.branches

        model.addAttribute("gotRequest", gotRequest)

        updateModel(model, groupTypes, groups, projects, branches)

        val searchService = SearchService()

        val issuesList = searchService.searchIssues(gotRequest)
            .map {
                val project = projects.findById(it.projectId)
                val group = groups.findById(project.parentId)
                val groupType = groupTypes.findById(group.parentId)
                it.makeResponse(
                    groupType?.name ?: "unknown",
                    group.name,
                    project.name
                )
            }

        val mergeRequestList = searchService.searchMergeRequest(gotRequest)
            .map {
                val project = projects.findById(it.projectId)
                val group = groups.findById(project.parentId)
                val groupType = groupTypes.findById(group.parentId)
                it.makeResponse(
                    groupType?.name ?: "unknown",
                    group.name,
                    project.name
                )
            }

        val blobList = searchService.searchBlobs(gotRequest)
            .map {
                val project = projects.findById(it.projectId)
                val group = groups.findById(project.parentId)
                val groupType = groupTypes.findById(group.parentId)
                it.makeResponse(
                    groupType?.name ?: "unknown",
                    group.name,
                    project.name
                )
            }

        val wikiList = searchService.searchWiki(gotRequest)
            .map {
                val project = projects.findById(it.projectId)
                val group = groups.findById(project.parentId)
                val groupType = groupTypes.findById(group.parentId)
                it.makeResponse(
                    groupType?.name ?: "unknown",
                    group.name,
                    project.name
                )
            }

        val commitList = searchService.searchCommits(gotRequest)
            .map {
                println(it.parentIds.joinToString(", "))
                val project = projects.findById(1) //fixme
                val group = groups.findById(project.parentId)
                val groupType = groupTypes.findById(group.parentId)
                it.makeResponse(
                    groupType?.name ?: "unknown",
                    group.name,
                    project.name
                )
            }

        val commentList = searchService.searchComments(gotRequest)
            .map {
                it.makeResponse(
                    it.noteableType
                )
            }

        val milestoneList = searchService.searchMilestone(gotRequest)
            .map {
                val project = projects.findById(it.projectId)
                val group = groups.findById(project.parentId)
                val groupType = groupTypes.findById(group.parentId)
                it.makeResponse(
                    groupType?.name ?: "unknown",
                    group.name,
                    project.name
                )
            }

        val userList = searchService.searchUsers(gotRequest)
            .map {
                it.makeResponse(
                    it.location ?: ""
                )
            }

        model.addAttribute("issuesList", issuesList)
        model.addAttribute("mergeRequestList", mergeRequestList)
        model.addAttribute("blobList", blobList)
        model.addAttribute("wikiList", wikiList)
        model.addAttribute("commitList", commitList)
        model.addAttribute("commentList", commentList)
        model.addAttribute("milestoneList", milestoneList)
        model.addAttribute("userList", userList)

        return "search-results"
    }

    private fun updateModel(
        model: Model,
        groupTypeList: List<GroupType>,
        groupList: List<Group>,
        projectList: List<Project>,
        branchList: List<Branch>,
    ) {


        model.addAttribute("request", SearchRequest())

        model.addAttribute("anyGroupType", ANY_GROUP_TYPE)
        model.addAttribute("anyGroup", ANY_GROUP)
        model.addAttribute("anyProject", ANY_PROJECT)

        model.addAttribute("groupTypeList", groupTypeList)
        model.addAttribute("groupList", groupList.groupBy { groupTypeList.findById(it.parentId)?.name ?: "unknown" })
        model.addAttribute("repositoryList", projectList.groupBy { groupList.findById(it.parentId).name })
        model.addAttribute("branchList", branchList.groupBy { projectList.findById(it.parentId).name })
    }

}