package com.example.nagoyameshi.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagoyameshi.entity.Admin;

public class AdminDetailsImpl implements UserDetails {

	private final Admin admin;

	public AdminDetailsImpl(Admin admin) {
		this.admin = admin;
	}

	public Admin getUser() {
		return admin;
	}

	//ハッシュ化済みのパスワードを返す
	@Override
	public String getPassword() {
		return admin.getPassword();
	}

	//ログイン時に利用するユーザー名（メールアドレス）を返す
	@Override
	public String getUsername() {
		return admin.getEmail();
	}

	// 管理者の権限を付与する
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	// アカウントが期限切れでなければtrueを返す
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// ユーザーがロックされていなければtrueを返す
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// ユーザーのパスワードが期限切れでなければtrueを返す
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// ユーザーが有効であればtrueを返す
	@Override
	public boolean isEnabled() {
		return true;
	}

}
