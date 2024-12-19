$(document).ready(function() {
    //개인정보 수정 페이지를 기본으로 보여줌
    displayToggle("personalInfo");
    //사이드바 메뉴 활성화
    sidebarMenu();

    //개인정보수정란 : 폼전송
    personalInfoFormSubmitBtn();

    //판매 내역 란 : 페이지네이션적용
    sellPagination();

    //주문 내역 란 : 페이지네이션
    purchasePagination();

    //찜한 상품 란
    likeyPagination();
});

//사이드 메뉴를 선택할때
//1. 선택한 메뉴에 active클래스를 붙여준다.
//2. 태그에 지정된 data-content값을 가져와서 해당 이름과 같은 id값인 div태그를 display:block으로 보여줌
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
//(1) active 클래스를 붙여줌 (1)
function sidebarActive (tag) {
    // 사이드바에 있는 모든 active 제거
    $('.sidebarMenu a').removeClass('active');
    // 해당 클래스에만 active 추가
    $(tag).addClass('active');
}
//(2) targetId를 받아서 #+targetId 로 선택해서 display:block;
function displayToggle (targetId) {
    // 모든 화면 display=none;
    $('.content').hide();
    $('#' + targetId).show();
}

//개인 정보 수정란에서 입력칸마다의 에러메시지를 보여주는 함수
function showError(element, message) {
    // Find the error message div for this input
    const errorDiv = element.closest('.formGroup').find('.error-message');

    // Show and set error message
    errorDiv.text(message).show();
}
//개인 정보 수정란에서 입력칸의 에러 메시지를 가린다.
function hideError(element) {
    const errorDiv = element.closest('.formGroup').find('.error-message');
    errorDiv.text('').hide();
}
//개인 정보 수정란에서 입력칸의 유효성 검사 함수들
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
//개인정보 수정 뷰
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
//판매내역 뷰
function sellPagination(){
    $(document).on('click',"#sellPrevPage", function() {
        let sellCurrentPage = $("#sellPageInfo").data("sell-current-page");
        const sellTotalPages = $("#sellPageInfo").data("sell-total-page");

        if (sellCurrentPage > 1) {
            sellCurrentPage--;

            // 새 페이지 정보 갱신
            $("#sellPageInfo").data("sell-current-page", sellCurrentPage);
            $("#sellPageInfo").text(sellCurrentPage + ' / ' + sellTotalPages);

            pageReload(sellCurrentPage);
        }
    });

    $(document).on('click', "#sellNextPage", function() {
        let sellCurrentPage = $("#sellPageInfo").data("sell-current-page");
        const sellTotalPages = $("#sellPageInfo").data("sell-total-page");

        if (sellCurrentPage < sellTotalPages) {
            sellCurrentPage++;

            // 새 페이지 정보 갱신
            $("#sellPageInfo").data("sell-current-page", sellCurrentPage);
            $("#sellPageInfo").text(sellCurrentPage + ' / ' + sellTotalPages);

            pageReload(sellCurrentPage);
        }
    });
    function pageReload(sellCurrentPage){
        $.ajax({
            url: "/mypage",
            method: "get",
            data: {pageIdx: sellCurrentPage},
            success: function (data){
                var data = $.parseHTML(data);
                var dataHtml = $("<div>").append(data);
                $("#sellContent").replaceWith(dataHtml.find("#sellContent"));
            }
        })
    }
}
//주문내역 뷰
function purchasePagination(){
    $(document).on('click',"#purchasePrevPage", function() {
        let purchaseCurrentPage = $("#purchasePageInfo").data("purchase-current-page");
        const purchaseTotalPages = $("#purchasePageInfo").data("purchase-total-page");

        if (purchaseCurrentPage > 1) {
            purchaseCurrentPage--;

            // 새 페이지 정보 갱신
            $("#purchasePageInfo").data("purchase-current-page", purchaseCurrentPage);
            $("#purchasePageInfo").text(purchaseCurrentPage + ' / ' + purchaseTotalPages);

            pageReload(purchaseCurrentPage);
        }
    });

    $(document).on('click', "#purchaseNextPage", function() {
        let purchaseCurrentPage = $("#purchasePageInfo").data("purchase-current-page");
        const purchaseTotalPages = $("#purchasePageInfo").data("purchase-total-page");

        if (purchaseCurrentPage < purchaseTotalPages) {
            purchaseCurrentPage++;

            // 새 페이지 정보 갱신
            $("#purchasePageInfo").data("purchase-current-page", purchaseCurrentPage);
            $("#purchasePageInfo").text(purchaseCurrentPage + ' / ' + purchaseTotalPages);

            pageReload(purchaseCurrentPage);
        }
    });

    function pageReload(purchaseCurrentPage){
        $.ajax({
            url: "/mypage",
            method: "get",
            data: {pageIdx: purchaseCurrentPage},
            success: function (data){
                var data = $.parseHTML(data);
                var dataHtml = $("<div>").append(data);
                $("#purchaseContent").replaceWith(dataHtml.find("#purchaseContent"));
            }
        })
    }
}
//찜한 상품 뷰
function likeyPagination() {
    $(document).on('click',"#likeyPrevPage", function() {
        let likeyCurrentPage = $("#likeyPageInfo").data("likey-current-page");
        const likeyTotalPages = $("#likeyPageInfo").data("likey-total-page");

        if (likeyCurrentPage > 1) {
            likeyCurrentPage--;

            // 새 페이지 정보 갱신
            $("#likeyPageInfo").data("likey-current-page", likeyCurrentPage);
            $("#likeyPageInfo").text(likeyCurrentPage + ' / ' + likeyTotalPages);

            pageReload(likeyCurrentPage);
        }
    });

    $(document).on('click', "#likeyNextPage", function() {
        let likeyCurrentPage = $("#likeyPageInfo").data("likey-current-page");
        const likeyTotalPages = $("#likeyPageInfo").data("likey-total-page");

        if (likeyCurrentPage < likeyTotalPages) {
            likeyCurrentPage++;

            // 새 페이지 정보 갱신
            $("#likeyPageInfo").data("likey-current-page", likeyCurrentPage);
            $("#likeyPageInfo").text(likeyCurrentPage + ' / ' + likeyTotalPages);

            pageReload(likeyCurrentPage);
        }
    });

    function pageReload(likeyCurrentPage){
        $.ajax({
            url: "/mypage",
            method: "get",
            data: {pageIdx: likeyCurrentPage},
            success: function (data){
                var data = $.parseHTML(data);
                var dataHtml = $("<div>").append(data);
                $("#likeyContent").replaceWith(dataHtml.find("#likeyContent"));
            }
        })
    }
}

