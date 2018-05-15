$(document).ready(function() {
    $('.nav-tabs').scrollingTabs({
        disableScrollArrowsOnFullyScrolled: true
    });

    $('.search-input').hideseek({
       highlight: true,
       nodata: 'Nenhum registro encontrado.',
       ignore_accents: true,
       attribute: 'data-values'
    });

    $('.datepicker').datepicker({
        format: 'dd/mm/yyyy',
        language: 'pt-BR'
    });

    $('*[data-chart-multi-dataset]').each(function() {
        var datasources = $(this).data('datasources');
        var labels = $(this).data('labels')
        var id = $(this).attr("id");
        var datasets = [];

        for (var key in datasources) {
            datasets.push({
                label: key,
                data: datasources[key],
                backgroundColor: randomColor()
            });
        }

        new Chart(document.getElementById(id), {
            type: 'bar',
            data: {
                labels: labels,
                datasets: datasets
            },
           options: {
                scales: {
                    yAxes: [{
                        stacked: true,
                        ticks: {
                            beginAtZero: true
                        }
                    }],
                    xAxes: [{
                        stacked: true,
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        })
    });

    $('*[data-chart]').each(function() {
        var id = $(this).attr("id");
        var type = $(this).data('chart')
        var labels = $(this).data('chart-labels')
        var label = $(this).data('chart-label')
        var dataChart = $(this).data('chart-data')
        var title = $(this).data('chart-title')
        var beginAtZero = $(this).data('begin-at-zero')

        var options = {};
        var data = {
            labels: labels,
            datasets: [{
                label: label,
                data: dataChart
            }]
        }

        if (type == 'bar') {
            options.scales = {
                xAxes: [{ gridLines: { display: true }, scaleLabel: { display: true, labelString: title }, ticks: { beginAtZero: beginAtZero } }],
                yAxes: [{ gridLines: { display: true }, scaleLabel: { display: true, labelString: label }, ticks: { beginAtZero: beginAtZero } }]
            }
            data.datasets[0].backgroundColor = randomColor();
        } else if (type == 'doughnut') {
            data.datasets[0].backgroundColor = [];
            for (var i = 0; i < dataChart.length; i++) {
                data.datasets[0].backgroundColor.push(randomColor());
            }

            options.pieceLabel = {
              render: 'value',
              fontSize: 14,
              fontStyle: 'bold',
              fontColor: '#fff',
              fontFamily: '"Lucida Console", Monaco, monospace'
            }
        }

        new Chart(document.getElementById(id), {
            type: type,
            data: data,
            options: options
        });
    });

    $('a[data-method]').click(function (event) {
        event.preventDefault();
        if ($(this).data('confirm')) {
            if (!confirm($(this).data('confirm'))) {
                return false;
            }
        }

        var form = $('<form></form>');
        form.attr('action', $(this).attr('href'));
        form.attr('method', 'POST');

        var field = $('<input></input>');
        field.attr('type', 'hidden');
        field.attr('name', '_method');
        field.attr('value', $(this).data('method'));

        form.append(field);

        $(document.body).append(form);
        form.submit();
        return false;
    });

    $('#fluxColumn_select a').click(function() {
        var value = $(this).text().trim();
        var input = $('#fluxColumn');
        input.val(input.val() + value + ', ');
        return false;
    });
})

function randomColor() {
    return '#'+(function lol(m,s,c){return s[m.floor(m.random() * s.length)] +
      (c && lol(m,s,c-1));})(Math,'0123456789ABCDEF',4)
}