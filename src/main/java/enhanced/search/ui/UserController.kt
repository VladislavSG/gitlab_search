package enhanced.search.ui

import enhanced.search.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal

@Controller
class UserController(
    @Autowired private val userService: UserService
) {

    @GetMapping("/login")
    fun login(
        principal: Principal?
    ): String {
        return if (principal == null)
            "login"
        else
            "redirect:/"
    }

    @PostMapping("/logout")
    fun login(): String {
        return "redirect:/"
    }

    @PostMapping("/newuser")
    fun newUser(
    ): String {
        return "newuser"
    }

    @PostMapping("/newuser/create")
    fun newUser(
        @RequestParam("uid") uid: String,
        @RequestParam("password") password: String,
        @RequestParam("firstname") firstname: String,
        @RequestParam("lastname") lastname: String,
    ): String {
        userService.create(uid, firstname, lastname, password)
        return "redirect:/"
    }

}