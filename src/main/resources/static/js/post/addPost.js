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
            image.classList.add("image");
            imagePreviewArea.prepend(image);
        };

        fileReader.readAsDataURL(imageFile);
        
        //이미지를 하나라도 추가하면 이미지 영역의 아이콘 지움
        $(".uploadIcon").hide();
        $(".uploadText").hide();
        $('.imageUpload').css('border', 'none');
        $(".imageUpload").css("caret-color", "transparent"); //커서깜빡임 제거
    });
}

//이미지 클릭시 이미지태그를 지움
function imageRemove(){
    $('.imageUpload').on('click', 'img', function() {
        $(this).remove();

        // 이미지가 없을때 다시 이미지 영역의 아이콘 표시
        if ($('.imageUpload img').length === 0) {
            $(".uploadIcon").show();
            $(".uploadText").show();
            $('.imageUpload').css('border', '2px dashed #d1d5db');
        }
    });
}

function upload() {

}



