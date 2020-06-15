MyCookies = {}

MyCookies.setCookie = function setCookie(name, value) {
	var d = new Date();
	d.setTime(d.getTime() + (2 * 24 * 60 * 60 * 1000)); //2 days
	var expires = "expires=" + d.toUTCString();
	document.cookie = "" + name + "=" + value + ";" + expires;// + ";path=/";
}

MyCookies.getCookie = function setCookie(name) {
	var cookie = decodeURIComponent(document.cookie);
	if (!cookie || cookie.length == 0) {
		return "";
	}
	var cs = cookie.split(";");
	for (var i = 0; i < cs.length; i++) {
		var c = cs[i];
		if (c.startsWith(name + "=")) {
			return c.substring(name.length + 1);
		}
	}
	return "";
}

MyCookies.deleteCookie = function setCookie(name) {
	//TODO
}
