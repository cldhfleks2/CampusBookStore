$(document).ready(function() {
    initImageSlider();

    modal();
    likeBtn();
    wishBtn();

    addReview()
});

//책 그림 슬라이드
function initImageSlider() {
    const $wrapper = $('.slider-wrapper');
    const $items = $('.slider-item');
    const $prevBtn = $('.slider-prev');
    const $nextBtn = $('.slider-next');
    let currentIndex = 0;
    const totalItems = $items.length;

    // Hide prev/next buttons if only one image
    if (totalItems <= 1) {
        $prevBtn.hide();
        $nextBtn.hide();
        return;
    }

    // Prepare the slider for circular navigation
    $wrapper.css('display', 'flex');
    $wrapper.css('width', `${totalItems * 100}%`);
    $items.css('flex', `0 0 ${100 / totalItems}%`);

    $nextBtn.on('click', function() {
        // Create a smooth transition for next slide
        $wrapper.css('transition', 'transform 0.5s ease');

        currentIndex++;
        if (currentIndex >= totalItems) {
            // Reset to first slide after last slide
            currentIndex = 0;

            // Instant reset without transition
            $wrapper.css('transition', 'none');
            updateSliderPosition();

            // Force reflow and restore transition
            $wrapper.offset();
            $wrapper.css('transition', 'transform 0.5s ease');
        }

        updateSliderPosition();
    });

    $prevBtn.on('click', function() {
        // Create a smooth transition for previous slide
        $wrapper.css('transition', 'transform 0.5s ease');

        currentIndex--;
        if (currentIndex < 0) {
            // Go to last slide when before first slide
            currentIndex = totalItems - 1;

            // Instant reset without transition
            $wrapper.css('transition', 'none');
            updateSliderPosition();

            // Force reflow and restore transition
            $wrapper.offset();
            $wrapper.css('transition', 'transform 0.5s ease');
        }

        updateSliderPosition();
    });

    function updateSliderPosition() {
        const offset = -currentIndex * (100 / totalItems);
        $wrapper.css('transform', `translateX(${offset}%)`);
    }
}

//신고 모달 ON
function modalON(){
    $("#reportOverlay").css("display", "flex");
}

//신고 모달 OFF
function modalOFF() {
    $("#reportOverlay").css("display", "none");
}

//모달창과 버튼
function modal(){
    $("#productReportBtn, #reviewReportBtn").on("click", function() {
        modalON()
    });
    noBtn();
    yesBtn();
}

//모달창의 예 버튼
function yesBtn(){
    $(".yesBtn").on("click", function() {
        modalOFF();
        alert("신고가 완료되었습니다.");
        report($(this).data("name"));
    });
}

//모달창의 아니오 버튼
function noBtn(){
    $(".noBtn").on("click", function() {
        modalOFF();
    });
}

//신고하기 개발중
function report(name) {
    if(name === "book"){

    }else if(name === "review"){

    }
}

//찜하기(좋아요)추가 요청
function likeBtn(){
    $(".likeBtn").on("click", function () {
        var postId = $(this).data("postId");

        //찜한 리스트임을 서버로 전송
        $.ajax({
            url:"/likePlus",
            method:"post",
            data: {postId: postId},
            success: function (){
                console.log("like-ajax-complete")
            },
            fail: function (err){
                console.log(err);
            }
        })
    })
}

//장바구니 추가 버튼
function wishBtn(){
    $(".wishBtn").on("click", function (){
        var postId = $(this).data("postId");
        $.ajax({
            url: "/wishPlus",
            method: "post",
            data: {postId: postId},
            success: function (){
                console.log("wish-ajax-complete")
            }, fail: function (err){
                console.log(err)
            }
        })
    })
}

//리뷰 작성 란
function addReview(){
    $(".submitReviewBtn").on("click", function (){
        var title = $("input[name='reviewTitle']").val(); //value값
        var author =$(".myname").text(); //text값
        var content = $("textarea[name='reviewContent']").val(); //value값


        //리뷰 작성 -> 컨트롤러에서 DB에 저장하는 등 로직 진행
        $.ajax({
            url: "/reviewSubmit",
            method: "post",
            data: {
                title: title,
                author: author,
                content: content
            },
            success: function (){
                //리뷰 리스트 갱신
                $.ajax({
                    url:"/reviewList",
                    method:"get",
                    async: false,
                    success: function (data){
                        $(".reviewList").replaceWith(data);
                        console.log(data);
                        console.log("/reviewList ajax complete")
                    },
                    fail: function (err){
                        console.log(err);
                    }
                })

                console.log("/reviewSubmit ajax complete");
            }, fail: function (err){
                console.log(err);
            }

        })

    });
}

