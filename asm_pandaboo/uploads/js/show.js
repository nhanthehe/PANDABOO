const showPasswordCheckbox = document.getElementById('f-option2');
const passwordInput = document.getElementById('password');

showPasswordCheckbox.addEventListener('click', function() {
	if (this.checked) {
		passwordInput.type = 'text';
	} else {
		passwordInput.type = 'password';
	}
})
var forms = document.querySelectorAll('.login_form');

forms.forEach(form => {
  form.addEventListener('submit', event => {
    var nameInput = form.querySelector('#name');
    var passwordInput = form.querySelector('#password');

//ktten
    var specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
    if (nameInput.value.trim() === '' || specialCharRegex.test(nameInput.value)) {
      nameInput.classList.add('is-invalid');
      event.preventDefault();
    } else {
      nameInput.classList.remove('is-invalid');
    }

//ktra pass
    if (passwordInput.value.trim() === '' || passwordInput.value.length < 3) {
      passwordInput.classList.add('is-invalid');
      event.preventDefault();
    } else {
      passwordInput.classList.remove('is-invalid');
    }
  });
});