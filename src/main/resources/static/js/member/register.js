$(document).ready(function() {
    nameValidation();
    emailValidation();
    phoneValidation();
    campusValidation();
    passwordValidation();

    //회원가입버튼을 누르면 ajax요청 전송
    submit();
});


function nameValidation() {
    $('#name').on('blur', function() {
        const name = $(this).val().trim();
        if (name === '') {
            showError($(this), '이름을 입력해주세요.');
            return false;
        }
        hideError($(this));
        return true;
    });
}

function emailValidation(){
    $('#email').on('blur', function() {
        const email = $(this).val().trim();
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (email === '') {
            showError($(this), '이메일을 입력해주세요.');
            return false;
        } else if (!emailRegex.test(email)) {
            showError($(this), '올바른 이메일 형식이 아닙니다.');
            return false;
        }
        hideError($(this));
        return true;
    });

}

function phoneValidation(){
    $('#phone').on('blur', function() {
        const phone = $(this).val().trim();
        const phoneRegex = /^(010|011|016|017|018|019)[0-9]{7,8}$/;

        if (phone === '') {
            showError($(this), '전화번호를 입력해주세요.');
            return false;
        } else if (!phoneRegex.test(phone)) {
            showError($(this), '올바른 전화번호 형식이 아닙니다. (-없이 입력)');
            return false;
        }
        hideError($(this));
        return true;
    });
}

function campusValidation(){
    // Campus validation
    $('#campus').on('blur', function() {
        const campus = $(this).val().trim();
        if (campus === '') {
            showError($(this), '대학교명을 입력해주세요.');
            return false;
        } else if (campus.length < 2) {
            showError($(this), '대학교명은 최소 2자 이상이어야 합니다.');
            return false;
        }
        hideError($(this));
        return true;
    });
}

function passwordValidation(){
    $('#password').on('blur', function() {
        const password = $(this).val();

        if (password === '') {
            showError($(this), '비밀번호를 입력해주세요.');
            return false;
        }
        hideError($(this));
        return true;
    });

    // 확인
    $('#passwordCorrect').on('blur', function() {
        const password = $('#password').val();
        const confirmPassword = $(this).val();

        if (confirmPassword === '') {
            showError($(this), '비밀번호 확인을 입력해주세요.');
            return false;
        } else if (password !== confirmPassword) {
            showError($(this), '비밀번호가 일치하지 않습니다.');
            return false;
        }
        hideError($(this));
        return true;
    });
}

//회원가입 번호 누를때
function submit() {
    // Form submission validation
    $('.signup-button').on('click', function(e) {
        e.preventDefault();

        // Trigger all validations
        const isNameValid = $('#name').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
        const isEmailValid = $('#email').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
        const isPhoneValid = $('#phone').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
        const isCampusValid = $('#campus').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
        const isPasswordValid = $('#password').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
        const isPasswordConfirmValid = $('#passwordCorrect').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;

        // Check if all validations pass
        if (isNameValid && isEmailValid && isPhoneValid && isCampusValid && isPasswordValid && isPasswordConfirmValid) {
            var name = $("#name").val();
            var email = $("#email").val();
            var phone = $("#phone").val();
            var campus = $("#campus").val();
            var password = $("#password").val();
            $.ajax({
                url: "/register",
                method: "post",
                data: {
                    name: name,
                    email: email,
                    phone: phone,
                    campus: campus,
                    password: password,
                },
                success: function (data){
                    console.log(data);
                    alert("회원가입이 완료 되었습니다. 다시 로그인 하세요.")
                    window.location.href = "/login";
                },
                error: function (xhr){
                    console.log(xhr.responseText);
                    if(JSON.parse(xhr.responseText).message === "이름이 중복 됩니다.")
                        alert("이름이 중복 됩니다.")
                }
            })
        }
    });
}

function showError(element, message) {
    // Find the error message div for this input
    const errorDiv = element.closest('.input-group').find('.error-message');

    // Show and set error message
    errorDiv.text(message).show();
}

function hideError(element) {
    const errorDiv = element.closest('.input-group').find('.error-message');
    errorDiv.text('').hide();
}