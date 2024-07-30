package com.asm.pandaboo.models;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBean {
	@AssertTrue(message = "Phương thức thanh toán không được bỏ trống")
	private boolean payMethod;
	@AssertTrue(message = "Điều khoản phải được chấp nhận")
	private boolean terms;
}
