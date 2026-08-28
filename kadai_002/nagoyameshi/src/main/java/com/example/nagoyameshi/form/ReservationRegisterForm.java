package com.example.nagoyameshi.form;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRegisterForm {
	
	@NotNull(message = "日付を登録してください。")
	private LocalDate reservationDate;
	
	@NotNull(message = "時間を登録してください。")
	private LocalTime reservationTime;
	
	@NotNull(message = "予約人数を登録してください。")
	@Min(value = 1, message = "予約人数は1人以上に設定してください。")
	private Integer numberOfPeople;
	
}
