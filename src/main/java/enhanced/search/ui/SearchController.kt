package enhanced.search.ui

import enhanced.search.dto.*
import enhanced.search.service.GitlabGetService
import enhanced.search.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class SearchController(
    @Autowired private val gitlabGetService: GitlabGetService,
    @Autowired private val userService: UserService,
) {

    @GetMapping("/")
    fun search(model: Model): String {
        model.addAttribute("request", SearchRequest())
        model.addAttribute("groupList", listOf(ANY_GROUP).plus(gitlabGetService.getGroups()))
        model.addAttribute("groupTypeList", listOf(ANY_GROUP_TYPE).plus(gitlabGetService.getGroupTypes()))
        model.addAttribute("repositoryList", listOf(ANY_PROJECT).plus(gitlabGetService.getProjects()))
        model.addAttribute("branchList", listOf<Branch>())
        model.addAttribute("branchList", gitlabGetService.getBranches())

        return "search-main"
    }

    @PostMapping("/search")
    fun search(
        @ModelAttribute gotRequest: SearchRequest,
        model: Model
    ): String {
        println(gotRequest.toString())

        model.addAttribute("gotRequest", gotRequest)

        model.addAttribute("request", SearchRequest())
        model.addAttribute("groupList", listOf(ANY_GROUP).plus(gitlabGetService.getGroups()))
        model.addAttribute("groupTypeList", listOf(ANY_GROUP_TYPE).plus(gitlabGetService.getGroupTypes()))
        model.addAttribute("repositoryList", listOf(ANY_PROJECT).plus(gitlabGetService.getProjects()))
        model.addAttribute("branchList", listOf<Branch>())
        model.addAttribute("branchList", gitlabGetService.getBranches())

        return "search-results"
    }

}