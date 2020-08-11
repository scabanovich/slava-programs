var cg = {};

cg.templates = new Map();
cg.selected = null;
cg.autocopy = false;
      
cg.loadTemplates = function() {
	var e = document.getElementById("templates");
	var c = e.childNodes;
	for (var i = 0; i < c.length; i++) {
		if (c[i] instanceof Element) {
			var id = c[i].getAttribute("id");
				cg.templates.set(id, c[i]);    			  
			}
	}
	e.parentElement.removeChild(e);
}

cg.setTemplate = function(templateId) {
	var t = cg.templates.get(templateId);
	cg.selected = t;
	if (t) {
		var e = document.getElementById("formArea");
		e.innerHTML = "";
		e.appendChild(t);
		cg.generate();
	}
}

cg.generate = function() {
	if (cg.selected) {
		var ev = document.createEvent('Events')
		ev.initEvent("click", true, false);
		cg.selected.dispatchEvent(ev);
		if (cg.autocopy) {
			setTimeout(function() { cg.copyToClipboard('output'); }, 100);
		}
	}
}
  
cg.copyToClipboard = function(sourceId) {
	var active = document.activeElement;
	var e = document.getElementById(sourceId);
	e.select();
	e.setSelectionRange(0, e.value.length);
	document.execCommand("copy");
	e.setSelectionRange(e.value.length, e.value.length);
	active.focus();
}
   
function changeIndent(dv, targetId) {
	var e = document.getElementById(targetId)
	var i = parseInt(e.value);
	e.value = i + dv;
	cg.generate();
}

cg.writeToOut = function(text) {
	var e = document.getElementById("output");
	var ls = text.split("\n");
	var indent = document.getElementById("indent").value;
	var is = (indent == "0") ? ""
		   : (indent == "1") ? "    " 
		   : (indent == "2") ? "        " 
		   : (indent == "3") ? "            " 
		   : (indent == "4") ? "                " 
		   : "        ";
	var indentedText = "";
	for (var i = 0; i < ls.length; i++) {
		indentedText += is + ls[i] + "\n";
	}
	e.value = indentedText;
}
      
cg.value = function(inputId) {
	return document.getElementById(inputId).value;
}
      
