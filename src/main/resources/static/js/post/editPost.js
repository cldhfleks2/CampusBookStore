$(document).ready(function() {
    initialize();
    dragEvent();
    imageRemove();
    editPost();
});

function initialize(){
    if ($('.image-wrapper').length > 0) {
        $('.imageUpload').css({
            'border': 'none',
            'display': 'flex',
            'caret-color': 'transparent'
        });
    }
}

// Drag and drop event handling
function dragEvent(){
    $('.imageUpload').on('dragover', function(event) {
        event.preventDefault();
        $(this).addClass('on');
    });

    $('.imageUpload').on('dragleave', function(event) {
        event.preventDefault();
        $(this).removeClass('on');
    });

    $('.imageUpload').on('drop', function(event) {
        event.preventDefault();
        $(this).removeClass('on');

        const transferFiles = event.originalEvent.dataTransfer.files;
        displayImages(transferFiles);
    });
}

// 업로드 이미지 보여주기
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

        // Hide upload icon and text when images are added
        $(".uploadIcon").hide();
        $(".uploadText").hide();
        $('.imageUpload').css('border', 'none');
        $(".imageUpload").css("caret-color", "transparent");
        $(".imageUpload").css("display", "flex");
    });
}

// 이미지 삭제
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

// 수정 내용을 서버로 전달
function editPost() {
    $(".submitButton").on("click", function (e) {
        e.preventDefault();
        var postId = $(this).data("post-id");

        var formData = new FormData();

        formData.append("id", postId);
        formData.append("title", $("input[name='title']").val());
        formData.append("author", $("input[name='author']").val());
        formData.append("price", $("input[name='price']").val());
        formData.append("content", $("textarea[name='content']").val());
        formData.append("quantity", $("input[name='quantity']").val());

        $(".image-wrapper input[type='file']").each(function() {
            if (this.files && this.files.length > 0) {
                formData.append("images", this.files[0]);
            }
        });

        $.ajax({
            url: "/editPost",
            method: "post",
            contentType: false,
            processData: false,
            data: formData,
            success: function () {
                window.location.href = "/detailPost/" + postId;
            },
            error: function (err) {
                console.log(err);
                alert("게시글 수정 중 오류가 발생했습니다.");
            }
        });
    });
}