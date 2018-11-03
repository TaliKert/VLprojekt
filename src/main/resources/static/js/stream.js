var thumbnails = [];
var lastThumbID;

function addNewThumbnail() {
    $('#stream').append(
        '<a id="thumbcontainer" href="/image/' + thumbnails[thumbnails.length - 1].id + '">' +
            '<img src="' + '/thumb/' + thumbnails[thumbnails.length - 1].id + '" />' +
        '</a>'
    )
}

function getThumbnailAfter(lastThumbID) {
    $.get('/thumb/after/' + lastThumbID)
        .done(function (thumbData) {
            thumbnails.push(thumbData);
            window.lastThumbID = thumbData.id;
            addNewThumbnail()
        })
}


$(document).ready(function () {
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
        }
    });

    $("button.newimg").click(function () {
        getThumbnailAfter(lastThumbID)
    });
});