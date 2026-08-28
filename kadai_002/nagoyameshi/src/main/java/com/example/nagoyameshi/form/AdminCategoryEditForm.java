package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryEditForm {
	
	@NotNull
	private Integer id;
	
	@NotBlank(message = "カテゴリを入力してください。")
	private String name;
	
}
