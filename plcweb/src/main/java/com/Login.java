package com;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Login {

    // 1. Vào trang gốc (/) thì hiện Landing Page
    @GetMapping("/")
    public String showLandingPage() {
        return "landing"; // Trả về file landing.html
    }
    
    // 2. Xử lý khi người dùng cố tình gõ /login trên thanh địa chỉ
    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        // Nếu đã đăng nhập rồi thì vào thẳng Dashboard
        if (session.getAttribute("user") != null) {
            return "redirect:/Dashboard";
        }
        // SỬA: Vì không còn trang login riêng, nên nếu chưa đăng nhập thì đá về trang chủ
        return "redirect:/"; 
    }

    // 3. Xử lý nút Đăng nhập (Method POST)
    @PostMapping("/login")
    public String processLogin(@RequestParam("Username") String username, 
                               @RequestParam("Password") String password, 
                               HttpSession session,
                               Model model) {

        // --- TRƯỜNG HỢP 1: ADMIN (Toàn quyền) ---
        if (("khang".equals(username) && "1072003".equals(password)) || 
            ("admin".equals(username) && "123".equals(password))) {
            
            session.setAttribute("user", username);
            session.setAttribute("role", "ADMIN");
            return "redirect:/Dashboard";
        }

        // --- TRƯỜNG HỢP 2: GUEST (Chỉ xem) ---
        else if ("guest".equals(username) && "123".equals(password)) {
            session.setAttribute("user", "Viewer");
            session.setAttribute("role", "GUEST");
            return "redirect:/Dashboard";
        }

        // --- TRƯỜNG HỢP 3: SAI MẬT KHẨU ---
        else {
            // Gửi thông báo lỗi xuống View
            model.addAttribute("error", "Username or Password is incorrect");
            
            // SỬA QUAN TRỌNG:
            // Trả về "landing" (file landing.html) để Thymeleaf nhận được biến error.
            // Bên file HTML đã có đoạn th:classappend="${error} ? 'active' : ''"
            // nên Modal sẽ tự động bật lên kèm dòng báo lỗi.
            return "landing"; 
        }
    }

    // 4. Xử lý Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa sạch session
        return "redirect:/";  // Quay về trang chủ
    }
}