package com.hainguyen.controller;

import com.hainguyen.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {
    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @RequestMapping("/")
    public String Index(@CookieValue(value = "setUser", defaultValue = "") String setUserLog, Model model) {
        Cookie cookie = new Cookie("setUser", setUserLog);
        model.addAttribute("cookieValue", cookie);
        return "login";
    }

    @PostMapping("/dologin")
    public String doLogin(@ModelAttribute("user") User user, @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletResponse response, HttpServletRequest request, Model model) {
        if (user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("123456")){
            setUser = user.getEmail();
            //create cookie
            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(10 * 10 * 10);
            response.addCookie(cookie);
            //get cookie
            Cookie[] cookies = request.getCookies();
            for (Cookie ck : cookies){
                //display only the cookie with the name 'setUser'
                if(ck.getName().equals("setUser")){
                    model.addAttribute("cookieValue", ck);
                    break;
                }else {
                    ck.setValue("");
                    model.addAttribute("cookieValue", ck);
                    break;
                }
            }
            model.addAttribute("message", "Login successful.");
        }else{
            user.setEmail("");
            Cookie cookie = new Cookie("setUser", setUser);
            model.addAttribute("cookieValue", cookie);
            model.addAttribute("message", "Login failed. Try again.");
        }
        return "login";
    }
}
