$(document).ready(function() {
    //개인정보 수정 페이지를 기본으로 보여줌
    displayToggle("personalInfo");

    //사이드바 메뉴 활성화
    sidebarMenu();
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