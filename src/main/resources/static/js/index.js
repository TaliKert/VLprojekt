$(document).ready(function () {
     // $.getScript("https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js") // validaatori jaoks vaja importida siit
    $('.navbar-toggler').click(function () {
        console.log('clicked');
        $('.navbar-toggler-icon').toggleClass('rotateButton-active');
    });
});

