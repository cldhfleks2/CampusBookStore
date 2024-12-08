$(document).ready(function() {
    modal();

    wishlistBtn()
});

//모달창과 버튼에 할당
function modal(){
    $("#productReportBtn, #reviewReportBtn").on("click", function() {
        modalON()
    });
    noBtn();
    yesBtn();
}

function yesBtn(){
    $(".yesBtn").on("click", function() {
        modalOFF();
        alert("신고가 완료되었습니다.");
        report($(this).data("name"));
    });
}

function noBtn(){
    $(".noBtn").on("click", function() {
        modalOFF();
    });
}

function report(name) {
    if(name === "book"){

    }else if(name === "review"){

    }
}

function wishlistBtn(){
    $(".wishlistBtn").on("click", function () {
        var postId = $(this).data("postId");
        var memberId = $(this).data("memberId");

        //찜한 리스트임을 서버로 전송
        $.ajax({
            url:"/wish",
            method:"post",
            data: {postId: postId, memberId:memberId },
            success: function (data){
                console.log("wish-ajax-complete")
            },
            fail: function (data){
                console.log(data);
            }
        })
    })
}

function modalON(){
    $("#reportOverlay").css("display", "flex");
}

function modalOFF() {
    $("#reportOverlay").css("display", "none");
}