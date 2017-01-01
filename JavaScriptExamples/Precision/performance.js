goog.require('goog.dom');
goog.provide('slava.performance');

/**
   Interface
*/

/**
  Computes average time for execution of a parameterless function.
*/
slava.performance.performanceMeasure = function(f) {};

/**
  Runs function f1 and f2 on n values starting from startValue
  with step (endValue - startValue) / n.
  Returns object with properties
    times - object {t1, t2} average times per function execution,
    values - two value array, each is array of {x, value} - values of respective function,
    deltas - array of {x, value} - logarithms of differences of functions.
    worst - object {x, f1, f2, delta} - values of functions at x with worst delta.
*/
slava.performance.mathFunctionPerformanceComparator = function(f1, f2, startValue, endValue, n) {};

/**
  Implementation
*/
(function() {
   slava.performance.performanceMeasure = function(f) {
     var dt = performanceMeasure_(f, 1);
     var times = 1;
     while(dt < 50) {
       times = times * 2;
       dt = performanceMeasure_(f, times);
     }
     dt = 1.0 * dt / times;
     return dt;
  }
  performanceMeasure_ = function(f, times) {
     var t = new Date().getTime();
     for (var i = 0; i < times; i++) {
       f.apply(null, []);
     }
     var dt = new Date().getTime() - t;
     return dt;
  }
  slava.performance.mathFunctionPerformanceComparator = function (f1, f2, startValue, endValue, n) {
    var dt1 = slava.performance.performanceMeasure(function() {
               mathFunctionRunner(f1, startValue, endValue, n);
            });
    var dt2 = slava.performance.performanceMeasure(function() {
               mathFunctionRunner(f2, startValue, endValue, n);
            });
    var results = {};
    results.times = { t1: dt1, t2: dt2};
    var f1R = mathFunctionRunner(f1, startValue, endValue, n);
    var f2R = mathFunctionRunner(f2, startValue, endValue, n);
    results.values = [f1R, f2R];
    var delta = [];
    for (var i in f1R) {
      var s = Math.abs(f1R[i].value + f2R[i].value) / 2;
      var diff = Math.abs(f1R[i].value - f2R[i].value);
      if(s > 1) diff /= s;  // for large values - relative precision, for small - absolute precision.
      var diffLog = diff <= 0 ? -18 : Math.log10(diff);
      delta.push({x: f1R[i].x, value: diffLog});
    }
    results.deltas = delta;
    var j = max(delta);
    results.worst = {
      x: delta[j].x, f1: f1R[j].value, f2: f2R[j].value, delta: delta[j].value
    }
    
    return results;  
  }

  function max(delta) {
    var j = -1;
    var vm = -100;
    for (var i in delta) {
      if(delta[i].value > vm) {
        vm = delta[i].value;
        j = i;
      }
    }
    return j;
  }

  /*
    Runs function n times from startValue with step (endValue - startValue) / n.
    Returns array of objects {x, value}
  */
  function mathFunctionRunner(f, startValue, endValue, n) {
    var d = (endValue - startValue) / n;
    var x = startValue;
    var results = [];
    for (var i = 0; i < n; i++) {
      var a = f.apply(this, [x]);
      results.push({x: x, value: a});
      x += d;    
    }
    return results;
  }
})();

