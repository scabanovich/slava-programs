goog.provide('slava.precision');

//Interface

slava.precision.LOWEST = 0.5;
slava.precision.LOW = 0.2;
slava.precision.HIGH = 0.04;

slava.precision.value = slava.precision.LOW;

slava.cos = function(x) {}

slava.exp = function(x) {}

slava.sin = function(x) {
  return slava.cos(x - Math.PI / 2);
}

slava.tan = function(x) {
  return slava.sin(x) / slava.cos(x);
}

slava.power = function(x, p) {
  if(p == 0) return 1;
  if(p == 1) return x;
  if(p % 2 == 0) {
    var r = slava.power(x, p / 2);
    return r * r;
  }
  r = slava.power(x, (p - 1) / 2);
  return r * r * x;
}

slava.log = function(x) {};

//Implementation
(function() {

  slava_e = function(x) {
    return {x: x, p: Math.exp(x), i: Math.exp(-x)};
  }
  var EP = [slava_e(1), slava_e(0.5), slava_e(0.25), slava_e(0.125), slava_e(0.0625), slava_e(0.03125), slava_e(0.03125 / 2)];

  slava.log = function(x) {
    if(x <= 0) throw "Argument out of range";
    var result = 0;
    while(x > EP[0].p) {
      result++;
      x *= EP[0].i;
    }
    while(x < EP[0].i) {
      result--;
      x *= EP[0].p;
    }
    for (var i = 1; i < EP.length; i++) {
      if(x > EP[i].p) { 
        x *= EP[i].i; result += EP[i].x;
      } else if(x < EP[i].i) {
        x *= EP[i].p; result -= EP[i].x;
      }
    }
    x = 1 - x;
    var x2 = x * x; var x3 = x2 * x; var x4 = x3 * x; var x5 = x4 * x; var x6 = x5 * x; var x7 = x6 * x; var x8 = x7 * x;
    result -= x + x2 * 0.5 + x3 * 0.333333333333333333 + x4 * 0.25 + x5 * 0.2 + x6 * 0.16666666666666667 + x7 * 0.142857142857142857 + x8 * 0.125;
    return result;
  }

  slava.exp = function(x) {
    if(x == 0) return 1;
    var b = (x < 0);
    if(b) x = -x;
    var p = Math.floor(x);
    x -= p;
    var result = 1;
    for (var i = 1; i < EP.length; i++) {
      if(x > EP[i].x) { 
        result *= EP[i].p; x -= EP[i].x;
      }
    }
    var x2 = x * x; var x3 = x2 * x; var x4 = x3 * x; var x5 = x4 * x; var x6 = x5 * x; var x7 = x6 * x;
    result *= 1 + x + x2 * 0.5 + x3 * 0.16666666666666667 + x4 * 0.041666666666666664
             + x5 * 0.008333333333333333 + x6 * 0.001388888888888889 + x7 * 0.0001984126984126984;

    result *= slava.power(Math.E, p);
    if(b) result = 1 / result;
    return result;
  }

  slava.cos = function(x) {
    var n = Math.floor(x / Math.PI + 0.5);
    x -= n * Math.PI;
    if(x < 0) x = -x;
    var y = Math.PI / 2 - x;
    if(y < 0.01) {
      var y2 = y * y;
      var y3 = y2 * y * 0.16666666666666667;
      var y5 = y2 * y3 * 0.05;
      var y7 = y2 * y5 * 0.023809523809523808;
      var r = y - y3 + y5 - y7;
      return n % 2 == 0 ? r : -r;
    }
    var t = 0;
    var xm = slava.precision.value;
    while(x > xm) {
      t++;
      x *= 0.5;
    }
    var x2 = x * x * 0.5;
    var x4 = x2 * x2 * 0.16666666666666667; // /6
    var x6 = x2 * x4 * 0.06666666666666667; // /15
    var x8 = x2 * x6 * 0.03571428571428571; // /28
    var result = x2 - x4 + x6 - x8;
    while(t > 0) {
      result = (result + result) * (2 - result);
      t--;
    }
    return n % 2 == 0 ? 1 - result : result - 1;
  }

  slava.exp = function(x) {
    if(x == 0) return 1;
    var b = (x < 0);
    if(b) x = -x;
    var p = Math.floor(x);
    x -= p;
    var t = 0;
    var xm = slava.precision.value * 0.5;
    while(x > xm) {
      t++;
      x *= 0.5;
    }
    var x2 = x * x; var x3 = x2 * x; var x4 = x3 * x; var x5 = x4 * x; var x6 = x5 * x; var x7 = x6 * x;
    var result = 1 + x + x2 * 0.5 + x3 * 0.16666666666666667 + x4 * 0.041666666666666664
               + x5 * 0.008333333333333333 + x6 * 0.001388888888888889 + x7 * 0.0001984126984126984;
    while(t > 0) {
      result = result * result;
      t--;
    }
    result *= slava.power(Math.E, p);
    if(b) result = 1 / result;
    return result;
  }

})();
