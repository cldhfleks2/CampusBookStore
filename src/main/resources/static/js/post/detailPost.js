$(document).ready(function() {
    //책 이미지
    initImageSlider();
    //책 관련 모달, 버튼

    report();
    likeyBtn();
    wishBtn();
    quantityBtn()

    //리뷰
    addReview();
    editReviewBtn();
    deleteReviewBtn();

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

    //다음 이미지
    $(document).on('click', '.slider-next', function() {
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
    
    //이전 이미지
    $(document).on('click', '.slider-prev', function() {
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

//신고하기 개발중
function report() {
    $(document).on("click", "#productReportBtn, #reviewReportBtn", function() {
        modalON();
        $("#reportOverlay .modalMessage").text("신고하시겠습니까?");
        $("#reportOverlay").css("display", "flex");
    });
    $(".noBtn").off("click").on("click", function() {
        modalOFF();
    });

    //예 버튼 클릭
    $(document).off("click", ".yesBtn").on("click",".yesBtn", function() {
        modalOFF();
        alert("신고가 완료되었습니다.");
        report($(this).data("name"));
    });
}

//게시물번호로 좋아요 갯수를 가져와서 갱신
function getLikeyCount(postId){
    $.ajax({
        url: "/likeyCount",
        method: "get",
        data: {postId: postId},
        success: function (data){
           $("#likeCount").text(data); //좋아요 갯수 갱신
        },
        fail: function (err) {
            console.log(err);
        }
    })
}

//찜하기(좋아요)추가 요청
function likeyBtn(){
    //내가 좋아요를 눌렀는지 확인 //0: 안좋아요   1: 좋아요
    var iamlikey = $(".likeCount").data("iamlikey");
    if(iamlikey === 1)
        $(".likeBtn").addClass("fill")
    console.log(iamlikey)

    $(document).on("click", ".likeBtn", function () {
        var postId = $(this).data("post-id");

        //찜한 리스트임을 서버로 전송
        $.ajax({
            url:"/likePlus",
            method:"post",
            data: {postId: postId},
            success: function (data){
                //"likey" 를 받으면 
                //likey버튼에 fill 클래스 추가
                if(data === "likey"){
                    $(".likeBtn").addClass("fill");
                }
                
                //"noLikey"를 받으면
                //fill클래스 제거
                if(data === "noLikey"){
                    $(".likeBtn").removeClass("fill");
                }
                //css도추가
                
                console.log("like-ajax-complete")
                getLikeyCount(postId); //좋아요 갯수 갱신
            },
            fail: function (err){ //본인 게시물에 좋아요를 누르거나 등
                //note: 실패시 아무것도 하지않음

                console.log("like-ajax-failed")
            }
        })
    })
}

//장바구니 추가 버튼
function wishBtn(){
    $(document).on("click", ".wishBtn", function () {
        var postId = $(this).data("post-id");
        var quantity = $("input[name='quantity']").val();
        console.log(quantity)

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
}

//리뷰 작성
function addReview(){
    $(document).on("click", ".submitReviewBtn", function () {
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
    $(document).on("click", ".editReviewBtn", function (){
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
        $(document).on("click", ".cancelEditReviewBtn", function() {
            reviewListUpdate(); // 원래 리뷰 목록으로 되돌리기
        });

        // 수정 완료 버튼 이벤트
        $(document).on("click", ".submitEditReviewBtn", function() {
            var editedId = reviewId;
            var editedTitle = $("input[name='editReviewTitle']").val();
            var editedAuthor = author;
            var editedContent = $("textarea[name='editReviewContent']").val();

            $.ajax({
                url: "/editReview",
                method: "patch",
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

//리뷰 삭제
function deleteReviewBtn() {
    $(document).on("click", ".deleteReviewBtn", function() {
        var reviewId = $(this).data("review-id");
        var author = $(this).data("author-name");
        var reviewItem = $(this).closest(".reviewItem");
        var originalTitle = reviewItem.find(".reviewTitle").text();
        var originalContent = reviewItem.find(".reviewContent").text();

        // 모달창 보이기
        $("#reportOverlay .modalMessage").text("리뷰를 삭제하시겠습니까?");
        $("#reportOverlay").css("display", "flex");
        
        //기존에 달린 이벤트리스너 해제 후 재등록

        $(document).off("click", ".yesBtn").on("click",".yesBtn", function() {
            $.ajax({
                url: "/deleteReview",
                method: "delete",
                data: {
                    id: reviewId,
                    title: originalTitle,
                    author: author,
                    content: originalContent
                },
                success: function() {
                    modalOFF();

                    //리뷰란 갱신
                    reviewListUpdate();

                    console.log("/deleteReview ajax complete");
                },
                fail: function(err) {
                    console.log(err);
                    alert("리뷰 삭제 중 오류가 발생했습니다.");
                }
            });
        });

        // 취소할때
        $(document).off("click", ".noBtn").on("click",".noBtn", function() {
            modalOFF();
        });


    });
}

function quantityBtn() {
    const $decreaseBtn = $('.quantityDecrease');
    const $increaseBtn = $('.quantityIncrease');
    const $quantityDisplay = $('.quantityDisplay');
    const $quantityInput = $("#quantityInput");
    const $totalPriceDisplay = $('.totalPriceDisplay');

    const $basePriceElement = $('.price');
    const $basePrice = parseInt($basePriceElement.text().replace(/,/g, ''), 10);

    let quantity = 1;

    const updateTotalPrice = () => {
        const totalPrice = $basePrice * quantity;
        $totalPriceDisplay.text(totalPrice.toLocaleString() + '원');
        $quantityInput.val(quantity);  // Use .val() for input
        $quantityDisplay.text(quantity);

    };

    $decreaseBtn.on('click', () => {
        if (quantity > 1) {
            quantity--;
            updateTotalPrice();
        }
    });

    $increaseBtn.on('click', () => {
        quantity++;
        updateTotalPrice();
    });
}
