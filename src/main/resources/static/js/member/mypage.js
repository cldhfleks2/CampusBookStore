$(document).ready(function() {
    //개인정보 수정 페이지를 기본으로 보여줌
    displayToggle("personalInfo");

    //사이드바 메뉴 활성화
    sidebarMenu();

    personalInfoFormSubmitBtn();
});

//클릭한 id를 가져와서 그것만 display=block;
function displayToggle (targetId) {
    // 모든 화면 display=none;
    $('.content').hide();
    $('#' + targetId).show();
}

//클릭한 class를 가져와서 active를 붙여줌
function sidebarActive (tag) {
    // 사이드바에 있는 모든 active 제거
    $('.sidebarMenu a').removeClass('active');
    // 해당 클래스에만 active 추가
    tag.addClass('active');
}

function sidebarMenu () {
    // 사이드바 메뉴 클릭 이벤트 핸들러
    $('.sidebarMenu a').on('click', function(e) {
        e.preventDefault(); // 기본 링크 동작 방지

        // 클릭된 메뉴의 id값 추출
        var targetContentId = $(this).data('content');

        // 클릭된 메뉴로 화면을 변경
        displayToggle(targetContentId);

        // 사이드바 메뉴 활성 상태 표시
        sidebarActive(this);
    });
}

function showError(element, message) {
    // Find the error message div for this input
    const errorDiv = element.closest('.formGroup').find('.error-message');


    // Show and set error message
    errorDiv.text(message).show();
}
function hideError(element) {
    const errorDiv = element.closest('.formGroup').find('.error-message');
    errorDiv.text('').hide();
}

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

function personalInfoFormSubmitBtn(){
    $(document).on("click", ".formActions button", function (e) {
        //폼 전송 이벤트 취소
        e.preventDefault();

        $(this).text("제출");
        $(this).attr('type', 'submit');
        $(".personalInfoForm input").removeAttr("readonly");

        //다시 폼 전송 이벤트를 검
        $(".formActions button").on("click", function (){
            // Trigger all validations
            const isNameValid = $('#name').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
            const isEmailValid = $('#email').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
            const isPhoneValid = $('#phone').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
            const isCampusValid = $('#campus').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
            const isPasswordValid = $('#password').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;
            const isPasswordConfirmValid = $('#passwordCorrect').trigger('blur').closest('.input-group').find('.error-message:visible').length === 0;

            // Check if all validations pass
            if (isNameValid && isEmailValid && isPhoneValid && isCampusValid && isPasswordValid && isPasswordConfirmValid) {
                console.log(isPhoneValid);

                // $(this).closest("form").submit();
            }
        })
    })
}

