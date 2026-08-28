package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

	private final ReviewRepository reviewRepository;
	private final StoreRepository storeRepository;
	private final ReviewService reviewService;

	public ReviewController(ReviewRepository reviewRepository, StoreRepository storeRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.storeRepository = storeRepository;
		this.reviewService = reviewService;
	}

	//レビュー一覧（有料会員のみ）
	@GetMapping("")
	public String index(@PathVariable(name = "storeId") Integer id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
			Model model) {

		Store store = storeRepository.getReferenceById(id);
		Page<Review> reviewPage = reviewRepository.findByStoreIdOrderByIdDesc(id, pageable);

		model.addAttribute("store", store);
		model.addAttribute("reviewPage", reviewPage);
		return "reviews/index";
	}

	//レビュー新規投稿ページへ遷移
	@GetMapping("/register")
	public String register(@PathVariable(name = "storeId") Integer storeId, Model model) {

		Store store = storeRepository.getReferenceById(storeId);

		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		model.addAttribute("store", store);

		return "reviews/register";

	}
	
	//レビュー新規投稿を登録
	@PostMapping("/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
						BindingResult bindingResult,
						@PathVariable(name = "storeId") Integer storeId,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		Store store = storeRepository.getReferenceById(storeId);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			return"reviews/register";
		}
		
		User user = userDetailsImpl.getUser();
		
		reviewService.create(store, user, reviewRegisterForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		return "redirect:/stores/" + storeId + "/reviews";
	}
	
	//レビューの編集画面へ遷移
	@GetMapping("/{reviewId}/edit")
	public String edit(@PathVariable(name = "storeId") Integer storeId,
						@PathVariable(name = "reviewId") Integer reviewId,
						Model model) {
		
		Store store = storeRepository.getReferenceById(storeId);
		Review review = reviewRepository.getReferenceById(reviewId);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getScore(), review.getComment());
		
		model.addAttribute("store", store);
		model.addAttribute("reviewEditForm", reviewEditForm);
		return "reviews/edit";
	}
	
	//レビューの編集を保存
	@PostMapping("/{reviewId}/update")
	public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm,
						BindingResult bindingResult,
						@PathVariable(name = "storeId") Integer storeId,
						@PathVariable(name = "reviewId") Integer reviewId,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		Store store = storeRepository.getReferenceById(storeId);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			return"reviews/edit";
		}
		
		reviewService.update(reviewEditForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		return "redirect:/stores/" + storeId + "/reviews"; //レビュー一覧へ遷移
	}

	//レビュー削除
	@Transactional
	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable(name = "storeId") Integer storeId,
						@PathVariable(name = "reviewId") Integer reviewId,
						@RequestParam(name = "from", required = false) String from, // 店舗詳細からの削除の区別用
						RedirectAttributes redirectAttributes) {

		reviewRepository.deleteById(reviewId);

		if ("show".equals(from)) {
			redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
			return "redirect:/stores/" + storeId; //店舗詳細へ遷移
		} else {
			redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
			return "redirect:/stores/" + storeId + "/reviews"; //レビュー一覧へ遷移
		}
	}

}
