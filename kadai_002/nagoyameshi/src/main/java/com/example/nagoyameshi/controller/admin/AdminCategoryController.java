package com.example.nagoyameshi.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.AdminCategoryEditForm;
import com.example.nagoyameshi.form.AdminCategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.service.AdminCategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
	
	private final CategoryRepository categoryRepository;
	private final AdminCategoryService adminCategoryService;
	
	public AdminCategoryController(CategoryRepository categoryRepository, AdminCategoryService adminCategoryService) {
		this.categoryRepository = categoryRepository;
		this.adminCategoryService = adminCategoryService;
	}
	
	//カテゴリ一覧
	@GetMapping("")
	public String index(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
		
		List<Category> categories;
		
		if(keyword != null && !keyword.isEmpty()) {
			categories = categoryRepository.findByNameLike("%" + keyword + "%");
		} else {
			categories = categoryRepository.findAll();
		}
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("categories", categories);
		return "admin/categories/index";
	}
	
	//カテゴリ登録画面へ
	@GetMapping("/register")
	public String register(Model model) {
		
		AdminCategoryRegisterForm adminCategoryRegisterForm = new AdminCategoryRegisterForm();
		
		model.addAttribute("adminCategoryRegisterForm", adminCategoryRegisterForm);
		return "admin/categories/register";
	}
	
	//カテゴリ登録
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated AdminCategoryRegisterForm adminCategoryRegisterForm,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		if(bindingResult.hasErrors()) {
			return "admin/categories/register";
		}
		
		adminCategoryService.create(adminCategoryRegisterForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "カテゴリを登録しました。");
		return "redirect:/admin/categories";
	}
	
	//カテゴリ編集画面へ
	@GetMapping("/{categoryId}/edit")
	public String edit(@PathVariable(name = "categoryId") Integer categoryId, Model model) {
		
		Category category = categoryRepository.getReferenceById(categoryId);
		AdminCategoryEditForm adminCategoryEditForm = new AdminCategoryEditForm();
		adminCategoryEditForm.setId(category.getId());
		adminCategoryEditForm.setName(category.getName());
		model.addAttribute("adminCategoryEditForm", adminCategoryEditForm);
		return "admin/categories/edit";
	}
	
	//カテゴリ編集
	@PostMapping("/update")
	public String update(@ModelAttribute @Validated AdminCategoryEditForm adminCategoryEditForm,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		if(bindingResult.hasErrors()) {
			return "admin/categories/edit";
		}
		
		adminCategoryService.update(adminCategoryEditForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "カテゴリを編集しました。");
		return "redirect:/admin/categories";
	}
	
	//カテゴリ削除
	@PostMapping("/{categoryId}/delete")
	@Transactional
	public String delete(@PathVariable(name = "categoryId") Integer categoryId, RedirectAttributes redirectAttributes) {
		
		Category category = categoryRepository.getReferenceById(categoryId);
		categoryRepository.delete(category);
		
		redirectAttributes.addFlashAttribute("successMessage", "既存のカテゴリを削除しました。");
		return "redirect:/admin/categories";
	}
	
}
