package com.asm.pandaboo.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountBean {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 36, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    private String username;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Địa chỉ email không được để trống")
    @Email(message="Email không đúng định dạng")
    private String email;
    
    private boolean status;

}
