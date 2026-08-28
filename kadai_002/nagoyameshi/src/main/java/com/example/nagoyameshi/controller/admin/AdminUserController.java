package com.example.nagoyameshi.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;

@Controller
public class AdminUserController {
	
	private final UserRepository userRepository;
	
	public AdminUserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	//ユーザー一覧
	@GetMapping("/admin/users")
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@PageableDefault(page = 0, size = 15, sort = "id", direction = Direction.ASC) Pageable pageable,
						Model model)
	{
		
		Page<User> userPage;
		
		if(keyword != null && !keyword.isEmpty()) {
			
			userPage = userRepository.findByEmailLike("%" + keyword + "%", pageable);
			
		} else {
			
			userPage = userRepository.findAll(pageable);
			
		}
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("userPage", userPage);
		return "admin/user/index";
	}
	
}
