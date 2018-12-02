// ORIGINAL CODE, DO NOT STEAL

$(document).ready(function () {
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
        }
    });

    connect();
    window.onbeforeunload = function () {
        disconnect();
    };

    window.currentBreakPointIndex = determineCurrentBreakPointIndex();

    if (window.location.pathname.split("/").slice(-1)[0] !== "") {
        window.lastThumbID = window.location.pathname.split("/").slice(-1)[0];
        window.activeImageElementId = "t" + window.lastThumbID;
        $.getThumbnail(window.lastThumbID).done(function () {
            refreshContainerPosition(document.getElementById(activeImageElementId));
        })
    }

    $.getThumbnailAfter(lastThumbID).done(function () {
        appendNewElementsUntilScreenFull();
    });

    $(window).scroll(function () {
        if (!scrollUpdateInProgress) appendNewElementsUntilScreenFull();
    });

    $('#ratingUp').click(function () {
        $.ajax({
            url: '/image/' + activeImageElementId.substr(1) + '/rate/true',
            type: 'post'
        }).done(function () {
            $.ajax({
                url: '/image/' + activeImageElementId.substr(1) + '/info',
                type: 'get'
            }).done(function (data) {
                updateRating(data.rating)
            });
        })
    });

    $('#ratingDown').click(function () {
        $.ajax({
            url: '/image/' + activeImageElementId.substr(1) + '/rate/false',
            type: 'post'
        }).done(function () {
            $.ajax({
                url: '/image/' + activeImageElementId.substr(1) + '/info',
                type: 'get'
            }).done(function (data) {
                updateRating(data.rating)
            });
        })
    });

    $('#submitCommentButton').click(function () {
        $.ajax({
            url: '/image/' + activeImageElementId.substr(1) + '/comment',
            type: 'post',
            data: {
                'authorName': '',
                'text': $('#commentTextInput').val()
            }
        }).done(function () {
            $.ajax({
                url: '/image/' + activeImageElementId.substr(1) + '/info',
                type: 'get'
            }).done(function (data) {
                updateComments(data.comments)
            });
        });
    });
});

var thumbnails = [];
var firstThumbID;
var lastThumbID;

var windowBreakPoints = [550, 690, 830, 980, 1120, 1300, Infinity];
var thumbsPerRowArray = [3, 4, 5, 6, 7, 8, 9];
var currentBreakPointIndex;

var stompClient = null;

// id of image currently opened. null if there is none
var activeImageElementId = null;

var lastImageReached = false;
var firstImageReached = false; // This is important, when we go to an URL with a specified image and the newer images are not loaded

var scrollUpdateInProgress = false;


function addNewThumbnailBefore() { // adding to before is more expensive than adding after, something to fix
    $('#stream').prepend(
        '<a class="thumbcontainer" href="/image/' + thumbnails[0].fileName + '">' +
        '<img src="' + '/thumb/' + thumbnails[0].id + '" />' +
        '</a>'
    );
    refreshRows();
}

$.addNewThumbnailAfter = function () {
    var dfd = $.Deferred();

    var lastRow = $("div span.thumbRow").last();
    if (lastRow.length === 0 || lastRow[0].childElementCount >= thumbsPerRowArray[currentBreakPointIndex]) {
        $('#stream').append("<span class='thumbRow'></span>");
        lastRow = $("div span.thumbRow").last();
    }
    var thumbnailElement = '<a id="t' + thumbnails[thumbnails.length - 1].id + '" class="thumbcontainer" href="/image/' + thumbnails[thumbnails.length - 1].fileName + '" >' +
        '<img class="thumbImage" src="' + '/thumb/' + thumbnails[thumbnails.length - 1].id + '" />' +
        '</a>';
    $(thumbnailElement).appendTo(lastRow).click(function (e) {
        e.preventDefault();
        if (activeImageElementId === e.currentTarget.id) {
            activeImageElementId = null;
            $('#itemContainer').css('display', 'none');
            history.pushState('', '', '/');
            return;
        }
        activeImageElementId = e.currentTarget.id;
        refreshContainerPosition(e.currentTarget);
        history.pushState('', '', '/entry/' + activeImageElementId.substr(1));
    });

    dfd.resolve();
    return dfd;
};

