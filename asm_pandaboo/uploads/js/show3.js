const emailInput = document.getElementById('email');
const verificationCodeInput = document.getElementById('verificationCode');
const submitButton = document.querySelector('#forgotPasswordForm button[type="submit"]');

document.getElementById('forgotPasswordForm').addEventListener('submit', function(event) {

  if (emailInput.value.trim() === '') {
    emailInput.classList.add('is-invalid');
    event.preventDefault();
  } else {
    emailInput.classList.remove('is-invalid');
  }

  if (verificationCodeInput.value.trim() === '') {
    verificationCodeInput.classList.add('is-invalid');
    event.preventDefault();
  } else {
    verificationCodeInput.classList.remove('is-invalid');
  }

  const emailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  if (!emailRegex.test(emailInput.value)) {
    emailInput.classList.add('is-invalid');
    event.preventDefault();
  } else {
    emailInput.classList.remove('is-invalid');
  }
});