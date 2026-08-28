package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@Controller
public class FavoriteController {

	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	private final StoreRepository storeRepository;

	public FavoriteController(FavoriteRepository favoriteRepository, FavoriteService favoriteService,
			StoreRepository storeRepository) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
		this.storeRepository = storeRepository;
	}

	//お気に入り一覧
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
						Model model) {

		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByIdDesc(user, pageable);

		model.addAttribute("favoritePage", favoritePage);
		return "favorite/index";
	}

	//お気に入り登録
	@PostMapping("/stores/{storeId}/favorites/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PathVariable(name = "storeId") Integer storeId,
						@RequestParam(name = "from", required = false) String from) {

		Store store = storeRepository.getReferenceById(storeId);
		User user = userDetailsImpl.getUser();
		favoriteService.create(store, user);

		if ("show".equals(from)) {
			return "redirect:/stores/" + storeId; //店舗詳細へ遷移
		} else {
			return "redirect:/favorites"; //お気に入り一覧へ遷移
		}

	}

	//お気に入り削除
	@PostMapping("/stores/{storeId}/favorites/delete")
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PathVariable(name = "storeId") Integer storeId,
						@RequestParam(name = "from", required = false) String from) {

		Store store = storeRepository.getReferenceById(storeId);
		User user = userDetailsImpl.getUser();
		favoriteService.delete(store, user);

		if ("show".equals(from)) {
			return "redirect:/stores/" + storeId; //店舗詳細へ遷移
		} else {
			return "redirect:/favorites"; //お気に入り一覧へ遷移
		}

	}

}
