$(document).ready(function () {
    activeCategory();
    initialize();
})

function initialize(){
    var selectCategoryName = $(".categoryBubbles").data("initialize-select-category")
    viewActiveCategoryPost(selectCategoryName);
}

function activeCategory() {
    //카테고리 클릭시 active클래스를 붙임
    $(document).on("click", ".categoryBubble", function () {
        $(".categoryBubble").removeClass("active") //다른것은 전부 제거
        $(this).addClass("active")
        var categoryName = $(this).text();

        viewActiveCategoryPost(categoryName)
    })
}

function viewActiveCategoryPost(categoryName) {
    $.ajax({
        url: "/categoryView",
        method: "get",
        data: {categoryName: categoryName},
        success: function (data){
            var data = $.parseHTML(data);
            var dataHtml = $("<div>").append(data);
            $("#productSection").replaceWith(dataHtml.find("#productSection"));
            console.log("getCategoryPostView-ajax-success")
        },
        error: function (err){
            console.log(err)
            console.log("getCategoryPostView-ajax-failed")
        }
    })
}