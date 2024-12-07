$(document).ready(function() {
    // 상품신고, 리뷰신고 버튼 누르면 모달창 보여지게.
    $("#productReportBtn, #reviewReportBtn").on("click", function() {
        $("#reportOverlay").css("display", "flex");
    });

    // "아니오" 버튼 클릭 시 모달 닫기
    $(".noBtn").on("click", function() {
        $("#reportOverlay").css("display", "none");
    });

    // "예" 버튼 클릭 시 신고 처리 후 모달 닫기
    $(".yesBtn").on("click", function() {
        $("#reportOverlay").css("display", "none");
        alert("신고가 완료되었습니다.");
    });
});