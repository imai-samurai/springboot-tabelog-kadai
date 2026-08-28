package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.AdminCategoryEditForm;
import com.example.nagoyameshi.form.AdminCategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;

@Service
public class AdminCategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public AdminCategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	//登録
	@Transactional
	public void create(AdminCategoryRegisterForm adminCategoryRegisterForm) {
		
		Category category = new Category();
		
		category.setName(adminCategoryRegisterForm.getName());
		
		categoryRepository.save(category);
	}
	
	//編集
	@Transactional
	public void update(AdminCategoryEditForm adminCategoryEditForm) {
		
		Category category = categoryRepository.getReferenceById(adminCategoryEditForm.getId());
		
		category.setName(adminCategoryEditForm.getName());
		
		categoryRepository.save(category);
		
	}
	
}
