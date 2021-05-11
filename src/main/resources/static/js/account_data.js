class User{
username = "";
email = "";
lastName = "";
firstName = "";
avatar = "";
userBoardRelations: [];
comments: [];
constructor(username, email, lastName, firstName, avatar) {
    this.username = username;
    this.email = email;
    this.lastName = lastName;
    this.firstName = firstName;
    this.avatar = avatar;
  }
}
var userRequest = new XMLHttpRequest();
var boardRequest = new XMLHttpRequest();
var container = document.getElementById("user");
var current_user;

window.onload = function() {
userRequest.open('GET', '/users/user');
userRequest.send();
boardRequest.open('GET', '/users/user/boards');
boardRequest.send();
};
userRequest.onload = function() {
    if (userRequest.status >= 200 && userRequest.status < 400) {
      var user = JSON.parse(userRequest.responseText);
      renderUser(user);
    } else {
      console.log("Fail connection (/users/user)");
    }
  }
function renderUser(data){
    document.getElementById("username").insertAdjacentHTML('afterbegin', "username: <input id = 'usernameinp' type='text' value = '"+data.username+"' />");
    document.getElementById("email").insertAdjacentHTML('afterbegin', "e-mail: <input id = 'emailinp' type='text' value = '"+data.email+"' />");
    document.getElementById("lastName").insertAdjacentHTML('afterbegin', "first name: <input id = 'fnameinp' type='text' value = '"+data.firstName+"' />");
    document.getElementById("firstName").insertAdjacentHTML('afterbegin', "last name: <input id = 'lnameinp' type='text' value = '"+data.lastName+"' />");
    var img = '';
    if(data.avatar!=null)
    img = '<img  src = "data:image/jpeg;base64, '+data.avatar+'"/>';
    else img = '<img src = "/img/blank_avatar.jpg"/>';
    current_user = new User(data.username, data.email, data.lastName, data.firstName, data.avatar);
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

    //TO DO: конкретные борды добавить в лист, обрачивая тегом
}