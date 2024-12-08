$(document).ready(function() {
    dragEvent()
    imageRemove();
    upload();
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
            // image.classList.add("image");
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

function upload() {
    $(".submitButton").on("click", function (e) {
        e.preventDefault();

        var formData = new FormData();

        formData.append("title", $("input[name='title']").val());
        formData.append("author", $("input[name='author']").val());
        formData.append("price", $("input[name='price']").val());
        formData.append("content", $("textarea[name='content']").val());

        $(".image-wrapper input[type='file']").each(function() {
            // Check if there are actual files
            if (this.files && this.files.length > 0) {
                formData.append("images", this.files[0]);
            }
        });

        $.ajax({
            url: "/addPost",
            method: "post",
            contentType: false,
            processData: false,
            data: formData,
            success: function (data) {
                window.location.href = "/main";
            },
            fail: function (err) {
                console.log(err)
            }

        })


    })
}



