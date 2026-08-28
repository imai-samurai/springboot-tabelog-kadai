package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.StoreRepository;

@Controller
public class HomeController {
	
	private final StoreRepository storeRepository;
	
	public HomeController(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	//トップページ
	@GetMapping("/")
	public String index(Model model) {
		
		List<Store> stores = storeRepository.findTop5ByOrderByIdAsc();
		
		model.addAttribute("stores", stores);
		
		return "index";
	}

}
