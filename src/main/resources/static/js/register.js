var xhr = new XMLHttpRequest();
var data;
document.getValueById("register").onsubmit = function(){
    data = {
        username: document.getValueById('username').value,
        firstName: document.getValueById('firstName').value,
        lastName: document.getValueById('lastName').value,
        email: document.getValueById('email').value,
        password: document.getValueById('pass').value,
    }
    xhr.open("POST", '/register', true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(data));
}
xhr.onload = function(){
alert(xhr.response);
}
