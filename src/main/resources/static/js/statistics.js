var browserData;
var osData;
var trafficData;


$(document).ready(function () {

    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
        }
    });

    function getBrowsers() {
        return $.get('/statistics/browsers')
            .done(function (data) {
                browserData = data;
            });
    }

    function getOSs() {
        return $.get('/statistics/os')
            .done(function (data) {
                osData = data;
            });
    }

    function getTraffic() {
        return $.get('/statistics/traffic')
            .done(function (data) {
                trafficData = data;
            });
    }


    $.when(getBrowsers()).then(function () {
        var dataLabels = [];
        var dataData = [];
        for (var i = 0; i < browserData.length; i++) {
            dataLabels.push(browserData[i][0]);
            dataData.push(browserData[i][1])
        }

        var ctx = document.getElementById("browserChart").getContext('2d');
        var browserPieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: dataLabels,
                datasets: [{
                    label: '# of Requests',
                    data: dataData,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {}
        });
    });

    $.when(getOSs()).then(function () {
        var dataLabels = [];
        var dataData = [];
        for (var i = 0; i < osData.length; i++) {
            dataLabels.push(osData[i][0]);
            dataData.push(osData[i][1])
        }

        var ctx = document.getElementById("osChart").getContext('2d');
        var osPieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: dataLabels,
                datasets: [{
                    label: '# of Requests',
                    data: dataData,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {}
        });
    });

    $.when(getTraffic()).then(function () {
        var dataLabels = ['00', '01', '02', '03', '04',
            '05', '06', '07', '08', '09',
            '10', '11', '12', '13', '14',
            '15', '16', '17', '18', '19',
            '20', '21', '22', '23']
        var ctx = document.getElementById("trafficChart").getContext('2d');
        var trafficLineChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: dataLabels,
                datasets: [{
                    label: '# of Requests per hour',
                    data: trafficData,
                    borderColor: 'rgba(255, 159, 64, 1)',
                    borderWidth: 2
                }]
            },
            options: {}
        });
    });
});