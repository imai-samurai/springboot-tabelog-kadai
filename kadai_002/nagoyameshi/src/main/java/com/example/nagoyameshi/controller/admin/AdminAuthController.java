package com.example.nagoyameshi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminAuthController {
	
	//管理者ログイン
	@GetMapping("/admin/login")
	public String login() {
		
		return "admin/auth/login";
		
	}
	
}
