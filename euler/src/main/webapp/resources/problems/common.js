var common = {};

common.addProblemHeader = function(targetId, problemNumber) {
	document.getElementById(targetId).innerHTML = 
    	"<h3>" + document.title + "</h3>" +
    	"<h4>Problem " + problemNumber + "</h4>";  
}

common.getInt = function(inputId) {
	var v = document.getElementById(inputId).value;
	var n = parseInt(v, 10);
	if (!Number.isInteger(n) || v != "" + n) {
		return v;
	}
	return n;
}

common.power = function(n, p) {
	if (p == 0) {
		return 1;
	}
	if (p == 1) {
		return n;
	}
	var p2 = parseInt(p / 2);
	var h = common.power(n, p2);
	h = h * h;
	if (p % 2 == 1) {
		h = h * n;
	}
	return h;
}

common.toSortedArray = function(set) {
	var result = [];
	var it = set.values();
	var v = it.next();
	while (!v.done) {
		result.push(v.value);
		v = it.next();
	}
	result.sort((a,b) => a-b);
	return result;
}
		

common.out = function(data, targetId) {
	if (targetId) {
		document.getElementById(targetId).innerHTML = data;
	}
}

common.err = function(data, targetId) {
	data = "<span style='color: red'>" + data + "</span>";
	common.out(data, targetId);
}

common.arrayToString = function(a) {
	var s = "";
	for (var i = 0; i < a.length; i++) {
		s += a[i];
	}
	return s;
}


common.math = {};

common.math.getSumOfDivisors = function(n) {
	var result = 1;
	for (var d = 2; d < n; d+=1) {
		var d2 = d * d;
		if (d2 >= n) {
			if (d2 == n) {
				result += d;
			}
			break;
		}
		if (n % d == 0) {
			result += d + parseInt(Math.floor(n / d), 10);
		}
	}
	return result;
}

common.math.getMinimalDivisor = function(n) {
	for (var d = 2; d < n; d+=1) {
		var d2 = d * d;
		if (d2 >= n) {
			if (d2 == n) {
				return d;
			}
			break;
		}
		if (n % d == 0) {
			return d;
		}
	}
	return n;
}

//BigNumber class
common.math.BigNumber = function(sourceArray) {
	this.sourceArray = sourceArray;
} 

common.math.BigNumber.prototype.length = function() {
	return this.sourceArray.length;
}

common.math.BigNumber.prototype.toString = function() {
	var s = "";
	for (var i = this.length() - 1; i >= 0; i--) {
		s += this.sourceArray[i];
	}
	return s;
}
	
common.math.BigNumber.prototype.getDigit = function(i) {
	return (i < 0 || i >= this.length()) ? 0 : this.sourceArray[i];
}
	
common.math.BigNumber.prototype.add = function(other) {
	var result = [];
	var m1 = this.length();
	var m2 = other.length();
	var m = m1 > m2 ? m1 : m2;
	var remainder = 0;
	for (var i = 0; i < m; i++) {
		var a = this.getDigit(i);
		var b = other.getDigit(i);
		remainder += a + b;
		result.push(remainder % 10);
		if (remainder >= 10) {
			remainder = 1;
		} else {
			remainder = 0;
		}
	}
	if (remainder > 0) {
		result.push(remainder);
	}
	return new common.math.BigNumber(result);
}

//Primes class
common.math.Primes = function(limit) {
	this.limit = limit;
	this.set = new Set();
	this.list = [];
	var sieve = [1,1];
	for (var i = 2; i < limit; i++) {
		sieve.push(0);
	}
	for (var n = 2; n < limit; n++) {
		if (sieve[n] == 0) {
			this.set.add(n);
			this.list.push(n);
			var m = n * n;
			while (m < limit) {
				sieve[m] = 1;
				m += n;
			}
		}
	}
}

common.math.Primes.prototype.isPrime = function(n) {
	return this.set.has(n);
}

common.math.Primes.prototype.get = function(index) {
	return index < 0 || index >= this.list.length ? -1 : this.list[index];
} 

common.math.Primes.prototype.size = function() {
	return this.set.size;
}

//Returns array of digits, result[0] is ones.
common.math.getDigits = function(n) {
	return common.math.getDigitsInBase(n, 10);
}


//b - base
common.math.getDigitsInBase = function(n, b) {
	var result = [];
	while (n > 0) {
		var d = n % b;
		n = parseInt((n - d) / b);
		result.push(d);
	}
	return result;
}
