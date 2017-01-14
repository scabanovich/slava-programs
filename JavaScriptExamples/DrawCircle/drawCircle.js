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
    var precisionElement = document.getElementById("precision");
    if(slava.circlegame.circle != null) {
        var c = slava.circlegame.circle;
        ctx.strokeStyle="#FF0000";
        ctx.beginPath();
        ctx.arc(c.xc, c.yc, c.r, 0, 2 * Math.PI);
        ctx.stroke();        
        precisionElement.innerHTML = "" + slava.circlegame.getPrecision();
        reportElement.style.visibility = "visible";
    } else {
        reportElement.style.visibility = "hidden";
        precisionElement.innerHTML = "";
    }
}

slava.circlegame.getPrecision = function() {
    if(slava.circlegame.circle == null) return 0;
    var c = slava.circlegame.circle;
    var rel = c.e / c.r
    if(rel > 1) return 0;
    var res = "" + (1 - rel) * 100;
    var d = res.indexOf('.');
    if(res.length > d + 3) {
        res = res.substring(0, d + 3);
    }
    return res + '%';
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
        rs[i] = Math.sqrt((x - xc) * (x - xc) + (y - yc) * (y - yc));
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



