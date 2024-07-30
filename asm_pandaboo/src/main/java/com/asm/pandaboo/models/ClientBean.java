package com.asm.pandaboo.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientBean {

	@NotEmpty(message = "Họ và tên không được để trống")
    private String fullname;
	
	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email chưa chưa đúng định dạng")
    private String email;

	@NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    private String road;
    
    private String ward;
    
    private String district;
    
    private String city;
    
    private String avatar;
}
