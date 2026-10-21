// kash-e sade baraye bazi — mikhaym bazi bedoone internet ham bazar kone
// vaghani avvalin bar ke bazi ro mikonin in fileha zakhire mishan, bad az oon
// hatta ba internet-e kande bazi bala miad.
var nomreKash = 'mar-kodnevis-v2';   // v2: font-e vazirmatn baft-shode + raf-e keshide-shodan-e kanvas
var fileha = [
  './',
  './index.html',
  './mar-kodnevis.html',
  './manifest.json',
  './icon-192.png',
  './icon-512.png',
  './icon-180.png'
];

// nasb-e avalie: hamechi ro zakhire mikone
self.addEventListener('install', function(e) {
  e.waitUntil(
    caches.open(nomreKash).then(function(k) { return k.addAll(fileha); })
  );
});

// safhaye ghadimi ro pak mikone ta sholt nemanad
self.addEventListener('activate', function(e) {
  e.waitUntil(
    caches.keys().then(function(esmha) {
      return Promise.all(esmha.map(function(esm) {
        if (esm != nomreKash) return caches.delete(esm);
      }));
    })
  );
});

// har darkhast: aval az kash, age nabood az internet
self.addEventListener('fetch', function(e) {
  e.respondWith(
    caches.match(e.request).then(function(mojood) {
      if (mojood) return mojood;
      return fetch(e.request).then(function(javab) {
        // javab-e khoob ro zakhire mikone baraye bar-e bad
        if (e.request.method == 'GET' && javab && javab.status == 200) {
          var copied = javab.clone();
          caches.open(nomreKash).then(function(k) { k.put(e.request, copied); });
        }
        return javab;
      }).catch(function() {
        return caches.match('./mar-kodnevis.html');
      });
    })
  );
});
