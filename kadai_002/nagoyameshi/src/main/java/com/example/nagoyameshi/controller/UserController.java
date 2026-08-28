package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	//ユーザーの詳細画面へ遷移
	@GetMapping("")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {

		User user = userDetailsImpl.getUser();

		model.addAttribute("user", user);

		return "user/index";
	}

	//ユーザーの編集画面へ遷移
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {

		User user = userDetailsImpl.getUser();
		UserEditForm userEditForm = new UserEditForm(user.getName(), user.getFuri(), user.getEmail());

		model.addAttribute("userEditForm", userEditForm);
		return "user/edit";
	}

	//ユーザーの編集画面へ遷移
	@PostMapping("/update")
	public String update(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@ModelAttribute @Validated UserEditForm userEditForm,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes) {
		
		User user = userDetailsImpl.getUser();
		
		// メールアドレスが「変更されており」、かつ「既に他の人に登録済み」であればエラー
		if (!userEditForm.getEmail().equals(user.getEmail()) && userService.isEmailRegistered(userEditForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}
		
		if(bindingResult.hasErrors()) {
			return "user/edit";
		}
		
		userService.update(userEditForm, user);
		
		redirectAttributes.addFlashAttribute("successMessage", "ユーザー情報を編集しました。");
		return "redirect:/user";
	}
	
}
