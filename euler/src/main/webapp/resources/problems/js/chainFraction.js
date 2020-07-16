var chain = {}

/**
 * Returns 'depth' levels of chain fraction of sqrt(n).
 */
chain.build = function (n, depth) {
	var vs = [];
	var m = parseInt(Math.sqrt(n));
	var v = m;
	var k = m;
	var d = 1;
	vs.push(v);
	if (depth > 0) depth--;
	while (depth > 0 
			|| depth < 0) { // depth < 0 : build period
		var d1 = n - k * k;
		if (d1 == 0) break;
		d = parseInt(d1 / d);
		v = parseInt((m + k) / d);
		vs.push(v);
		k = v * d - k;
		if (depth > 0) {
			depth--;
		} else if (d == 1) {
			break;
		}
	}
	var result = {
		n : n,
		vs : vs,
		k : k,
		d : d				
	}
	return result;
}

/**
 * Returns chain fraction of sqrt(n) until period ends.
 */
chain.buildPeriod = function(n) {
	return chain.build(n, -1);
}
