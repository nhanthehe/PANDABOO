const form = document.getElementById('registerForm');
const nameInput = document.getElementById('name');
const emailInput = document.getElementById('email');
const phoneInput = document.getElementById('phone');
const passwordInput = document.getElementById('password');
const confirmPasswordInput = document.getElementById('confirm_password');


form.addEventListener('submit', (event) => {
  // Kiểm tra
  let isValid = true;

  if (!validateName(nameInput.value)) {
    event.preventDefault();
    nameInput.classList.add('is-invalid');
    isValid = false;
  } else {
    nameInput.classList.remove('is-invalid');
  }

  if (!validateEmail(emailInput.value)) {
    event.preventDefault();
    emailInput.classList.add('is-invalid');
    isValid = false;
  } else {
    emailInput.classList.remove('is-invalid');
  }

  if (!validatePhone(phoneInput.value)) {
    event.preventDefault();
    phoneInput.classList.add('is-invalid');
    isValid = false;
  } else {
    phoneInput.classList.remove('is-invalid');
  }

  if (!validatePassword(passwordInput.value)) {
    event.preventDefault();
    passwordInput.classList.add('is-invalid');
    isValid = false;
  } else {
    passwordInput.classList.remove('is-invalid');
  }

  if (confirmPasswordInput.value !== passwordInput.value) {
    event.preventDefault();
    confirmPasswordInput.classList.add('is-invalid');
    isValid = false;
  } else {
    confirmPasswordInput.classList.remove('is-invalid');
  }

  if (isValid) {
    window.location.href = '/login.html';
  }
});

function validateName(name) {
  const nameRegex = /^[a-zA-Z\s]+$/;
  return nameRegex.test(name) && name.trim() !== '';
}

function validateEmail(email) {
  const emailRegex = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/;
  return emailRegex.test(email);
}

function validatePhone(phone) {
  const phoneRegex = /^0\d{9}$/;
  return phoneRegex.test(phone);
}

function validatePassword(password) {
  return password.trim() !== '' && password.length >= 3;
}