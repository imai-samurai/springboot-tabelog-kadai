package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {

	@NotBlank(message = "お名前を入力してください。")
	private String name;

	@NotBlank(message = "フリガナを入力してください。")
	@Pattern(regexp = "^[ァ-ヶー]+$", message = "フリガナは全角カタカナで入力してください。")
	private String furi;

	@NotBlank(message = "メールアドレスの入力をしてください")
	@Email(message = "メールアドレスの形式が正しくありません。")
	private String email;
	
}
