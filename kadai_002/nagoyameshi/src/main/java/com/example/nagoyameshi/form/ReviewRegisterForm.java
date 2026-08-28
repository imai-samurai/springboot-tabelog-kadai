package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRegisterForm {

	@NotNull(message = "評価を選択してください。")
	@Min(value = 1, message = "評価は1以上で指定してください")
	@Max(value = 5, message = "5段階で評価をお願いします。")
	private Integer score;

	@NotBlank(message = "コメントを記載してください。")
	private String comment;

}
