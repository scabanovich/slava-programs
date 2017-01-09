goog.provide('water');
goog.provide('slava.water');

goog.require('goog.net.XhrIo');

slava.water.problems = null;
slava.water.problem = null;
water.state = null;
slava.water.history = [];

slava.water.load = function(callback) {
  if(slava.writers) return;
  goog.net.XhrIo.send('water.json', function(e) {  
      var xhr = e.target;
      slava.water.problems = xhr.getResponseJson().problems;
      slava.water.setProblem(0);
      callback.apply(this);
  });
  
  //wait for request, and after timeout if it fails load some default problems.
  setTimeout(function() {
    if(slava.water.problems == null) {
      //alert('Failed to load file with problems from disk. Fall back to coded problems'); 
      var json = { problems: [
        {id: 1, volumes: [12,19,14], quantities: [7,12,9], level: "easy"},
        {id: 2, volumes:  [71, 67, 29], quantities:  [50, 7, 25], level: "medium"},
        {id: 3, volumes: [53,94,90,55], quantities: [39,75,76,38], level: "hard"},
        {id: 4, volumes: [71,96,73,90], quantities: [49,76,50,69], level: "hard"},
        {id: 5, volumes: [55,55,53,92], quantities: [36,38,34,73], level: "advanced"},
        {id: 6, volumes: [84,115,112,82,113], quantities: [61,98,88,67,89], level: "challenging"},
        {id: 7, volumes: [71,111,117,73,119], quantities: [48,88,94,57,98], level: "challenging"},
        {id: 8, volumes: [159,167,167,125,123], quantities: [135,126,143,104,89], level: "extreme"},
        {id: 9, volumes: [114,192,186,151,116], quantities: [85,153,138,126,106], level: "extreme"},
        {id: 10, volumes: [183,175,126,128], quantities: [136,134,91,100], level: "extreme"},
        ]
      };
      slava.water.problems = json.problems;
      slava.water.setProblem(0);
      callback.apply(this);  
    }
  }, 1000);
}

slava.water.setProblem = function(index) {
    slava.water.problem = slava.water.problems[index];
    water.state = {
        quantities: slava.water.problem.quantities
    };
    slava.water.history = [];
}

water.getProblemDescription = function(problem) {
  var s = 0;
  for (i in problem.quantities) {
    s += problem.quantities[i];
  }
  return "You have " +  + s + " ml of water in glasses of " + problem.volumes.join(', ') + " ml";
}

water.move = function(from, to) {
  this.from = from;
  this.to = to;
  this.initQuantities();
};

water.move.prototype.initQuantities = function() {
  if(this.to != this.from) {
    this.quantities = [];
    for (i in water.state.quantities) {
      this.quantities.push(water.state.quantities[i]);
    }
    var n = water.state.quantities[this.from] + water.state.quantities[this.to];
    if(n <= slava.water.problem.volumes[this.to]) {
      this.quantities[this.to] = n;
      this.quantities[this.from] = 0;
    } else {
      this.quantities[this.to] = slava.water.problem.volumes[this.to];
      this.quantities[this.from] = n - slava.water.problem.volumes[this.to];
    }
  }
}

water.move.prototype.setTo = function(to) {
    if(water.state.quantities[to] == slava.water.problem.volumes[to]) return false;
    this.to = to;
    this.initQuantities();
    return true;
}

water.move.prototype.isSet = function() {
    return this.from != this.to;
}

slava.water.considerMove = function(row) {
    var history = slava.water.history;
    if(row < 0) {
        if(history.length == 0) return;
        if(!history[history.length - 1].isSet()) {
            history.splice(history.length - 1, 1);
        }
        return 1; //slava.water.ui.writeHistory();
    }
    if(history.length == 0 || history[history.length - 1].isSet()) {
        if(water.state.quantities[row] == 0) return;
        history.push(new water.move(row, row));
        return 1; //slava.water.ui.writeHistory();
    } else if(row != history[history.length - 1].from) {
        if(!history[history.length - 1].setTo(row)) {
            history[history.length - 1].from = row;
            history[history.length - 1].to = row;
            return 1; //slava.water.ui.writeHistory();
        }
        history[history.length - 1].previousQuantities = water.state.quantities;
        water.state.quantities = history[history.length - 1].quantities;
        slava.water.ui.drawGlasses();
        slava.water.ui.writeHistory();
        return 2; //slava.water.ui.drawGlasses();
    }
    return 0; //no changes
}

slava.water.solve = function(volumes, quantities) {
    var stack = [];
    var visited = {};
    var path = {};
    var source = {};
    stack.push(quantities);
    visited['' + quantities.join(',')] = 0;
    var c = 0;
    while(c < stack.length) {
        var current = stack[c];
        var cKey = '' + current.join(',');
        var moves = visited[cKey];
        for (i = 0; i < quantities.length; i++) {
            for (j = 0; j < quantities.length; j++) {
                if(i == j) continue;
                var next = current.slice();
					if(current[j] + current[i] <= volumes[j]) {
						next[i] = 0;
						next[j] = current[j] + current[i];
					} else {
						next[j] = volumes[j];
						next[i] = current[i] + current[j] - volumes[j];
					}
					var key = '' + next.join(',');
					if(!visited.hasOwnProperty(key)) {
						if(next[i] == 1 || next[j] == 1) {
							visited[key] = moves + 1;
							path[key] = cKey;
							source[key] = {from: i, to: j};
							var solution = [];
							while(key) {
							  if(source.hasOwnProperty(key)) {
							      solution.unshift(source[key]);
							  }
							  key = path[key];
							}
							return solution;
						}
						visited[key] = moves + 1;
						path[key] = cKey;
						source[key] = {from: i, to: j};
						stack.push(next);
					}                
            }
        }
        c++;
    }
}
