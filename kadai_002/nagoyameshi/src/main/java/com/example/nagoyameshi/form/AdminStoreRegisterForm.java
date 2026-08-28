package com.example.nagoyameshi.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminStoreRegisterForm {

	List<Integer> categoryIds;

	@NotBlank(message = "店舗名を入力してください")
	private String name;

	@NotNull(message = "店舗の写真を登録してください")
	private MultipartFile imageFile;

	@NotBlank(message = "店舗の説明文を入力してください")
	private String description;

	@NotNull(message = "価格帯(上限)を入力してください")
	@Min(value = 0, message = "価格帯は0円以上に設定してください")
	private Integer priceUpper;

	@NotNull(message = "価格帯(下限)を入力してください")
	@Min(value = 0, message = "価格帯は0円以上に設定してください")
	private Integer priceLower;

	@NotBlank(message = "開店時間を選択してください")
	private String hoursOpen;

	@NotBlank(message = "閉店時間を選択してください")
	private String hoursClose;

	@NotBlank(message = "郵便番号を入力してください")
	private String postalCode;

	@NotBlank(message = "住所を入力してください")
	private String address;

	@NotBlank(message = "電話番号を入力してください")
	private String phoneNumber;

	@NotBlank(message = "定休日を入力してください")
	private String regularHoliday;

}
