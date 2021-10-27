// Initialize Firebase
(function(){
    var config = {
        apiKey: "AIzaSyDG6xmhpFS0GyWqqlWEiBzWzCiNBOBCwc0",
        authDomain: "linkedlist-566ea.firebaseapp.com",
        databaseURL: "https://linkedlist-566ea.firebaseio.com",
        storageBucket: "linkedlist-566ea.appspot.com",
    };

    firebase.initializeApp(config);
})();

function extend(base) {
    var parts = Array.prototype.slice.call(arguments, 1);
    parts.forEach(function (p) {
        if (p && typeof (p) === 'object') {
            for (var k in p) {
                if (p.hasOwnProperty(k)) {
                    base[k] = p[k];
                }
            }
        }
    });
    return base;
}