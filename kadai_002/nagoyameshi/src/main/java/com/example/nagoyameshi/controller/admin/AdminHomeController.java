package com.example.nagoyameshi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Controller
public class AdminHomeController {
	
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	
	public AdminHomeController(StoreRepository storeRepository, UserRepository userRepository) {
		this.storeRepository = storeRepository;
		this.userRepository = userRepository;
	}
	
	//管理者トップページ(集計機能もトップページに実装）
	@GetMapping("/admin")
	public String index(Model model) {
		
		//集計機能用
		long freeUser = userRepository.countByRoleId(1);
		long puremiumUser = userRepository.countByRoleId(2);
		long totalUser = freeUser + puremiumUser;
		long totalStore = storeRepository.count();
		long totalSales = puremiumUser * 300;
		
		model.addAttribute("freeUser", freeUser);
		model.addAttribute("puremiumUser", puremiumUser);
		model.addAttribute("totalUser", totalUser);
		model.addAttribute("totalStore", totalStore);
		model.addAttribute("totalSales", totalSales);
		
		return "admin/index";
	}
	
}
