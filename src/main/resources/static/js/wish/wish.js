$(document).ready(function () {
    quantityBtn();
    deleteBtn();
    updateCartInfo();
})



function quantityBtn(){
    $('.wishItem').each(function() {
        const $wishItem = $(this);
        const $decreaseBtn = $wishItem.find('.quantityDecrease');
        const $increaseBtn = $wishItem.find('.quantityIncrease');
        const $quantityDisplay = $wishItem.find('.quantityDisplay');
        const $quantityInput = $wishItem.find('.quantityInput');

        // 가격 추출 (콤마 제거 후 숫자로 변환)
        const $bookPriceElement = $wishItem.find('.bookPrice');
        const basePrice = parseInt($bookPriceElement.text().replace(/,/g, ''), 10);

        let quantity = parseInt($quantityDisplay.text(), 10);
        let MAXquantity = quantity; //초깃값이 즉 최댓값인상태임

        const updateItemPrice = () => {
            const totalPrice = basePrice * quantity;
            $bookPriceElement.text(totalPrice.toLocaleString() + '원');
            $quantityInput.val(quantity);
            $quantityDisplay.text(quantity);
        };

        $decreaseBtn.on('click', () => {
            if (quantity > 1) {
                quantity--;
                updateItemPrice();

                updateCartInfo(); //총 금액 계산
            }
        });

        $increaseBtn.on('click', () => {
            if( quantity < MAXquantity){
                quantity++;
                updateItemPrice();

                updateCartInfo();
            }
        });

        // 초기 가격 설정
        updateItemPrice();
    });
}


//총 상품 금액, 포인트 계산 함수
function updateCartInfo(){
    //현재 총 금액 계산
    let totalPrice = 0;
    $('.bookPrice').each(function() {
        var priceText = $(this).text().trim();
        var price = priceText.replace(' 원', '').replace(/,/g, '');
        totalPrice += parseInt(price, 10) || 0; // NaN 방지
    });
    $(".calculatePrice").text(totalPrice.toLocaleString()); // 콤마로 구분하여 출력

    // 현재 보유 포인트
    var currentPoint = parseInt($(".currentPoint").text().trim().replace('P', '').replace('원', '').replace(/,/g, ''), 10) || 0;

    // 구매 후 예상 포인트 계산
    var remainPoint = currentPoint - totalPrice;
    $(".remainPoint").text(remainPoint >= 0 ? remainPoint.toLocaleString() : "포인트가 부족합니다.");
}


//TODO: 구매하기 버튼



//TODO: deleteBtn클래스 구현
function deleteBtn(){
    $(document).on("click", ".deleteBtn", function (){
        var wishId = $(this).data("wish-id");

        if(!wishId) return; //id가 업으면 에러

        $.ajax({
            url: "/wishDelete",
            method: "delete",
            data: {wishId: wishId},
            success: function (data){
                //비동기 갱신
                var data = $.parseHTML(data);
                var dataHtml = $("<div>").append(data);
                $("#wishList").replaceWith(dataHtml.find("#wishList"));

                console.log("delete-ajax-complete")
            },
            fail: function (err){
                console.log(err);
            }
        })
    })
}