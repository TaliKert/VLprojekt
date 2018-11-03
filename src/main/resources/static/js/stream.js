// ORIGINAL CODE, DO NOT STEAL

var thumbnails = [];
var firstThumbID;
var lastThumbID;

var windowBreakPoints = [550, 690, 830, 980, 1120, 1300, Infinity];
var thumbsPerRowArray = [3, 4, 5, 6, 7, 8, 9];
var currentBreakPointIndex;

var stompClient = null;

var lastImageReached = false;
var firstImageReached = true; // This is important, when we go to an URL with a specified image and the newer images are not loaded

function addNewThumbnailBefore() { // adding to before is more expensive than adding after, something to fix
    $('#stream').prepend(
        '<a class="thumbcontainer" href="/image/' + thumbnails[0].id + '">' +
        '<img src="' + '/thumb/' + thumbnails[0].id + '" />' +
        '</a>'
    );
    refreshRows();
}

function addNewThumbnailAfter() {

    var lastRow = $("div span:last-child");
    if (lastRow.length === 0 || lastRow[0].childElementCount >= thumbsPerRowArray[currentBreakPointIndex]) {
        $('#stream').append("<span class='thumbRow'></span>");
        lastRow = $("div span:last-child");
    }
    lastRow.append(
        '<a class="thumbcontainer" href="/image/' + thumbnails[thumbnails.length - 1].id + '">' +
        '<img src="' + '/thumb/' + thumbnails[thumbnails.length - 1].id + '" />' +
        '</a>'
    )

}

function getThumbnailBefore(firstThumbId) {
    // TODO: implement
}

function getThumbnailAfter(lastThumbID) {
    $.ajax({
        url: '/thumb/after/' + lastThumbID,
        type: 'get'
    }).done(function (thumbData, statusText, xhr) {
        if (xhr.status === 204) {
            lastImageReached = true;
        } else {
            thumbnails.push(thumbData);
            window.lastThumbID = thumbData.id;
            addNewThumbnailAfter();
        }
    })
}

function refreshRows() {
    // console.log('breakpoint triggered, index ' + currentBreakPointIndex);
    $("span.thumbRow").contents().unwrap();
    var thumbContainers = $("#stream > .thumbcontainer");
    console.log(thumbContainers);
    for (var i = 0; i < thumbContainers.length; i += thumbsPerRowArray[currentBreakPointIndex]) {
        thumbContainers.slice(
            i, i + thumbsPerRowArray[currentBreakPointIndex]
        ).wrapAll(
            "<span class='thumbRow'></span>"
        );
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
        refreshRows()
    }
});

// WEBSOCKET

function connect() {
    var socket = new SockJS('/realtime');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
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

$(document).ready(function () {
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
        }
    });
    // Placeholder, untill I get around making infinite scrolling
    $("button.newimg").click(function () {
        if (lastImageReached) return;
        getThumbnailAfter(lastThumbID)
    });
    connect();
    window.currentBreakPointIndex = determineCurrentBreakPointIndex()

    window.onbeforeunload = function() {
        disconnect();
    };
});