$(document).ready(function() {
    //책 이미지
    initImageSlider();

    //책 관련 모달, 버튼
    modal();
    likeBtn();
    wishBtn();
    //리뷰
    addReview();
    editReviewBtn();
});

function reBinding(){
    addReview();
    editReviewBtn();
}

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
        var postId = $(this).data("post-id");

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
        var postId = $(this).data("post-id");
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

//리뷰 리스트 갱신
function reviewListUpdate(){
    $.ajax({
        url:"/reviewList",
        method:"get",
        async: false,
        success: function (data){
            $("#reviewSection").replaceWith(data);
            // console.log(data);
            console.log("/reviewList ajax complete")
        },
        fail: function (err){
            console.log(err);
            return; //실패시 리바인딩 하지 않음
        }
    })
    reBinding() //성공하면 리바인딩 시켜서 on click등이 다시 작동하도록
}

//리뷰 작성
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
                reviewListUpdate()

                console.log("/reviewSubmit ajax complete");
            }, fail: function (err){
                console.log(err);
            }

        })

    });
}

//리뷰 수정
function editReviewBtn() {
    $(".editReviewBtn").on("click", function() {
        var reviewId = $(this).data("review-id");
        var author = $(this).data("author-name");
        var reviewItem = $(this).closest(".reviewItem");
        var originalTitle = reviewItem.find(".reviewTitle").text();
        var originalContent = reviewItem.find(".reviewContent").text();

        // 기존 리뷰 영역을 수정 폼으로 대체
        reviewItem.html(`
            <div class="editReviewForm">
                <input type="hidden" name="reviewId" value="${reviewId}">
                <div class="addReviewInputs">
                    <div class="addReviewTitle">
                        <input type="text" name="editReviewTitle" value="${originalTitle}" placeholder="리뷰 제목을 입력하세요" class="addReviewTitleInput">
                    </div>
                    <textarea placeholder="리뷰 내용을 입력하세요" name="editReviewContent" class="addReviewContent">${originalContent}</textarea>
                </div>
                <div class="editReviewActions">
                    <button class="submitEditReviewBtn">수정 완료</button>
                    <button class="cancelEditReviewBtn">취소</button>
                </div>
            </div>
        `);

        // 수정 취소 버튼 이벤트
        $(".cancelEditReviewBtn").on("click", function() {
            reviewListUpdate(); // 원래 리뷰 목록으로 되돌리기
        });

        // 수정 완료 버튼 이벤트
        $(".submitEditReviewBtn").on("click", function() {
            var editedId = reviewId;
            var editedTitle = $("input[name='editReviewTitle']").val();
            var editedAuthor = author;
            var editedContent = $("textarea[name='editReviewContent']").val();

            $.ajax({
                url: "/editReview",
                method: "post",
                data: {
                    id: editedId,
                    title: editedTitle,
                    author: editedAuthor,
                    content: editedContent
                },
                success: function() {
                    reviewListUpdate(); // 수정 후 리뷰 목록 새로고침
                    console.log("/editReview ajax complete");
                },
                fail: function(err) {
                    console.log(err);
                    alert("리뷰 수정 중 오류가 발생했습니다.");
                }
            });
        });
    });
}
