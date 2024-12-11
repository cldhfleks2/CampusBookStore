$(document).ready(function() {
    //개인정보 수정 페이지를 기본으로 보여줌
    displayToggle("personalInfo");

    //사이드바 메뉴 활성화
    sidebarMenu();

    //개인정보 수정 폼
    personalInfoFormSubmitBtn();

    //페이지네이션
    pagination();
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
    $(tag).addClass('active');
}

function sidebarMenu () {
    // 사이드바 메뉴 클릭 이벤트 핸들러
    $('.sidebarMenu a').on('click', function(e) {
        e.preventDefault(); // 기본 링크 동작 방지

        // 클릭된 메뉴의 id값 추출
        var targetContentId = $(this).data('content');
        // activeMenuToggle(targetContentId)

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
    const name = $('#name').val().trim();
    if (name === '') {
        showError($('#name'), '이름을 입력해주세요.');
        return false;
    }
    hideError($('#name'));
    return true;
}
function emailValidation(){
    const email = $('#email').val().trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (email === '') {
        showError($('#email'), '이메일을 입력해주세요.');
        return false;
    } else if (!emailRegex.test(email)) {
        showError($('#email'), '올바른 이메일 형식이 아닙니다.');
        return false;
    }
    hideError($('#email'));
    return true;

}
function phoneValidation(){
    const phone = $('#phone').val().trim();
    const phoneRegex = /^(010|011|016|017|018|019)[0-9]{7,8}$/;

    if (phone === '') {
        showError($('#phone'), '전화번호를 입력해주세요.');
        return false;
    } else if (!phoneRegex.test(phone)) {
        showError($('#phone'), '올바른 전화번호 형식이 아닙니다. (-없이 입력)');
        return false;
    }
    hideError($('#phone'));
    return true;
}
function campusValidation(){
    const campus = $('#campus').val().trim();
    if (campus === '') {
        showError($('#campus'), '대학교명을 입력해주세요.');
        return false;
    } else if (campus.length < 2) {
        showError($('#campus'), '대학교명은 최소 2자 이상이어야 합니다.');
        return false;
    }
    hideError($('#campus'));
    return true;
}
function passwordValidation(){
    const password = $('#password').val();
    const confirmPassword = $('#passwordCorrect').val();

    if (password === '') {
        showError($('#password'), '비밀번호를 입력해주세요.');
        return false;
    }

    if (confirmPassword === '') {
        showError($('#passwordCorrect'), '비밀번호 확인을 입력해주세요.');
        return false;
    } else if (password !== confirmPassword) {
        showError($('#passwordCorrect'), '비밀번호가 일치하지 않습니다.');
        return false;
    }

    hideError($('#password'));
    hideError($('#passwordCorrect'));
    return true;
}
function personalInfoFormSubmitBtn(){
    $(document).on("click", ".formActions button", function (e) {
        //폼 전송 이벤트 취소
        e.preventDefault();

        if ($(this).text() === "정보 수정") {
            $(this).text("제출");
            $(this).attr('type', 'submit');
            $(".personalInfoForm input").removeAttr("readonly");
            return;
        }

        // 제출 버튼 클릭 시 검증
        let isNameValid = nameValidation();
        let isEmailValid = emailValidation();
        let isPhoneValid = phoneValidation();
        let isCampusValid = campusValidation();
        let isPasswordValid = passwordValidation();
        console.log("이름 "+ isNameValid);
        console.log("이메일 "+isEmailValid);
        console.log("폰 "+isPhoneValid);
        console.log("학교 "+isCampusValid);
        console.log("비번 "+isPasswordValid);
        console.log("");

        // 모든 검증 통과 시 폼 제출
        if (isNameValid && isEmailValid && isPhoneValid && isCampusValid && isPasswordValid) {
            $(".personalInfoForm").submit();
        }

    })
}

function pagination(){
    $(document).on('click',"#prevPage", function() {
        let currentPage = $("#pageInfo").data("current-page");
        const totalPages = $("#pageInfo").data("total-page");
        console.log(currentPage)
        console.log(totalPages)

        if (currentPage > 1) {
            currentPage--;

            // 새 페이지 정보 갱신
            $("#pageInfo").data("current-page", currentPage);
            $("#pageInfo").text(currentPage + ' / ' + totalPages);

            $.ajax({
                url: "/mypage",
                method: "get",
                data: {pageIdx: currentPage},
                success: function (data){
                    var data = $.parseHTML(data);
                    var dataHtml = $("<div>").append(data);
                    $("#sellContent").replaceWith(dataHtml.find("#sellContent"));
                }
            })
        }
    });

    $(document).on('click', "#nextPage", function() {
        let currentPage = $("#pageInfo").data("current-page");
        const totalPages = $("#pageInfo").data("total-page");
        console.log(currentPage)
        console.log(totalPages)

        if (currentPage < totalPages) {
            currentPage++;

            // 새 페이지 정보 갱신
            $("#pageInfo").data("current-page", currentPage);
            $("#pageInfo").text(currentPage + ' / ' + totalPages);

            $.ajax({
                url: "/mypage",
                method: "get",
                data: {pageIdx: currentPage},
                success: function (data){
                    var data = $.parseHTML(data);
                    var dataHtml = $("<div>").append(data);
                    $("#sellContent").replaceWith(dataHtml.find("#sellContent"));
                }
            })
        }
    });
    // let currentPage = $("#pageInfo").data("current-page");
    // const totalPages = $("#pageInfo").data("total-page");
    //
    // $("#prevPage").prop('disabled', currentPage <= 1);
    // $("#nextPage").prop('disabled', currentPage >= totalPages);
    // $("#pageInfo").text(`${currentPage}/${totalPages}`);
}
