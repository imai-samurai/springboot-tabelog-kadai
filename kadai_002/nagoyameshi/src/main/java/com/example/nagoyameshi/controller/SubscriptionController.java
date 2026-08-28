package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {
	
	private final StripeService stripeService;
	
	public SubscriptionController(StripeService stripeService) {
		this.stripeService = stripeService;
	}
	
	//有料会員登録画面へ遷移
	@GetMapping("/register")
	public String register() {
		
		return "subscription/register";
	}
	
	//実際の登録処理画面へ遷移(Stripe)
	@PostMapping("/register/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						HttpServletRequest httpServletRequest,
						RedirectAttributes redirectAttributes) {
		
		User user = userDetailsImpl.getUser();
		
		String checkoutUrl = stripeService.createStripeSession(httpServletRequest, user);
		
		if (checkoutUrl.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "エラーが発生しました。");
			return "redirect:/subscription/register";
		}
		
		return "redirect:" + checkoutUrl;
	}
	
	//有料会員登録　サクセスの時
	@GetMapping("/success")
	public String success(@RequestParam("session_id") String sessionId,
							RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("successMessage", "プレミアム会員へのご登録が完了しました。");
		return "redirect:/";
	}
	
	//クレカ編集・有料会員解約
	@PostMapping("/portal")
	public String portal(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						HttpServletRequest httpServletRequest,
						RedirectAttributes redirectAttributes) {
		
		String stripeCustomerId = userDetailsImpl.getUser().getStripeCustomerId();
		
		if(stripeCustomerId == null || stripeCustomerId.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "有料会員情報の確認ができませんでした。");
		    return "redirect:/";
		}
		
		String portalUrl = stripeService.createStripePortalSession(stripeCustomerId, httpServletRequest);
		
		return "redirect:" + portalUrl;
	}
	
}
