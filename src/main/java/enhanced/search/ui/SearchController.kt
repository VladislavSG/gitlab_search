package enhanced.search.ui

import enhanced.search.dto.*
import enhanced.search.service.GitlabGetService
import enhanced.search.service.SearchService
import enhanced.search.service.UserService
import enhanced.search.utils.findById
import enhanced.search.utils.getFullName
import enhanced.search.utils.makeResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import java.security.Principal
import java.util.*
import kotlin.concurrent.thread

@Controller
class SearchController(
    @Autowired private val userService: UserService,
    @Autowired private val gitlabGetService: GitlabGetService,
    @Autowired private val searchService: SearchService
) {

    @GetMapping("/")
    fun search(
        model: Model,
        principal: Principal
    ): String {
        val user = userService.search(principal.name)!!
        gitlabGetService.initToken(user.accessToken)

        val groupTypes = gitlabGetService.groupTypes
        val groups = gitlabGetService.groups
        val projects = gitlabGetService.projects
        val branches = gitlabGetService.branches

        updateModel(model, groupTypes, groups, projects, branches, user.getFullName())

        return "search-main"
    }

    @GetMapping("/search")
    fun search(
        @ModelAttribute gotRequest: SearchRequest,
        model: Model,
        principal: Principal
    ): String {
        val user = userService.search(principal.name)!!
        gitlabGetService.initToken(user.accessToken)
        searchService.initToken(user.accessToken)

        val groupTypes = Collections.synchronizedList(gitlabGetService.groupTypes)
        val groups = Collections.synchronizedList(gitlabGetService.groups)
        val projects = Collections.synchronizedList(gitlabGetService.projects)
        val branches = Collections.synchronizedList(gitlabGetService.branches)

        model.addAttribute("gotRequest", gotRequest)

        updateModel(model, groupTypes, groups, projects, branches, user.getFullName())

        val issuesThread = thread {
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

            model.addAttribute("issuesList", issuesList)
        }

        val mergeRequestThread = thread {
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
            model.addAttribute("mergeRequestList", mergeRequestList)
        }

        val blobThread = thread {
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
            model.addAttribute("blobList", blobList)
        }

        val wikiThread = thread {
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
            model.addAttribute("wikiList", wikiList)
        }

        val commitThread = thread {
            val commitList = searchService.searchCommits(gotRequest)
                .map {
                    val project = projects.findById(1) //fixme
                    val group = groups.findById(project.parentId)
                    val groupType = groupTypes.findById(group.parentId)
                    it.makeResponse(
                        groupType?.name ?: "unknown",
                        group.name,
                        project.name
                    )
                }

            model.addAttribute("commitList", commitList)
        }

        val commentThread = thread {
            val commentList = searchService.searchComments(gotRequest)
                .map {
                    it.makeResponse(
                        it.noteableType
                    )
                }
            model.addAttribute("commentList", commentList)
        }

        val milestoneThread = thread {
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
            model.addAttribute("milestoneList", milestoneList)
        }

        val userThread = thread {
            val userList = searchService.searchUsers(gotRequest)
                .map {
                    it.makeResponse(
                        it.location ?: ""
                    )
                }
            model.addAttribute("userList", userList)
        }

        issuesThread.join()
        mergeRequestThread.join()
        blobThread.join()
        wikiThread.join()
        commitThread.join()
        commentThread.join()
        milestoneThread.join()
        userThread.join()

        return "search-results"
    }

    private fun updateModel(
        model: Model,
        groupTypeList: List<GroupType>,
        groupList: List<Group>,
        projectList: List<Project>,
        branchList: List<Branch>,
        fullName: String,
    ) {
        model.addAttribute("request", SearchRequest())

        model.addAttribute("userFullName", fullName)

        model.addAttribute("anyGroupType", ANY_GROUP_TYPE)
        model.addAttribute("anyGroup", ANY_GROUP)
        model.addAttribute("anyProject", ANY_PROJECT)

        model.addAttribute("groupTypeList", groupTypeList)
        model.addAttribute("groupList", groupList.groupBy { groupTypeList.findById(it.parentId)?.name ?: "unknown" })
        model.addAttribute("repositoryList", projectList.groupBy { groupList.findById(it.parentId).name })
        model.addAttribute("branchList", branchList.groupBy { projectList.findById(it.parentId).name })
    }

}