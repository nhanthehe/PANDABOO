package com.asm.pandaboo.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionBean {
	
	@NotBlank(message = "Tên ưu đãi không được bỏ trống!")
    @Size(min = 6, max = 30, message = "Tên ưu đãi phải từ 6 đến 30 ký tự!")
    private String prom_name;

    @NotNull(message = "Giá bắt đầu không được bỏ trống!")
    @Positive(message = "Giá bắt đầu phải là số dương!")
    private Double start_price;

    @NotNull(message = "Ngày bắt đầu không được bỏ trống!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start_date;

    @NotNull(message = "Ngày kết thúc không được bỏ trống!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end_date;

    @NotNull(message = "Tỉ lệ không được bỏ trống!")
    @Positive(message = "Phần trăm giảm giá phải là số dương!")
    private Double discount;

    private String note;
    
    private boolean status;
}
