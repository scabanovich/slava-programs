goog.provide('slava.writers');

goog.require('goog.dom');
goog.require('goog.net.XhrIo');

slava.writers = null;

slava.getText = function(dataUrl, callback) {
  goog.net.XhrIo.send(dataUrl, function(e) {  
      var xhr = e.target;
      var obj = xhr.getResponseText();
      callback.apply(this, [obj]);
  });
}

slava.findWriter = function(id) {
  for (i in slava.writers) {
    if(id == slava.writers[i].id) {
      return slava.writers[i];
    }
  }
  return null;
}

slava.initWriters = function(s1, s2) {
  if(slava.writers) return;
  goog.net.XhrIo.send('writers.json', function(e) {  
      var xhr = e.target;
      slava.writers = xhr.getResponseJson().writers;
          for (i in slava.writers) {
            var w = slava.writers[i];
            var o = goog.dom.createDom('option', {value: w.id, selected: (i == 0)});
            goog.dom.appendChild(o, goog.dom.createTextNode(w._name));
            goog.dom.appendChild(s1, o);
            o = goog.dom.createDom('option', {value: w.id, selected: (i > 0)});
            goog.dom.appendChild(o, goog.dom.createTextNode(w._name));
            goog.dom.appendChild(s2, o);
          }
  });
}

slava.getJson = function(dataUrl, callback) {
  goog.net.XhrIo.send(dataUrl, function(e) {  
      var xhr = e.target;
      var obj = xhr.getResponseJson();
      callback.apply(this, [obj]);
  });
}

slava.countWords = function(text) {
  return (text.match(/\w+/g) || []).length;
}

slava.countWord = function(text, word) {
  var r = "[^A-Za-z]" + word + "[^A-Za-z]";
  var re = new RegExp(r,"ig");
  return (text.match(re) || []).length;
};


