package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;

@Controller
@RequestMapping("/stores")
public class StoreController {

	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final ReviewRepository reviewRepository;
	private final FavoriteRepository favoriteRepository;

	public StoreController(StoreRepository storeRepository, CategoryRepository categoryRepository,
							ReviewRepository reviewRepository, FavoriteRepository favoriteRepository)
	{
		this.storeRepository = storeRepository;
		this.categoryRepository = categoryRepository;
		this.reviewRepository = reviewRepository;
		this.favoriteRepository = favoriteRepository;
	}

	//店舗一覧
	@GetMapping("")
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@RequestParam(name = "price", required = false) Integer price,
						@RequestParam(name = "categoryId", required = false) Integer categoryId,
						@RequestParam(name = "order", required = false) String order,
						@PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable,
						Model model) {

		List<Category> categories = categoryRepository.findAll();
		Page<Store> storePage;

		if (keyword != null && !keyword.isEmpty()) { //キーワード検索

			if ("priceDesc".equals(order)) {
				storePage = storeRepository.findByNameLikeOrderByPriceUpperDesc("%" + keyword + "%", pageable);
			} else {
				storePage = storeRepository.findByNameLikeOrderByPriceLowerAsc("%" + keyword + "%", pageable);
			}

		} else if (price != null) { //予算での絞り込み

			if ("priceDesc".equals(order)) {
				storePage = storeRepository.findByPriceLowerLessThanEqualOrderByPriceUpperDesc(price, pageable);
			} else {
				storePage = storeRepository.findByPriceLowerLessThanEqualOrderByPriceLowerAsc(price, pageable);
			}

		} else if (categoryId != null) { //カテゴリでの絞り込み

			if ("priceDesc".equals(order)) {
				storePage = storeRepository.findByCategories_IdOrderByPriceUpperDesc(categoryId, pageable);
			} else {
				storePage = storeRepository.findByCategories_IdOrderByPriceLowerAsc(categoryId, pageable);
			}

		} else { //何も指定がない時

			if ("priceDesc".equals(order)) {
				storePage = storeRepository.findAllByOrderByPriceUpperDesc(pageable);
			} else if ("priceAsc".equals(order)) {
				storePage = storeRepository.findAllByOrderByPriceLowerAsc(pageable);
			} else {
				storePage = storeRepository.findAll(pageable);
			}

		}

		model.addAttribute("categories", categories);
		model.addAttribute("storePage", storePage);
		// 現在の検索状態・セレクトボックスの選択状態を画面でキープするためのデータ送信
		model.addAttribute("keyword", keyword);
		model.addAttribute("price", price);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("order", order);

		return "stores/index";
	}

	//店舗詳細ページ
	@GetMapping("/{storeId}")
	public String show(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PathVariable(name = "storeId") Integer id,
						Model model) {

		Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
		Page<Review> reviewPage = reviewRepository.findByStoreIdOrderByIdDesc(id, PageRequest.of(0, 6));
		boolean hasFavorite = false;

		if (userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			model.addAttribute("user", user);
			hasFavorite = favoriteRepository.existsByStoreAndUser(store, user); // お気に入りの判定
		}

		model.addAttribute("store", store);
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("hasFavorite", hasFavorite);

		return "stores/show";

	}

}
