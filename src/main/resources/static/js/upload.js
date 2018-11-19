$(document).ready(function () {

    $("#uploadSubmitButton").click(function (e) {
        e.preventDefault();

        $(".successMsg").css('display', 'none');
        $(".errorMsgTooBig").css('display', 'none');
        $(".errorMsgBadFile").css('display', 'none');
        $(".errorMsg").css('display', 'none');

        var file = document.getElementById('image-file').files[0];
        var fd = new FormData();
        fd.append('file', file);
        var xhr = new XMLHttpRequest();

        xhr.open('post', '/upload', true);
        xhr.setRequestHeader('X-CSRF-TOKEN', $('meta[name="_csrf"]').attr('content'));
        xhr.send(fd);
        xhr.onload = function (ev) {
            if (xhr.status === 200) {
                $(".successMsg")
                    .css('display', 'inline-block')
                    .attr('href', xhr.response)
            } else if (xhr.status === 400) {
                $(".errorMsgTooBig").css('display', 'inline-block')
            } else if (xhr.status === 415) {
                $(".errorMsgBadFile").css('display', 'inline-block')
            } else {
                $(".errorMsg").css('display', 'inline-block');
                alert(xhr.response)
            }
        }
    });
});