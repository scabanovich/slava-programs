goog.require('goog.dom');

slava.magnet.getTableSize = function() {
    return slava.magnet.instance.size <= 30 ? slava.magnet.instance.size : 30;
}

slava.magnet.createTable = function(id) {
    var t = document.getElementById('magnetStateTable');
    var magnet = slava.magnet.instance;
    var size = slava.magnet.getTableSize();
    for (i = 0; i < size; i++) {
        var row = goog.dom.createDom('tr', {});
        for (j = 0; j < size; j++) {
            var cell = goog.dom.createDom('td', {id: 'td_' + i + '_' + j, class: magnet.state[i][j] == 1 ? 'arrowUp' : 'arrowDown'});
            var ch = magnet.state[i][j] == 1 ? '&uarr;' : '&darr;';
            cell.innerHTML = ch;
            goog.dom.appendChild(row, cell);
        }
        goog.dom.appendChild(t, row);
    }
}
    
slava.magnet.updateTable = function(id) {
    var magnet = slava.magnet.instance;
    var size = slava.magnet.getTableSize();
    for (i = 0; i < size; i++) {
         for (j = 0; j < size; j++) {
            var cell = document.getElementById('td_' + i + '_' + j);
            var ch = magnet.state[i][j] == 1 ? '&uarr;' : '&darr;';
            cell.innerHTML = ch;
            cell.className = magnet.state[i][j] == 1 ? 'arrowUp' : 'arrowDown';
        }
    }
    //document.getElementById('magnetization').innerHTML = "" + magnet.getMagnetization().toFixed(5);
    slava.magnet.magnetizationGauge.data.setCell(0, 1, "" + magnet.getMagnetization().toFixed(5));
    slava.magnet.magnetizationGauge.chart.draw(
        slava.magnet.magnetizationGauge.data,
        slava.magnet.magnetizationGauge.options
    );
    slava.magnet.drawMagnetizationHistory();
}

slava.magnet.drawTemperatureGauge = function() {
    var data = google.visualization.arrayToDataTable([
      ['Label', 'Value'],
      ['Temperature', slava.magnet.instance.temperature]
    ]);
    var options = {
        width: 220, height: 220,
        max: 1000,
        greenFrom: 0, greenTo: 350,
        yellowFrom:350, yellowTo: 500,
        redFrom:   500, redTo:  1000,
        minorTicks: 10
    };

    var chart = new google.visualization.Gauge(document.getElementById('temperatureGauge'));
    chart.draw(data, options);
        $( function() {
             $( "#temperatureSlider" ).slider({
                 max: 1000,
                 value: slava.magnet.instance.temperature,
                 change: function( event, ui ) {
                     slava.magnet.instance.setTemperature(ui.value);
                     data.setCell(0, 1, slava.magnet.instance.temperature);
                     chart.draw(data, options);
                 }
             });
        } );
}

slava.magnet.drawFieldGauge = function() {
    var maxField = 2;
    var minField = -2;
    var data = google.visualization.arrayToDataTable([
          ['Label', 'Value'],
          ['Field', slava.magnet.instance.externalField]
    ]);
    var options = {
          width: 220, height: 220,
          max: maxField,
          min: minField,
          minorTicks: 10
    };

    var chart = new google.visualization.Gauge(document.getElementById('fieldGauge'));
    chart.draw(data, options);
    $( function() {
         $( "#fieldSlider" ).slider({
             max: maxField,
             min: minField,
             step: 0.1,
             value: slava.magnet.instance.externalField,
             change: function( event, ui ) {
                 slava.magnet.instance.setField(ui.value);
                 data.setCell(0, 1, slava.magnet.instance.externalField);
                 chart.draw(data, options);
             }
         });
     } );
}

slava.magnet.magnetizationGauge = {
    data: null,
    chart: null,
    options: null
};

function drawMagnetizationGauge() {
    var maxM = 1;
    var minM = -1;
    var data = google.visualization.arrayToDataTable([
          ['Label', 'Value'],
          ['Magnetization', 0.7]
    ]);
    slava.magnet.magnetizationGauge.data = data;
    var options = {
          width: 320, height: 320,
          max: maxM,
          min: minM,
          minorTicks: 10
    };

    var chart = new google.visualization.Gauge(document.getElementById('magnetizationGauge'));
    chart.draw(data, options);
    slava.magnet.magnetizationGauge.chart = chart;
    slava.magnet.magnetizationGauge.options = options;
}
     
slava.magnet.drawMagnetizationHistory = function() {
    var a = [];
    a.push(['Time', 'Magnetization']);
    for (i = 0; i < slava.magnet.instance.magnetizationGraph.length; i++) {
      a.push([i * 0.5, parseFloat(parseFloat(slava.magnet.instance.magnetizationGraph[i]).toFixed(4))]);
    }
    var data = new google.visualization.arrayToDataTable(a);
    var options = {
          title: '',
          width: 500,
          height: 200,
          legend: { position: 'none' },
          chart: { title: 'Performance comparison'},
          curveType: 'function',
          hAxis: {direction: -1}
    };
    var chart = new google.visualization.LineChart(document.getElementById('magnetizationHistory'));
    chart.draw(data, options);
}

     