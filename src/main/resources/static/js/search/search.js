$(document).ready(function() {
    inputSearchText();
    prevPaginationBtn();
    nextPaginationBtn();
    clickPageBtn();
});
//ajax로 검색결과를 동적으로 불러옴
function pageReload(searchText, currentPage){
    $.ajax({
        url: "/search",
        method: "get",
        data: {keyword: searchText, pageIdx: currentPage},
        success: function (data){
            var data = $.parseHTML(data);
            var dataHtml = $("<div>").append(data);
            $("#searchSection").replaceWith(dataHtml.find("#searchSection"));
            $("#searchSummary").replaceWith(dataHtml.find("#searchSummary"));
        },
        error: function (err){
            console.log(err);
            console.log("search-ajax-failed")
        }
    })
}
//실시간으로 검색 결과 표시
function inputSearchText(){
    $(document).on("input", "#liveSearchInput", function (){
        var searchText = $(this).val();
        pageReload(searchText, null)
    })
}
//이전 페이지 버튼
function prevPaginationBtn(){
    $(document).on('click',".pagination-prev", function() {
        let currentPage = $(".pagination-number.active").text();
        const searchText = $("#liveSearchInput").val();

        if (currentPage > 1) {
            currentPage--;

            pageReload(searchText, currentPage);
        }
    });
}
//다음 페이지 버튼
function nextPaginationBtn(){
    $(document).on('click',".pagination-next", function() {
        let currentPage = $(".pagination-number.active").text();
        const totalPages = $(".pagination").data("total-page");
        const searchText = $("#liveSearchInput").val();

        if (currentPage < totalPages) {
            currentPage++;

            pageReload(searchText, currentPage);
        }
    });
}
//페이지 버튼
function clickPageBtn(){
    $(document).on('click',".pagination-number", function() {
        let currentPage = $(this).text();
        const searchText = $("#liveSearchInput").val();

        pageReload(searchText, currentPage);
    });
}

