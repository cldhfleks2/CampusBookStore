$(document).ready(function (){
    clickMenuBar();
    checkbox();
    btns();
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

function btns(){
    function postViewReload(){
        $.ajax({
            url: "/admin/report",
            method: "get",
            success: function (data){
                var data = $.parseHTML(data);
                var dataHtml = $("<div>").append(data);
                $("#postContainer").replaceWith(dataHtml.find("#postContainer"));
                console.log("reportPostViewReload-ajax-success");
            },
            fail: function (err) {
                console.log(err)
                console.log("reportPostViewReload-ajax-failed");
            }
        })
    }

    // 선택 제외 버튼
    $(document).on("click", ".accept-btn", function () {
        const selectedReportIds = $('.row-checkbox:checked').map(function() {
            return $(this).data('report-id');
        }).get();

        if (selectedReportIds.length > 0) {
            $.ajax({
                url: "/admin/report/ignore",
                method: "delete",
                contentType: "application/json", // JSON 형식 지정
                data: JSON.stringify(selectedReportIds), // 데이터를 JSON 문자열로 변환
                success: function (){

                    postViewReload();

                    console.log("ignoreReportPost-ajax-complete");
                    alert(`선택된 신고 ID: ${selectedReportIds.join(', ')} 를 제외합니다.`);
                },
                fail: function (err) {
                    console.log(err)
                    console.log("ignoreReportPost-ajax-failed");
                }
            })
        } else {
            alert('선택된 항목이 없습니다.');
        }
    });

    // 선택 삭제 버튼
    $(document).on("click", ".reject-btn", function () {
        const selectedReportIds = $('.row-checkbox:checked').map(function() {
            return $(this).data('report-id');
        }).get();

        if (selectedReportIds.length > 0) {
            $.ajax({
                url: "/admin/report/post/delete",
                method: "delete",
                contentType: "application/json",
                data: JSON.stringify(selectedReportIds),
                success: function (){

                    postViewReload();

                    console.log("reportPostDelete-ajax-complete");
                    alert(`선택된 신고 ID: ${selectedReportIds.join(', ')} 를 삭제합니다.`);
                },
                fail: function (err) {
                    console.log(err)
                    console.log("reportPostDelete-ajax-failed");
                }
            })
        } else {
            alert('선택된 항목이 없습니다.');
        }
    });
}