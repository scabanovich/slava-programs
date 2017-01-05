goog.require('goog.dom');

goog.provide('slava.water.ui');

slava.water.ui.initProblemSelector = function() {
    var s1 = document.getElementById('problemSelector');
    for (i in slava.water.problems) {
        var w = slava.water.problems[i];
        var o = goog.dom.createDom('option', {value: w.id, selected: (i == 0)});
        goog.dom.appendChild(o, goog.dom.createTextNode(w.id + ' - ' + w.level));
        goog.dom.appendChild(s1, o);
    }
}

function createHandler(chart) {
    return function selectHandler(e) {
        var history = slava.water.history;
        var s = chart.getSelection();
        var row = -1;
        for (i in s) {
            row = s[i].row;
        }
        var result = slava.water.considerMove(row);
        if(result > 1) {
            slava.water.ui.drawGlasses();
        }
        if(result > 0) {
            slava.water.ui.writeHistory();
        }
    };
}
    
slava.water.onProblemSelected = function() {
    var i = document.getElementById('problemSelector').selectedIndex;
    slava.water.setProblem(i);
    slava.water.ui.drawGlasses();
    slava.water.ui.writeHistory();
}

slava.water.ui.drawGlasses = function() {
          var a = [];
          a.push(['Glasses', 'Water', 'Air']);
          for (i in water.state.quantities) {
            var w = water.state.quantities[i];
            var v = slava.water.problem.volumes[i];
            a.push(["" + (parseInt(i) + 1), w, v - w]);
          }
          var data = google.visualization.arrayToDataTable(a);
          var options = {
            width: 600,
            height: 400,
            title: water.getProblemDescription(slava.water.problem),
            legend: { position: 'bottom'},
            bar: { groupWidth: '75%' },
            colors: ['#88AAFF', '#DDDDDD'],
            isStacked: true,
          };
          var chart = new google.visualization.ColumnChart(document.getElementById("glasses"));
          chart.draw(data, options);
          google.visualization.events.addListener(chart, 'select', createHandler(chart));
}
      
slava.water.ui.writeHistory = function() {
    var elem = document.getElementById("history");
    var arr = [];
    for (i in slava.water.history) {
        var m = slava.water.history[i];
        arr.push((parseInt(i) + 1));
        arr.push('.  ');
        arr.push(m.from + 1);
        arr.push(':');
        if(m.to != m.from) {
            arr.push(m.to + 1);
        } else {
            arr.push('?');
        }
        arr.push('.  ');
    }
    elem.innerText = arr.join('');
    
    document.getElementById('back').style.visibility = (slava.water.history.length > 0) ? 'visible' : 'hidden';
}
      
slava.water.ui.back = function() {
    var history = slava.water.history;
    if(history.length == 0) return;
    if(history[history.length - 1].isSet()) {
        water.state.quantities = history[history.length - 1].previousQuantities;
    }
    history.splice(history.length - 1, 1);
    slava.water.ui.drawGlasses();
    slava.water.ui.writeHistory();
}
      
