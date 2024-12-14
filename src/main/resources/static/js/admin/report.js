$(document).ready(function (){
    clickMenuBar();
    checkbox();
    display();
})


function clickMenuBar(){
    $(document).on("click", ".menuItem", function () {
        $(".menuItem").removeClass("active"); //모든 active제거
        $(this).addClass("active"); //현재 활성화한것에만 active추가
    });
}

function checkbox() {
    const $selectAllRowsCheckbox = $('#selectAllRows');
    const $rowCheckboxes = $('.row-checkbox');

    // 전체 선택/해제 기능
    $selectAllRowsCheckbox.on('change', function() {
        $rowCheckboxes.prop('checked', this.checked);
    });

    // 개별 선택 시 전체 선택 체크박스 상태 업데이트
    $rowCheckboxes.on('change', function() {
        const allChecked = $rowCheckboxes.length === $rowCheckboxes.filter(':checked').length;
        $selectAllRowsCheckbox.prop('checked', allChecked);
    });
}

function display(){
    // 선택 제외 버튼
    $('.accept-btn').on('click', function() {
        const selectedIds = $('.row-checkbox:checked').map(function() {
            return $(this).data('id');
        }).get();

        if (selectedIds.length > 0) {
            alert(`선택된 신고 ID: ${selectedIds.join(', ')} 를 제외합니다.`);
            // 실제 구현 시 서버 통신 로직 추가
        } else {
            alert('선택된 항목이 없습니다.');
        }
    });

    // 선택 삭제 버튼
    $('.reject-btn').on('click', function() {
        const selectedIds = $('.row-checkbox:checked').map(function() {
            return $(this).data('id');
        }).get();

        if (selectedIds.length > 0) {
            alert(`선택된 신고 ID: ${selectedIds.join(', ')} 를 삭제합니다.`);
            // 실제 구현 시 서버 통신 로직 추가
        } else {
            alert('선택된 항목이 없습니다.');
        }
    });
}