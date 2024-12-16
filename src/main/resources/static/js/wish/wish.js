$(document).ready(function () {
    quantityBtn();
    deleteBtn();
    checkbox();
    purchaseBtn();
})

//항목별 수량 체크 버튼
function quantityBtn(){
    $('.wishItem').each(function() {
        const $wishItem = $(this);
        const $decreaseBtn = $wishItem.find('.quantityDecrease');
        const $increaseBtn = $wishItem.find('.quantityIncrease');
        const $quantityDisplay = $wishItem.find('.quantityDisplay');
        const $quantityInput = $wishItem.find('.quantityInput');
        let MAXquantity = $wishItem.find(".maxQuantity").text(); //최댓값은 wish.post.quantity에서 가져옴

        // 가격 추출 (콤마 제거 후 숫자로 변환)
        const $bookPriceElement = $wishItem.find('.bookPrice');
        const basePrice = parseInt($bookPriceElement.text().replace(/,/g, ''), 10);

        let quantity = parseInt($quantityDisplay.text(), 10);

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
        var thisParents = $(this).closest(".checkboxContainer");
        var check = thisParents.find(".wishItemCheckbox").is(':checked');
        //항목 가격 계산
        var priceText = $(this).text().trim();
        var price = priceText.replace(' 원', '').replace(/,/g, '');
        //체크 된것만 총 가격 계산
        if(check){
            totalPrice += parseInt(price, 10) || 0; // NaN 방지
        }
    });
    $(".calculatePrice").text(totalPrice.toLocaleString()); // 콤마로 구분하여 출력

    // 현재 보유 포인트
    var currentPoint = parseInt($(".currentPoint").text().trim().replace('P', '').replace('원', '').replace(/,/g, ''), 10) || 0;

    // 주문 후 예상 포인트 계산
    var remainPoint = currentPoint - totalPrice;
    $(".remainPoint").text(remainPoint >= 0 ? remainPoint.toLocaleString() : "포인트가 부족합니다.")
        .css("color", remainPoint >= 0 ? "black" : "red")
        .css("font-weight", remainPoint >= 0 ? "" : "bold");
}

//항목 삭제 버튼
function deleteBtn(){
    $(document).on("click", ".deleteBtn", function (){
        var wishId = $(this).data("wish-id");
        var thisParents = $(this).closest(".wishItem");
        if(!wishId) return; //id가 업으면 에러

        $.ajax({
            url: "/wishDelete",
            method: "delete",
            data: {wishId: wishId},
            success: function (){
                console.log("delete-ajax-success")

                //뷰 갱신
                thisParents.remove();
                // updateCartInfo();
            },
            error: function (err){
                console.log(err);
                console.log("delete-ajax-failed")
            }
        })
    })
}

//전체 체크 박스
function checkbox(){
    const $selectAllCheckbox = $('#selectAllCheckbox');
    const $wishItemCheckboxes = $('.wishItemCheckbox');
    // const $calculatePriceElement = $('.calculatePrice');
    // const $remainPointElement = $('.remainPoint');
    // const $currentPointElement = $('.currentPoint');

    // 전체 선택 체크박스 기능
    $selectAllCheckbox.on('change', function() {
        const isChecked = $(this).is(':checked');
        $wishItemCheckboxes.prop('checked', isChecked);
        // updateTotalPrice();
        updateCartInfo();
    });

    // 개별 체크박스 기능
    $wishItemCheckboxes.on('change', function() {
        // 모든 체크박스가 선택 되었으면 전체 선택을 체크해줌
        const allChecked = $wishItemCheckboxes.length === $wishItemCheckboxes.filter(':checked').length;
        $selectAllCheckbox.prop('checked', allChecked);
        // updateTotalPrice();
        updateCartInfo();
    });

    // // 총 선택된 상품 금액 계산
    // function updateTotalPrice() {
    //     let totalPrice = 0;
    //
    //     $wishItemCheckboxes.filter(':checked').each(function() {
    //         const price = parseInt($(this).data('price'));
    //         const quantity = parseInt($(this).data('quantity'));
    //         totalPrice += price * quantity;
    //     });
    //
    //     $calculatePriceElement.text(totalPrice.toLocaleString());
    //
    //     // 현재 포인트에서 총 가격을 뺀 나머지 포인트 계산
    //     const currentPoint = parseInt($currentPointElement.text().replace(/,/g, ''));
    //     const remainPoint = currentPoint - totalPrice;
    //     $remainPointElement.text(remainPoint.toLocaleString());
    // }
}

//주문하기 버튼
function purchaseBtn(){
    $(document).on("click", ".purchaseButton", function () {
        const totalPrice = parseInt($('.calculatePrice').text().replace(/,/g, ''));
        const currentPoint = parseInt($('.currentPoint').text().replace(/,/g, ''));
        if (totalPrice > currentPoint) {
            // 잔액 부족 시 모달 창 표시
            $('#checkoutModal').show();
            $('#modalMessage').text('잔액이 부족합니다.');
        } else {
            // 주문 완료 시 모달 창 표시
            $('#checkoutModal').show();
            $('#modalMessage').text('주문이 완료되었습니다.');

            console.log($('.wishItemCheckbox:checked').map(function() {
                return {
                    wishId: $(this).data('wish-id'),
                    quantity: parseInt($(this).closest('.wishItem').find('.quantityDisplay').text())
                };
            }).get())
            //TODO ajax요청
            $.ajax({
                url: "/order",
                method: "post",
                contentType: "application/json",
                data: JSON.stringify(
                    $('.wishItemCheckbox:checked').map(function() {
                        return {
                            wishId: $(this).data('wish-id'),
                            quantity: parseInt($(this).closest('.wishItem').find('.quantityDisplay').text())
                        };
                    }).get()
                ),
                success: function (data){
                    console.log(data)
                    console.log("order-ajax-success")
                },
                error: function (err){
                    console.log(err)
                    console.log("order-ajax-failed")
                }
            })

        }
    });

    //x 버튼
    $(document).on("click", ".close", function (){
        $(".modal").css("display", "none");
        if($("#modalMessage").text() === "주문이 완료되었습니다."){
            window.location.href = "/main";
        }
    });
}
