$(document).ready(function () {
    quantityBtn();
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

        let quantity = 1;

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
            }
        });

        $increaseBtn.on('click', () => {
            quantity++;
            updateItemPrice();
        });

        // 초기 가격 설정
        updateItemPrice();
    });
}