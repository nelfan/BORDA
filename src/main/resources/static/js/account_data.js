class User{
id = 0;
username = "";
email = "";
lastName = "";
firstName = "";
avatar = "";

constructor(id, username, email, lastName, firstName, avatar) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.lastName = lastName;
    this.firstName = firstName;
    this.avatar = avatar;
  }
}
var userRequest = new XMLHttpRequest();
var boardRequest = new XMLHttpRequest();
var userPost = new XMLHttpRequest();
var container = document.getElementById("user");
var current_user;

window.onload = function() {
userRequest.open('GET', '/users/'+localStorage.getItem('token'), true);
userRequest.setRequestHeader('Authorization', 'Bearer '+localStorage.getItem('token'));
userRequest.send();
boardRequest.open('GET', '/users/'+localStorage.getItem('token')+'/boards');
boardRequest.setRequestHeader('Authorization', 'Bearer '+localStorage.getItem('token'));
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
    if(data.avatar!=null)
    document.getElementById("avatar").src = "data:image/jpeg;base64, "+data.avatar;
    current_user = new User(data.id, data.username, data.email, data.lastName, data.firstName, data.avatar);
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
        boardsContainer.insertAdjacentHTML('beforeend', '<a href="http://localhost:9090/pages/main_page.html" data-id="'+data[i].id+'"><div class="boards-info">'+data[i].name+'</div></a>');
    }
}

/*document.getElementById("username").labels[0].onclick = function(){
document.getElementById("username").disabled = false;
}*/
document.getElementById("avatar").onclick = function(){
document.getElementById('file-input').click();
}
document.getElementById("email").labels[0].onclick = function(){
document.getElementById("email").disabled = false;
}

document.getElementById("firstName").labels[0].onclick = function(){
document.getElementById("firstName").disabled = false;
}

document.getElementById("lastName").labels[0].onclick = function(){
document.getElementById("lastName").disabled = false;
}

document.getElementById("firstName").onchange = function(){
current_user.firstName = this.value;
postData();
document.getElementById("firstName").disabled = true;
}
document.getElementById("email").onchange = function(){
current_user.email = this.value;
postData();
document.getElementById("email").disabled = true;
}
document.getElementById("lastName").onchange = function(){
current_user.lastName = this.value;
postData();
document.getElementById("lastName").disabled = true;
}

/*document.getElementById("username").onchange = function(){
current_user.username = this.value;
postData();
document.getElementById("username").disabled = true;
}*/
function postData(){
userPost.open("POST", '/users/update/'+localStorage.getItem('token')+'');
userPost.setRequestHeader("Content-Type", "application/json");
userPost.setRequestHeader('Authorization', 'Bearer '+localStorage.getItem('token'));
var data = {
        username: current_user.username,
        email: current_user.email,
        firstName: current_user.firstName,
        lastName: current_user.lastName,
        avatar: current_user.avatar,
}
userPost.send(JSON.stringify(data));
}

document.getElementById("file-input").onchange  = e => {
  var file = e.target.files[0];
  var reader = new FileReader();
  reader.onloadend = function() {

    var data=(reader.result).split(',')[1];
    current_user.avatar = data;
    document.getElementById("avatar").src = "data:image/jpeg;base64, "+data;
    postData();
  }
  reader.readAsDataURL(file);
}


$(document).on('click', '#boards-container a', (e) => {
    let s = $(e.currentTarget).data('id');
    localStorage.setItem("current_board", s);
    console.log(s);
});