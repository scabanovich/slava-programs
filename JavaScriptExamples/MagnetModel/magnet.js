goog.provide('slava.magnet');

goog.require('goog.dom');

slava.magnet.Magnet = function() {
    this.setSize(50);
    this.setTemperatureAndField(300, 0.2);
}

slava.magnet.Magnet.prototype.setSize = function(size) {
    this.size = size; 
    this.state = [];
    for (i = 0; i < size; i++) {
        var a = [];
        for (j = 0; j < size; j++) {
          a.push(1);
        }
        this.state.push(a);
    }
    this.magnetizationGraph = [];
    for (i = 0; i < 120; i++) {
        this.magnetizationGraph.push(0);
    }
}

slava.magnet.Magnet.prototype.setTemperature = function(temperature) {
    this.setTemperatureAndField(temperature, this.externalField);
}

slava.magnet.Magnet.prototype.setField = function(externalField) {
    this.setTemperatureAndField(this.temperature, externalField);
}

slava.magnet.Magnet.prototype.setTemperatureAndField = function(temperature, externalField) {
    this.temperature = temperature;
    this.externalField = externalField;
    if(temperature == 0) temperature = 1;
    var _weights = [];
    //down
    var w = [];
    for (i = 0; i <= 4; i++) {
        w.push(Math.exp( - ( - externalField - (2 * i - 4) ) / temperature * 200));
    }
    _weights.push(w);
    //up
    w = [];
    for (i = 0; i <= 4; i++) {
        w.push(Math.exp( - ( externalField + (2 * i - 4) ) / temperature * 200));
    }
    _weights.push(w);
    this.weights = _weights;
}

slava.magnet.Magnet.prototype.flip = function() {
    var x = Math.floor(Math.random() * this.size);
    var y = Math.floor(Math.random() * this.size);
    var s = this.state[x][y] == 1 ? 1 : 0;
    var sum = 0;
    var x1 = (x == this.size - 1) ? 0 : x + 1;
    if(this.state[x1][y] == 1) sum++;
    x1 = (x == 0) ? this.size - 1 : x - 1;
    if(this.state[x1][y] == 1) sum++;
    var y1 = (y == this.size - 1) ? 0 : y + 1;
    if(this.state[x][y1] == 1) sum++;
    y1 = (y == 0) ? this.size - 1 : y - 1;
    if(this.state[x][y1] == 1) sum++;
    var wU = this.weights[1][sum];
    var wD = this.weights[0][sum];
    var p = wU / (wU + wD);
    this.state[x][y] = (Math.random() > p) ? 1 : -1;
}

slava.magnet.Magnet.prototype.flipRound = function() {
    for (t = 0; t < 10; t++) {
    	for (i = 0; i < this.size * this.size; i++) {
        	this.flip();
    	}
    }
  	this.magnetizationGraph.splice(0, 0, this.getMagnetization());
  	this.magnetizationGraph.splice(this.magnetizationGraph.length - 1, 1);
}

slava.magnet.Magnet.prototype.getMagnetization = function() {
    var m = 0;
    for (i = 0; i < this.size; i++) {
        for (j = 0; j < this.size; j++) {
          m += this.state[i][j];
        }
    }
    return parseFloat(m / this.size / this.size);
}

slava.magnet.instance = new slava.magnet.Magnet();