function getThumbnailBefore(firstThumbId) {
    // TODO: implement
}

$.getThumbnailAfter = function (lastThumbID) {
    var dfd = $.Deferred();
    $.ajax({
        url: '/thumb/after/' + lastThumbID,
        type: 'get'
    }).done(function (thumbData, statusText, xhr) {
        if (xhr.status === 204) {
            lastImageReached = true;
            dfd.resolve();
        } else {
            thumbnails.push(thumbData);
            window.lastThumbID = thumbData.id;
            $.addNewThumbnailAfter().done(function () {
                dfd.resolve();
            });
        }
    });

    return dfd;
};

$.getThumbnail = function (thumbID) {
    var dfd = $.Deferred();
    $.ajax({
        url: '/thumb/exact/' + thumbID,
        type: 'get'
    }).done(function (thumbData, statusText, xhr) {
        thumbnails.push(thumbData);
        window.lastThumbID = thumbData.id;
        $.addNewThumbnailAfter().done(function () {
            dfd.resolve();
        });
    });
    return dfd;
};

function refreshRows() {
    // console.log('breakpoint triggered, index ' + currentBreakPointIndex);
    $("span.thumbRow").contents().unwrap();
    var thumbContainers = $("#stream > .thumbcontainer");
    for (var i = 0; i < thumbContainers.length; i += thumbsPerRowArray[currentBreakPointIndex]) {
        thumbContainers.slice(
            i, i + thumbsPerRowArray[currentBreakPointIndex]
        ).wrapAll(
            "<span class='thumbRow'></span>"
        );
    }
}

function refreshContainerPosition(target) {
    // TODO move scrollbar to top of container
    $('#itemContainer')
        .insertAfter(target.parentNode)
        .css('display', 'inline-block');
    $('#itemContainer > img')
        .attr('src', target.pathname);
    $.ajax({
        url: '/image/' + target.id.substr(1) + '/info',
        type: 'get'
    }).done(function (data) {
        updateRating(data.rating);
        $('#infoBlockUploaderUsername').html(data.authorName).attr('href', '/u/' + data.authorName);
        updateComments(data.comments);
    });
    // $('html, body').animate({
    //     scrollTop: $("#itemContainer").offset().top - 58
    // },1000);
}

function updateRating(rating) {
    $('#ratingScore').html(rating);
}

function updateComments(comments) {
    $('#commentBlock').empty();
    for (var i = 0; i < comments.length; i++) {
        var comment = '<div>' +
            '<div><a href="/u/' + comments[i].authorName + '">' + comments[i].authorName + '</a></div>' +
            '<div>' + comments[i].text + '</div>' +
            '<hr>' +
            '</div>';
        $(comment).appendTo($('#commentBlock'));
    }
}

function determineCurrentBreakPointIndex() {
    for (var i = 0; i < window.windowBreakPoints.length; i++) {
        if ($(window).width() < windowBreakPoints[i]) {
            return i;
        }
    }
}

$(window).resize(function () {
    var breakPointIndex = determineCurrentBreakPointIndex();
    if (currentBreakPointIndex !== breakPointIndex) {
        currentBreakPointIndex = breakPointIndex;
        refreshRows();
        if (window.activeImage !== null) {
            refreshContainerPosition(
                document.getElementById(activeImageElementId)
            )
        }
    }
});

function appendNewElementsUntilScreenFull() {
    scrollUpdateInProgress = true;
    if ($(window).scrollTop() + $(window).height() + 200 > $("span.thumbRow:last").offset().top) {
        if (lastImageReached) {
            scrollUpdateInProgress = false;
            return;
        }
        $.getThumbnailAfter(lastThumbID).done(function () {
            appendNewElementsUntilScreenFull()
        });
    } else {
        scrollUpdateInProgress = false;
    }
}

// WEBSOCKET

function connect() {
    var socket = new SockJS('/realtime');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/streamupdate', function (thumbData) {
            if (firstImageReached) {
                thumbnails.unshift(JSON.parse(thumbData.body));
                addNewThumbnailBefore();
            }
        });
        stompClient.send("/app/streamsubscribe")
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}