package com;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Transfer {

    // Hàm kiểm tra nhanh xem đã đăng nhập chưa
    private boolean isNotLoggedIn(HttpSession session) {
        return session.getAttribute("user") == null;
    }

    // Hàm kiểm tra xem có phải Admin không
    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    // --- CÁC TRANG CHUNG (Ai login rồi cũng xem được) ---

    @GetMapping("/Dashboard")
    public String dashboard(HttpSession session) {
        if (isNotLoggedIn(session)) return "redirect:/login";
        return "homepage"; // Tên file HTML Dashboard của bạn
    }
    
    @GetMapping("/IO-status")
    public String activeAlarms(HttpSession session) {
        if (isNotLoggedIn(session)) return "redirect:/login";
        return "IO";
    }
    @GetMapping("/invt")
    public String invtStatus(HttpSession session) {
        if (isNotLoggedIn(session)) return "redirect:/login";
        return "INVT";
    }
    @GetMapping("/reports")
    public String reports(HttpSession session) {
        if (isNotLoggedIn(session)) return "redirect:/login";
        return "reports";
    }
    

    // --- CÁC TRANG CẦN BẢO MẬT (Chỉ Admin mới vào được) ---
    // Ví dụ: Recipes, Torque, INVT (Vì có nút điều khiển/chỉnh sửa)
    @GetMapping("/recipes")
    public String recipePage(HttpSession session, RedirectAttributes redirectAttributes) { // 1. Thêm tham số này
        if (isNotLoggedIn(session)) return "redirect:/login";
        
        // 2. Kiểm tra quyền
        if (!isAdmin(session)) {
            // 3. Gửi thông báo lỗi sang trang Dashboard
            redirectAttributes.addFlashAttribute("error_msg", "You do not have permission to access RECIPES!");
            return "redirect:/Dashboard"; 
        }
        
        return "recipes";
    }

    @GetMapping("/torque")
    public String torqueControl(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isNotLoggedIn(session)) return "redirect:/login";
        
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("error_msg", "You do not have permission to access TORQUE CONTROL!");
            return "redirect:/Dashboard";
        }
        
        return "Torque";
    }
    @GetMapping("/speed-monitoring")
    public String speedMonitoring(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isNotLoggedIn(session)) return "redirect:/login";
        
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("error_msg", "You do not have permission to access SPEED MONITORING!");
            return "redirect:/Dashboard";
        }
        
        return "Speed";
    }

    
}

    
    
        
       

