class User{
id = 0;
username = "";
email = "";
lastName = "";
firstName = "";
avatar = "";
userBoardRelations = [];
comments = [];

constructor(id, username, email, lastName, firstName, avatar, userBoardRelations, comments) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.lastName = lastName;
    this.firstName = firstName;
    this.avatar = avatar;
    this.userBoardRelations = userBoardRelations;
    this.comments = comments;
  }
}
var userRequest = new XMLHttpRequest();
var boardRequest = new XMLHttpRequest();
var boardPost = new XMLHttpRequest();
var container = document.getElementById("user");
var current_user;

window.onload = function() {
userRequest.open('GET', '/users/user');
userRequest.send();
boardRequest.open('GET', '/users/user/boards');
boardRequest.send();
};
userRequest.onload = function()  {
    if (userRequest.status >= 200 && userRequest.status < 400) {
      var user = JSON.parse(userRequest.responseText);
      renderUser(user);
    } else {
      console.log("Fail connection (/users/user)");
    }
  }

function renderUser(data){
    document.getElementById("username").value = data.username;
    document.getElementById("email").value = data.email;
    document.getElementById("firstName").value = data.firstName;
    document.getElementById("lastName").value = data.lastName;

    var img = '';
    if(data.avatar!=null)
    img = '<img  src = "data:image/jpeg;base64, '+data.avatar+'"/>';
    else img = '<img src = "/img/blank_avatar.jpg"/>';

    current_user = new User(data.username, data.email, data.lastName, data.firstName, data.avatar, data.userBoardRelations, data.comments);
    document.getElementById("image-container").insertAdjacentHTML('afterbegin', img);
}
boardRequest.onload = function(){
if (boardRequest.status >= 200 && boardRequest.status < 400) {
      var boards = JSON.parse(boardRequest.responseText);
      renderBoards(boards);
    } else {
      console.log("Fail connection (/users/user/boards)");
    }
}

function renderBoards(data){
    var boardsContainer = document.getElementById("boards-container");
    for(var i = 0; i<data.length; i++){
        boardsContainer.insertAdjacentHTML('beforeend', '<div class="boards-info">'+data[i].name+'</div>');
    }
}

document.getElementById("username").onchange = function(){
current_user.username = this.value;
postData();
}
document.getElementById("firstName").onchange = function(){
current_user.firstName = this.value;
postData();
}
document.getElementById("email").onchange = function(){
current_user.email = this.value;
postData();
}
document.getElementById("lastName").onchange = function(){
current_user.lastName = this.value;
postData();
}

function postData(){
alert("1");
boardPost.open("POST", '/users', true);
boardPost.setRequestHeader("Content-Type", "application/json");
//var data =
boardPost.send(JSON.stringify(current_user));
}
boardPost.onload = function(){
alert(boardPost.status);
}