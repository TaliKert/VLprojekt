$(document).ready(function () {

    document.getElementById('js-form').style.display = 'block';
    document.getElementById('nojs-form').style.display = 'none';

    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
        }
    });

    $('#js-form').submit(function () {
        $("input[type=submit]").attr("disabled", "disabled");
        document.getElementById('error-msg').style.display = 'none';
        $.ajax({
            url: '/register',
            type: 'POST',
            data: $('#js-form').serialize(),
            statusCode: {
                201: function () {
                    window.location.replace('/')
                },
                409: function () {
                    document.getElementById('error-msg').style.display = 'block';
                    $("input[type=submit]").removeAttr("disabled");
                }
            }
        });
    });
});