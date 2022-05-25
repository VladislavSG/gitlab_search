package enhanced.search.ui


import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest


@Controller
class MyErrorController : ErrorController {

    @RequestMapping("/error")
    fun handleError(
        request: HttpServletRequest,
        model: Model
    ): String {
        val statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)?.toString()?.toInt()
        val throwable = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) as Throwable?

        val status = HttpStatus.valueOf(statusCode ?: -1)

        model.addAttribute("status", "${status.value()} - ${status.reasonPhrase}")
        model.addAttribute("description", throwable?.toString() ?: "")

        return "error"
    }

}