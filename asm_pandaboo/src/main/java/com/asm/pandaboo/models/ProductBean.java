package com.asm.pandaboo.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBean {
	@NotBlank(message = "Tên sản phẩm không được bỏ trống!")
	@NotEmpty(message = "Tên sản phẩm không được bỏ trống!")
	private String prod_name;
	
	@Min(value = 0, message = "Loại sản phẩm không được để trống!")
	private int cat_id;
	
	@Min(value = 0, message = "Đơn vị tính không được để trống!")
	private int unit_id;
	
	@NotNull(message = "Giá gốc không được bỏ trống!")
    @Digits(integer = 9, fraction = 2, message = "Giá bán phải là số và không quá 9 chữ số phần nguyên và 2 chữ số phần thập phân")
    private Double price;
	

	@NotNull(message = "Giá bán không được bỏ trống!")
    @Digits(integer = 9, fraction = 2, message = "Giá bán phải là số và không quá 9 chữ số phần nguyên và 2 chữ số phần thập phân")
	private Double red_price;
	
    @NotBlank(message = "Mô tả không được bỏ trống!")
	@NotEmpty(message = "Mô tả không được bỏ trống!")
	private String descriptions;
    
    List<MultipartFile> images;
}
