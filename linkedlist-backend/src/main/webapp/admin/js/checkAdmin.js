if(!window.com){
    com = {};
}

if(!com.ep){
    com.ep = {};
}

if(!com.ep.linkedList){
    com.ep.linkedList = {};
}

if(!com.ep.linkedList.admin){
    com.ep.linkedList.admin = {};
}

com.ep.linkedList.admin.auth = (function(){
    function login(email,password) {
        firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log("errorCode : "+errorCode);
            console.log("errorMessage : "+errorMessage);
            alert("아이디 또는 비밀번호가 틀렸습니다.");
        });

        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                firebase.database().ref('admin/'+user.uid).on('value', function(snapshot) {
                  if(snapshot){
                    location.href = "/admin/pages/index.html";
                  } else {
                    alert("관리자가 아닙니다.");
                  }
                });
            }
        });
    }

    function logout(){
        firebase.unauth();
    }

    function check(){
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                firebase.database().ref('admin/'+user.uid).on('value', function(snapshot) {
                  if(snapshot){
                  } else {
                    alert("관리자가 아닙니다.");
                    location.href = "/admin/pages/login.html";
                  }
                });
            } else {
                alert("로그인을 해주세요.");
                location.href = "/admin/pages/login.html";
            }
        });
    }

    function isAdmin(callback){
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                firebase.database().ref('admin/'+user.uid).on('value', function(snapshot) {
                  if(snapshot){
                    callback(true);
                  } else {
                    callback(false);
                  }
                });
            } else {
                callback(false);
            }
        });
    }

    return {
        login : login,
        logout : logout,
        isAdmin : isAdmin,
        check : check
    }
})();