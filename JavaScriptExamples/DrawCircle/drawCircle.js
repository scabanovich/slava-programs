slava = {}
slava.circlegame = {}

slava.circlegame.path = [];
slava.circlegame.circle = null;

slava.circlegame.DrawTool = function() {
    this.active = false;
}

slava.circlegame.toPoint = function(e) {
    var c = document.getElementById("myCanvas");
    var x = e.pageX - c.offsetLeft;
    var y = e.pageY - c.offsetTop;
    while(c.parentNode != null) {
        c = c.parentNode;
        if(c.offsetLeft) {
        	x -= c.offsetLeft;
        }
        if(c.offsetTop) {
        	y -= c.offsetTop;
        }
        //alert(x);
    }
    return {x: x, y: y};
}

slava.circlegame.DrawTool.prototype.start = function(e) {
    this.active = true;
    slava.circlegame.path = [];
    slava.circlegame.circle = null;
    this.add(slava.circlegame.toPoint(e));
    slava.circlegame.paint();
}

slava.circlegame.DrawTool.prototype.add = function(point) {
    var last = slava.circlegame.path[slava.circlegame.path.length - 1];
    if(last && last.x == point.x && last.y == point.y) return;
    slava.circlegame.path.push(point);
}

slava.circlegame.DrawTool.prototype.draw = function(e) {
    if(!this.active) return;
    var last = slava.circlegame.path[slava.circlegame.path.length - 1];
    this.add(slava.circlegame.toPoint(e));
    slava.circlegame.paint();
}

slava.circlegame.DrawTool.prototype.stop = function(e) {
    this.active = false;
    slava.circlegame.circle = slava.circlegame.approximate();
    slava.circlegame.paint();
}

slava.circlegame.drawTool = new slava.circlegame.DrawTool();


slava.circlegame.paint = function() {
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    ctx.fillStyle="#DDDDDD";
    ctx.fillRect(0,0,500,500);
    ctx.strokeStyle="#000000";
    var path = slava.circlegame.path;
    if(path.length > 0) {
        ctx.beginPath();
        ctx.moveTo(path[0].x, path[0].y);
        for (var i = 1; i < path.length; i++) {
            ctx.lineTo(path[i].x, path[i].y);
        }
       // ctx.lineTo(path[0].x, path[0].y);
        ctx.stroke();
    }
    slava.circlegame.paintPrecision(ctx);
}

slava.circlegame.paintPrecision = function(ctx) {
    var reportElement = document.getElementById("report");
    if(slava.circlegame.circle != null) {
        var c = slava.circlegame.circle;
        ctx.strokeStyle="#FF0000";
        ctx.beginPath();
        ctx.arc(c.xc, c.yc, c.r, 0, 2 * Math.PI);
        ctx.stroke();
        if(!slava.circlegame.isCircle()) {
            reportElement.innerHTML = "Your curve does not look like a circle. Try again.";
        } else if(!slava.circlegame.isClosedCurve()) {
            reportElement.innerHTML = "You drew only a part of circle. Please do not cheat!";  
        } else {
            reportElement.innerHTML = "Your circle is " + slava.circlegame.getPrecision() + " perfect.";
        }
        reportElement.style.visibility = "visible";
    } else {
        reportElement.style.visibility = "hidden";
        reportElement.innerHTML = "";
    }
}

slava.circlegame.getPrecision = function() {
    if(slava.circlegame.circle == null) return 0;
    var c = slava.circlegame.circle;
    var rel = c.e / c.r * 10; //normalization
    if(rel > 1) return "0%";
    var res = "" + (1 - rel) * 100;
    var d = res.indexOf('.');
    if(res.length > d + 3) {
        res = res.substring(0, d + 3);
    }
    return res + '%';
}

slava.circlegame.isCircle = function() {
    if(slava.circlegame.circle == null) return false;
    var path = slava.circlegame.path;
    if(path.length < 3) return false;
    var rAppr = slava.circlegame.circle.r;
    if(rAppr > 250) return false;
    var worst = 0;
    var countBad = 0; 
    var xc = slava.circlegame.circle.xc;
    var yc = slava.circlegame.circle.yc;
    var x0 = path[0].x;
    var y0 = path[0].y;
    var mR = 0;
    for (i = 0; i < path.length; i++) {
        var x = path[i].x;
        var y = path[i].y;
        var ri = slava.circlegame.distance(x, y, xc, yc);
        var dr = Math.abs(rAppr - ri) / rAppr;
        if(dr > worst) {
            worst = dr;
            if(worst > 0.3) return false;
        }
        if(dr > 0.15) countBad++;
        var r0 = slava.circlegame.distance(x, y, x0, y0);
        if(r0 > mR) mR = r0;
    }
    if(countBad > path.length / 4) {
        return false;
    }
    if(mR < 1.5 * rAppr && slava.circlegame.isClosedCurve()) {
        return false;
    }
    return true;
}

slava.circlegame.isClosedCurve = function() {
    if(slava.circlegame.circle == null) return false;
    var path = slava.circlegame.path;
    if(path.length < 3) return false;
    var rAppr = slava.circlegame.circle.r;
    var x0 = path[0].x;
    var y0 = path[0].y;
    var x = path[path.length - 1].x;
    var y = path[path.length - 1].y;
    var ri = slava.circlegame.distance(x, y, x0, y0);
    return ri < 0.3 * rAppr;
}

slava.circlegame.approximate = function() {
    var xLeft = 0;
    var yUp = 0;
    var width = 500;
    var height = 500;
    var best = slava.circlegame.searchArrpIn(xLeft, yUp, width, height);
    if(best == null) return null;
    while(width > 2) {
        width /= 2;
        height /= 2;
        xLeft = best.xc - width / 2;
        yUp = best.yc - height / 2;
        var b = slava.circlegame.searchArrpIn(xLeft, yUp, width, height);
        if(b.e > best.e) break;
        best = b;
    }
    return best;
}

slava.circlegame.searchArrpIn = function(xLeft, yUp, width, height) {
    var best = slava.circlegame.checkAppr(xLeft + width / 2, yUp + height / 2);
    if(best == null) return null;
    for (ix = 0; ix < 8; ix++) {
      var x = xLeft + width * ix / 8;
      for (iy = 0; iy < 8; iy++) {
          var y = yUp + height * iy / 8;
          var res = slava.circlegame.checkAppr(x,y);
          if(res.e < best.e) {
              best = res;
          }
      } 
    }
    return best;
}

slava.circlegame.checkAppr = function(xc, yc) {
    var path = slava.circlegame.path;
    if(path.length < 3) return null;
    var rs = [];
    var r = 0;
    for (i = 0; i < path.length; i++) {
        var x = path[i].x;
        var y = path[i].y;
        rs[i] = slava.circlegame.distance(x, y, xc, yc);
        r += rs[i];
    }
    r /= rs.length;
    var d = 0;
    for (i = 0; i < path.length; i++) {
        d += (r - rs[i]) * (r - rs[i]);
    }
    var e = Math.sqrt(d / rs.length);
    return {xc: xc, yc: yc, r: r, e: e};
}

slava.circlegame.distance = function(x, y, xc, yc) {
    return Math.sqrt((x - xc) * (x - xc) + (y - yc) * (y - yc));
}
