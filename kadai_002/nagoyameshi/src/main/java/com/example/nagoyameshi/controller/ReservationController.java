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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

@Controller
public class ReservationController {
	
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;
	private final StoreRepository storeRepository;
	
	public ReservationController(ReservationRepository reservationRepository,
								ReservationService reservationService,
								StoreRepository storeRepository) {
		
		this.reservationRepository = reservationRepository;
		this.reservationService = reservationService;
		this.storeRepository = storeRepository;
	}
	
	//予約一覧
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
						Model model) {
		
		User user = userDetailsImpl.getUser();
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("reservationPage", reservationPage);
		return "reservations/index";
	}
	
	//新規予約画面へ遷移
	@GetMapping("/stores/{storeId}/reservations/register")
	public String register(@PathVariable(name = "storeId") Integer storeId,Model model) {
		
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + storeId));
		
		model.addAttribute("store", store);
		model.addAttribute("reservationRegisterForm", new ReservationRegisterForm());
		return "reservations/register";
	}
	
	//予約登録
	@PostMapping("/stores/{storeId}/reservations/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@ModelAttribute @Validated ReservationRegisterForm reservationRegisterForm,
						BindingResult bindingResult,
						@PathVariable(name = "storeId") Integer storeId,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		User user = userDetailsImpl.getUser();
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + storeId));
		
		//先にバリデーションエラー @notnul などを判定
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			return "reservations/register";
		}
		
		//登録日時の判定
		if(reservationService.isPastDateTime(reservationRegisterForm.getReservationDate(), reservationRegisterForm.getReservationTime())) {
			bindingResult.addError(new FieldError(bindingResult.getObjectName(), "reservationDate", "過去の日時は指定できません。"));
			model.addAttribute("store", store);
			return "reservations/register";
		}
		
		//営業時間外か判定
		if(reservationService.isOutsideBusinessHours(reservationRegisterForm, store)) {
			bindingResult.addError(new FieldError(bindingResult.getObjectName(), "reservationTime", "営業時間外は指定できません。"));
			model.addAttribute("store", store);
			return "reservations/register";
		}
		
		reservationService.create(reservationRegisterForm, store, user);
		
		redirectAttributes.addFlashAttribute("successMessage", "予約を登録しました。");
		return "redirect:/stores/" + storeId; //店舗詳細へ遷移
	}
	
	//予約キャンセル
	@PostMapping("/reservations/{reservationId}/delete")
	@Transactional
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PathVariable(name = "reservationId") Integer reservationId,
						RedirectAttributes redirectAttributes) {
		
		User user = userDetailsImpl.getUser();
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + reservationId));
		
		if(!user.getId().equals(reservation.getUser().getId())) {
			return "redirect:/reservations";
		}
		
		reservationRepository.deleteById(reservationId);
		
		redirectAttributes.addFlashAttribute("successMessage", "予約を削除しました。");
		return "redirect:/reservations";
	}
	
}
