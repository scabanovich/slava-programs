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

common.clearOut = function (targetIds) {
	for (var i = 0; i < targetIds.length; i++) {
		common.out("", targetIds[i]);
	}
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
	if ((typeof sourceArray) == 'number') {
		this.sourceArray = [];
		while (sourceArray > 0) {
			this.sourceArray.push(sourceArray % 10);
			sourceArray = parseInt(sourceArray / 10);
		}
	} else {
		this.sourceArray = sourceArray;
	}
}

common.math.BigNumber.ZERO = new common.math.BigNumber([0]);
common.math.BigNumber.ONE = new common.math.BigNumber([1]);

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

common.math.BigNumber.prototype.multiply = function(other) {
	var result = [];
	var m1 = this.length();
	var m2 = other.length();
	var m = m1 + m2 - 1;
	var remainder = 0;
	for (var s = 0; s < m; s++) {
		for (var i = 0; i < m1; i++) {
			var a = this.getDigit(i);
			var j = s - i;
			if (j < 0 > j >= m2) {
				continue;
			}
			var b = other.getDigit(j);
			remainder += a * b;
		}
		result.push(remainder % 10);
		remainder = parseInt(remainder / 10);
	}
	while (remainder > 0) {
		result.push(remainder % 10);
		remainder = parseInt(remainder / 10);
	}
	return new common.math.BigNumber(result);
}

common.math.BigNumber.prototype.lastDigits = function(k) {
	if (this.length() <= k) {
		return this;
	}
	var result = [];
	for (var i = 0; i < k; i++) {
		result.push(this.getDigit(i));
	}
	return new common.math.BigNumber(result);
}

common.math.BigNumber.prototype.power = function(k, digits) {
	if (k == 0) {
		return this.ONE;
	} else if (k == 1) {
		return this;
	}
	var k2 = parseInt(k / 2);
	var p2 = this.power(k2, digits);
	p2 = p2.multiply(p2).lastDigits(digits);
	if (k % 2 == 1) {
		p2 = p2.multiply(this).lastDigits(digits);
	}
	return p2;
}

common.math.BigNumber.prototype.reverseDigits = function() {
	var a = [];
	var j = 0;
	while (this.sourceArray[j] == 0 && j + 1 < this.sourceArray.length) {
		j++;
	}
	for (var i = this.sourceArray.length - 1; i >= j; i--) {
		a.push(this.sourceArray[i]);
	}
	return new common.math.BigNumber(a);
}

common.math.BigNumber.prototype.equals = function(other) {
	if (this.sourceArray.length != other.sourceArray.length) {
		return false;
	}
	for (var i = 0; i < this.sourceArray.length; i++) {
		if (this.sourceArray[i] != other.sourceArray[i]) {
			return false;
		}
	}
	return true;
}

//end of class BigNumber

//class common.math.Primes
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
	if (n <= this.limit) {
		return this.set.has(n);
	}
	for (var i = 0; i < this.list.length; i++) {
		var p = this.list[i];
		if (p * p > n) {
			return true;
		}
		if (n % p == 0) {
			return false;
		}
	}
	return false;
}

common.math.Primes.prototype.get = function(index) {
	return index < 0 || index >= this.list.length ? -1 : this.list[index];
}

common.math.Primes.prototype.countDistinctFactors = function(n) {
	var result = 0;
	for (var i = 0; i < this.list.length; i++) {
		var p = this.list[i];
		if (p * p > n) {
			if (n > 1) {
				result++;
			}
			break;
		}
		if (n % p == 0) {
			result++;
			while (n % p == 0) {
				n = parseInt(n / p);
			}
		}
	}
	return result;
}

common.math.Primes.prototype.size = function() {
	return this.set.size;
}
//End class common.math.Primes

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

//returns string
common.math.convertToBase = function (n, b) {
	var ds = common.math.getDigitsInBase(n, b);
	var result = "";
	for (var i = ds.length - 1; i >= 0; i--) {
		if (ds[i] < 10) {
			result += ds[i];
		} else {
			result += String.fromCharCode(87 + ds[i]);
		}
	}
	return result;
}
