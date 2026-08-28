package com.example.nagoyameshi.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.AdminStoreEditForm;
import com.example.nagoyameshi.form.AdminStoreRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.service.AdminStoreService;

@Controller
@RequestMapping("/admin/stores")
public class AdminStoreController {
	
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final AdminStoreService adminStoreService;
	
	public AdminStoreController(StoreRepository storeRepository, CategoryRepository categoryRepository, AdminStoreService adminStoreService) {
		
		this.storeRepository = storeRepository;
		this.categoryRepository = categoryRepository;
		this.adminStoreService = adminStoreService;
		
	}
	
	//店舗一覧
	@GetMapping("")
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable,
						Model model) {
		
		Page<Store> storePage;
		
		if(keyword != null && !keyword.isEmpty()) {
			storePage = storeRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			storePage = storeRepository.findAll(pageable);
		}
		
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("storePage", storePage);
		return "admin/store/index";
	}
	
	//店舗詳細
	@GetMapping("/{storeId}") 
	public String show(@PathVariable(name = "storeId") Integer storeId, Model model) {
		
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + storeId));
		
		model.addAttribute("store", store);
		return "admin/store/show";
	}
	
	//店舗登録(Get)
	@GetMapping("/register")
	public String register(Model model) {
		
		AdminStoreRegisterForm adminStoreRegisterForm = new AdminStoreRegisterForm();
		List<Category> categories = categoryRepository.findAll();
		
		model.addAttribute("adminStoreRegisterForm", adminStoreRegisterForm);
		model.addAttribute("categories", categories);
		return "admin/store/register";
	}
	
	//店舗登録(Post)
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated AdminStoreRegisterForm adminStoreRegisterForm,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		if(bindingResult.hasErrors()) {
			List<Category> categories = categoryRepository.findAll();
			model.addAttribute("categories", categories);
			return "admin/store/register";
		}
		
		adminStoreService.create(adminStoreRegisterForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を登録しました。");
		return "redirect:/admin/stores";
	}
	
	//店舗編集(Get)
	@GetMapping("/{storeId}/edit")
	public String edit(@PathVariable(name = "storeId") Integer storeId, Model model) {
		
		List<Category> categories = categoryRepository.findAll();
		Store store = storeRepository.getReferenceById(storeId);
		
		List<Integer> categoryIds = new ArrayList<>();
		for (Category category : store.getCategories()) {
			categoryIds.add(category.getId());
		}
		
		AdminStoreEditForm adminStoreEditForm = new AdminStoreEditForm();
		
		adminStoreEditForm.setId(store.getId());
		adminStoreEditForm.setCategoryIds(categoryIds);
		adminStoreEditForm.setName(store.getName());
		adminStoreEditForm.setDescription(store.getDescription());
		adminStoreEditForm.setPriceUpper(store.getPriceUpper());
		adminStoreEditForm.setPriceLower(store.getPriceLower());
		adminStoreEditForm.setHoursOpen(store.getHoursOpen().toString());
		adminStoreEditForm.setHoursClose(store.getHoursClose().toString());
		adminStoreEditForm.setPostalCode(store.getPostalCode());
		adminStoreEditForm.setAddress(store.getAddress());
		adminStoreEditForm.setPhoneNumber(store.getPhoneNumber());
		adminStoreEditForm.setRegularHoliday(store.getRegularHoliday());
		
		model.addAttribute("categories", categories);
		model.addAttribute("store", store);
		model.addAttribute("adminStoreEditForm", adminStoreEditForm);
		
		return "admin/store/edit";
	}
	
	//店舗編集(Post)
	@PostMapping("/{storeId}/update")
	public String update(@PathVariable(name = "storeId") Integer storeId,
						@ModelAttribute @Validated AdminStoreEditForm adminStoreEditForm,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		if(bindingResult.hasErrors()) {
			Store store = storeRepository.getReferenceById(storeId);
			List<Category> categories = categoryRepository.findAll();
			model.addAttribute("store", store);
			model.addAttribute("categories", categories);
			return "admin/store/edit";
		}
		
		adminStoreService.update(adminStoreEditForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
		return "redirect:/admin/stores"; //店舗一覧へ遷移
	}
	
	//店舗削除
	@PostMapping("/{storeId}/delete")
	@Transactional
	public String delete(@PathVariable(name = "storeId") Integer storeId, RedirectAttributes redirectAttributes) {
		
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + storeId));
		storeRepository.delete(store);
		
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を削除しました。");
		return "redirect:/admin/stores"; //店舗一覧へ遷移
	}
	

}
