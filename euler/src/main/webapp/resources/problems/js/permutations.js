var Permutations = {}
//Modifies argument array p with next permutation of its numbers. 
Permutations.next = function(p) {
	var max = p[p.length - 1];
	var tail = [max];
	for (var i = p.length - 2 ; i >= 0; i--) {
		var d = p[i];
		if (d < max) {
			var min = max + 1;
			var jm = -1;
			for (var j = 0; j < tail.length; j++) {
				if (tail[j] > d && tail[j] < min) {
					min = tail[j];
					jm = j;
				}
			}
			tail[jm] = d;
			p[i] = min;
			tail.sort();
			for (var j = 0; j < tail.length; j++) {
				p[i + 1 + j] = tail[j]
			}
			return true;
		} else {
			max = d;
			tail.push(d);
		}
	}	
	return false;
}

//p - array of numbers; returns number composed from left to right 
//starting at fromIndex and ending at toIndex.
Permutations.toNumber = function(p, fromIndex, toIndex) {
	var n = 0;
	for (var i = fromIndex; i <= toIndex; i++) {
		n = n * 10 + p[i];
	}
	return n;
}