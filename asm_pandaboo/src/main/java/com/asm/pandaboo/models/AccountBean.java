package com.asm.pandaboo.models;

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
    
    @NotNull(message = "Ảnh đại diện không được để trống")
    private String avatar;
    
    private boolean roles;
    
    private boolean status;

}
