package com;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice // <-- Annotation này giúp nó chạy trên tất cả các trang
public class Name {

    @ModelAttribute("username")
    public String getUsername(HttpSession session) {
        Object user = session.getAttribute("user");
        return (user != null) ? user.toString() : "";
    }

    @ModelAttribute("role")
    public String getRole(HttpSession session) {
        Object role = session.getAttribute("role");
        return (role != null) ? role.toString() : "";
    }
}