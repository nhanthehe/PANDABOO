const changePasswordForm = document.getElementById('changePasswordForm');
const oldPassword = document.getElementById('oldPassword');
const newPassword = document.getElementById('newPassword');
const confirmNewPassword = document.getElementById('confirmNewPassword');

changePasswordForm.addEventListener('submit', (event) => {
	// Kiểm tra ô mật khẩu cũ
	const specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
	if (oldPassword.value.trim() === '' || specialCharRegex.test(oldPassword.value)) {
		oldPassword.classList.add('is-invalid');
		event.preventDefault();
	} else {
		oldPassword.classList.remove('is-invalid');
	}
	if (newPassword.value.trim() === '' || newPassword.value.length < 3 || specialCharRegex.test(newPassword.value)) {
		newPassword.classList.add('is-invalid');
		event.preventDefault();
	} else {
		newPassword.classList.remove('is-invalid');
	}
	if (confirmNewPassword.value !== newPassword.value) {
		confirmNewPassword.classList.add('is-invalid');
		event.preventDefault();
	} else {
		confirmNewPassword.classList.remove('is-invalid');
	}
});