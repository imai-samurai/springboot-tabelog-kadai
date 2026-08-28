package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	//会員登録を実行するメソッド
	@Transactional
	public User create(SignupForm signupForm) {
		
		User user = new User();
		Role role = roleRepository.findByName("ROLE_FREE");
		
		user.setRole(role);
		user.setName(signupForm.getName());
		user.setFuri(signupForm.getFuri());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));//パスワードをハッシュ化してセットする
		user.setEnabled(false); //メール認証の可否
		
		return userRepository.save(user);
	}
	
	//メールアドレスが登録済みかチェックする
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		return user != null; //登録済みであればtrue
	}
	
	//パスワードと確認用のパスワードが一致するかチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);//一致していればtrue
	}
	
	//会員情報の編集
	@Transactional
	public void update(UserEditForm userEditForm, User user) {
		
		user.setName(userEditForm.getName());
		user.setFuri(userEditForm.getFuri());
		user.setEmail(userEditForm.getEmail());
		
		userRepository.save(user);
	}
	
	//ユーザーを有効にする
	@Transactional
	public void enableUser(User user) {
		
		user.setEnabled(true);
		userRepository.save(user);
		
	}	
	
}
