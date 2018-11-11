$(document).ready(function () {
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

    $('.input-group-addon').click(function () {
        $(this).prev().focus()
    });

    $('*[data-chart-multi-dataset]').each(function () {
        var datasources = $(this).data('datasources');
        var labels = $(this).data('labels');
        var id = $(this).attr("id");
        var stacked = $(this).data('stacked');
        var datasets = [];

        for (var key in datasources) {
            var colorRgb = randomColorRGB();
            var backgroundColor = colorRgb;
            var solidRgb = colorRgb.replace('rgba', 'rgb').split(',');
            var borderColor = solidRgb[0] + "," + solidRgb[1] + "," + solidRgb[2] + ")";
            datasets.push({
                label: key,
                data: datasources[key],
                backgroundColor: backgroundColor,
                borderColor: borderColor,
                borderWidth: 1
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
                        stacked: stacked,
                        ticks: {
                            beginAtZero: true
                        }
                    }],
                    xAxes: [{
                        stacked: stacked,
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                },
                plugins: {datalabels: {display: false}}
            }
        })
    });

    $('*[data-chart]').each(function () {
        var id = $(this).attr("id");
        var type = $(this).data('chart');
        var labels = $(this).data('chart-labels');
        var label = $(this).data('chart-label');
        var dataChart = $(this).data('chart-data');
        var title = $(this).data('chart-title');
        var beginAtZero = $(this).data('begin-at-zero');

        var options = {};

        var data = {
            labels: labels,
            datasets: [{
                label: label,
                data: dataChart
            }]
        };

        options.plugins = {
            datalabels: {
                color: '#FFF',
                font: {
                    weight: 'bold',
                    size: 20
                },
                display: function (ctx) {
                    return ctx.dataset.data[ctx.dataIndex] > 0 && ctx.chart.isDatasetVisible(ctx.datasetIndex);
                },
                formatter: function (value, ctx) {
                    return isNaN(value) || value % 1 === 0 || value.toFixed === undefined ? value : value.toFixed(2);
                }
            }
        };

        if (type === 'bar') {
            options.scales = {
                xAxes: [{
                    gridLines: {display: true},
                    scaleLabel: {display: true, labelString: title},
                    ticks: {beginAtZero: beginAtZero}
                }],
                yAxes: [{
                    gridLines: {display: true},
                    scaleLabel: {display: true, labelString: label},
                    ticks: {beginAtZero: beginAtZero}
                }]
            };

            data.datasets[0].backgroundColor = randomColor();
        } else {
            data.datasets[0].backgroundColor = [];
            for (var i = 0; i < dataChart.length; i++) {
                data.datasets[0].backgroundColor.push(randomColor());
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

    $('*[data-column-suggest]').click(function () {
        var value = $(this).text().trim();
        var input = $('#' + $(this).data('column-suggest'));
        var prefix = $(this).data('column-suggest-prefix') || '';
        var unique = $(this).data('column-suggest-unique') || false;
        var oldValue = input.val();

        if (unique) {
            input.val(value);
        } else {
            input.val(oldValue + (oldValue ? prefix : '') + value);
        }

        return false;
    });

    $('*[data-loader]').click(function () {
        $('#loader').fadeIn()
    });

    $(".collapse").on("hide.bs.collapse", function () {
        $(this).parent().find('.panel-heading').find('i').attr("class", "fa fa-chevron-right");
    });

    $(".collapse").on("show.bs.collapse", function () {
        $(this).parent().find('.panel-heading').find('i').attr("class", "fa fa-chevron-down");
    });

    $('#dynamicFields_add').click(function () {
        var $dynamicFieldId = $('#dynamicField_id');
        var $dynamicFieldName = $('#dynamicField_name');
        var $dynamicFieldField = $('#dynamicField_field');
        var indexValue = parseInt($dynamicFieldId.val());

        $('#dynamicFields_tbody')
            .append(
                '<tr>' +
                '<td>' +
                '<input type="text" class="form-control" name="dynamicFields[' + indexValue + '].name" value="' + $dynamicFieldName.val() + '">' +
                '</td>' +
                '<td>' +
                '<input type="text" class="form-control" name="dynamicFields[' + indexValue + '].field" value="' + $dynamicFieldField.val() + '">' +
                '</td>' +
                '<td>' +
                '<button class="btn btn-danger dynamicFields_remove"><i class="fa fa-trash"></i></button>' +
                '</td>' +
                '</tr>');

        $dynamicFieldId.val(indexValue + 1);
        $dynamicFieldName.val('');
        $dynamicFieldField.val('');
        $dynamicFieldField.selectpicker('')
    });

    $('.dynamicFields_remove').on('click', function () {
        $(this).parent().parent().remove();
    })
});

function randomColor() {
    return '#' + (function lol(m, s, c) {
        return s[m.floor(m.random() * s.length)] +
            (c && lol(m, s, c - 1));
    })(Math, '0123456789ABCDEF', 4)
}

function randomColorRGB() {
    var o = Math.round, r = Math.random, s = 255;
    return 'rgba(' + o(r() * s) + ',' + o(r() * s) + ',' + o(r() * s) + ', 0.2)';
}
