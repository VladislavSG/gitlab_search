package enhanced.search.ui

import enhanced.search.dto.SearchRequest
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
    fun search(
        model: Model
    ): String {
        model.addAttribute("request", SearchRequest())
        model.addAttribute("groupList", searchService.getGroups())
        model.addAttribute("groupTypeList", searchService.getGroupTypes())
        model.addAttribute("repositoryList", searchService.getRepositories())
        model.addAttribute("branchList", searchService.getBranches())

        return "search-main"
    }

    @PostMapping("/search")
    fun search(
        @ModelAttribute request: SearchRequest,
        model: Model
    ): String {
        model.addAttribute("request", request)
        println(request.toString())
        return "redirect:/"
    }

}