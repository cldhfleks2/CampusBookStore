$(document).ready(function() {
    dragEvent()
    imageRemove();
    upload();
    dynamicCategoryInitialize()
});

//드래그 앤 드랍 컨트롤
function dragEvent(){
    // Drag over event
    $('.imageUpload').on('dragover', function(event) {
        event.preventDefault();
        $(this).addClass('on');
    });

    // Drag leave event
    $('.imageUpload').on('dragleave', function(event) {
        event.preventDefault();
        $(this).removeClass('on');
    });

    // Drop event
    $('.imageUpload').on('drop', function(event) {
        event.preventDefault();
        $(this).removeClass('on');

        const transferFiles = event.originalEvent.dataTransfer.files;
        displayImages(transferFiles);
    });
}

// 이미지를 div태그에 보여주기
function displayImages(transferFiles) {
    var imageFileList = [];
    var fileNum = transferFiles.length;

    for (let i = 0; i < fileNum; i++) {
        if (!transferFiles[i].type.match("image.*")) {
            return;
        }
        imageFileList.push(transferFiles[i]);
    }

    var imagePreviewArea = $('.imageUpload');

    imageFileList.forEach(function(imageFile) {
        const fileReader = new FileReader();
        fileReader.onload = function(event) {
            const image = new Image();
            image.src = event.target.result;
            $(image).attr("name", "image");

            const hiddenFileInput = $("<input>", {
                type: "file",
                name: "uploadedFile",
                style: "display:none;"
            });

            const imageWrapper = $("<div>", {
                class: "image-wrapper",
            });

            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(imageFile);
            hiddenFileInput[0].files = dataTransfer.files;

            imageWrapper.append(image);
            imageWrapper.append(hiddenFileInput);

            imagePreviewArea.prepend(imageWrapper);
        };

        fileReader.readAsDataURL(imageFile);
        
        //이미지를 하나라도 추가하면 이미지 영역의 아이콘 지움
        $(".uploadIcon").hide();
        $(".uploadText").hide();
        $('.imageUpload').css('border', 'none');
        $(".imageUpload").css("caret-color", "transparent"); //커서깜빡임 제거
        $(".imageUpload").css("display", "flex");
    });
}

//이미지 클릭시 이미지태그를 지움
function imageRemove(){
    $('.imageUpload').on('click', '.image-wrapper', function(e) {
        // Remove the entire image-wrapper div
        $(this).remove();

        // If no images left, show upload icon and text, restore border
        if ($('.image-wrapper').length === 0) {
            $(".uploadIcon").show();
            $(".uploadText").show();
            $('.imageUpload').css('border', '2px dashed #ccc');
            $(".imageUpload").css("caret-color", "auto");
            $(".imageUpload").css("display", "block");
        }
    });

}

//책 등록
function upload() {
    $(".submitButton").on("click", function (e) {
        e.preventDefault();

        var formData = new FormData();

        formData.append("title", $("input[name='title']").val());
        formData.append("author", $("input[name='author']").val());
        formData.append("price", $("input[name='price']").val());
        formData.append("content", $("textarea[name='content']").val());
        formData.append("quantity", $("input[name='quantity']").val());
        formData.append("campus", $("input[name='campus']").val());
        formData.append("major", $("input[name='major']").val());

        $(".image-wrapper input[type='file']").each(function() {
            if (this.files && this.files.length > 0) {
                formData.append("images", this.files[0]);
            }
        });

        const categorys = $('.categoryInput').map(function() {
            return $(this).val().trim();
        }).get().filter(category => category !== ''); // 빈 값 제거
        // Append each category separately
        categorys.forEach(category => {
            formData.append('categorys', category);
        });

        $.ajax({
            url: "/addPost",
            method: "post",
            contentType: false,
            processData: false,
            data: formData,
            success: function () {
                console.log("addPost-ajax-success")
                window.location.href = "/main";
            },
            error: function (err) {
                console.log(err)
                console.log("addPost-ajax-failed")
                alert("게시글 작성 중 오류가 발생했습니다.");
            }
        })


    })
}








// 카테고리 박스 크기를 동적으로 설정
function adjustInputWidth(input) {
    // Create a temporary span to measure text width
    const tempSpan = $('<span>').hide().appendTo('body');
    tempSpan.text(input.val());
    tempSpan.css({
        'font-size': input.css('font-size'),
        'font-family': input.css('font-family')
    });

    // Calculate width, limit to 200px
    let textWidth = tempSpan.width() + 20; // Add padding
    textWidth = Math.min(textWidth, 200);

    input.css('width', textWidth + 'px');
    tempSpan.remove();
}

// 동적으로 카테고리 박스를 추가
function addCategoryBox() {
    const newBox = $('<div class="categoryBox">' +
        '<span class="removeCategory">×</span>' +
        '<input type="text" class="categoryInput" placeholder="카테고리를 입력하세요">' +
        '</div>');

    $('#categoriesContainer').append(newBox);

    // Get the new input and adjust its width
    const newInput = newBox.find('.categoryInput');
    newInput.focus();

    // Dynamic width adjustment on input
    newInput.on('input', function() {
        adjustInputWidth($(this));
    });

    // Remove category functionality
    newBox.find('.removeCategory').click(function() {
        // Prevent removing the last category box
        if ($('#categoriesContainer .categoryBox').length > 1) {
            $(this).closest('.categoryBox').remove();
        }
    });
}

function dynamicCategoryInitialize(){
    // 모든 카테고리 input에 대해 width 조정
    $('.categoryInput').each(function() {
        adjustInputWidth($(this));
    });


    // Remove category functionality for initial box
    $('.removeCategory').click(function() {
        // Prevent removing the last category box
        if ($('#categoriesContainer .categoryBox').length > 1) {
            $(this).closest('.categoryBox').remove();
        }
    });

// Add new category box on Enter key in an input
    $('#categoriesContainer').on('keydown', '.categoryInput', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault(); // Prevent form submission

            // Only add a new box if the current input has some text
            if ($(this).val().trim() !== '') {
                addCategoryBox();
            }
        }
    });
}










//TODO 유효성 검사를 할까 말까..






