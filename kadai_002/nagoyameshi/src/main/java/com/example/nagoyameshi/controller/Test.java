package com.example.nagoyameshi.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Admin;
import com.example.nagoyameshi.repository.AdminRepository;

@Controller
public class Test {
	
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Test(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	//管理者テストユーザーを直接作成
	@GetMapping("/fadfeagafdafdafafdafda/fdafgajghghghghghgggff/fhjkinmejqqqqhjukfnjhdhdhdh")
	public String test() {
		Admin admin = new Admin();
		
		String email = "admintest@example.com";
		String password = "password";
		
		admin.setEmail(email);
		admin.setPassword(passwordEncoder.encode(password));//パスワードをハッシュ化してセットする
		
		adminRepository.save(admin);
		
		return "index";
	}

}
