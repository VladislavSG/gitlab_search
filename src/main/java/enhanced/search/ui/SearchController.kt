package enhanced.search.ui

import enhanced.search.dto.*
import enhanced.search.service.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class SearchController(
    @Autowired private val searchService: SearchService
) {

    @GetMapping("/")
    fun search(model: Model): String {

        model.addAttribute("request", SearchRequest())
        model.addAttribute("groupList", listOf(ANY_GROUP).plus(searchService.getGroups()))
        model.addAttribute("groupTypeList", listOf(ANY_GROUP_TYPE).plus(searchService.getGroupTypes()))
        model.addAttribute("repositoryList", listOf(ANY_PROJECT).plus(searchService.getProjects()))
        model.addAttribute("branchList", searchService.getBranches())

        return "search-main"
    }

    @PostMapping("/search")
    fun search(
        @ModelAttribute gotRequest: SearchRequest,
        model: Model
    ): String {
        model.addAttribute("gotRequest", gotRequest)

        model.addAttribute("request", SearchRequest())
        model.addAttribute("groupList", listOf(ANY_GROUP).plus(searchService.getGroups()))
        model.addAttribute("groupTypeList", listOf(ANY_GROUP_TYPE).plus(searchService.getGroupTypes()))
        model.addAttribute("repositoryList", listOf(ANY_PROJECT).plus(searchService.getProjects()))
        model.addAttribute("branchList", searchService.getBranches())

        println(gotRequest)
        model.addAttribute("request", gotRequest)
        println(gotRequest.toString())

        return "search-results"
    }

}