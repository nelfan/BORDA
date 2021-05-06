var ourRequest = new XMLHttpRequest();
var container = document.getElementById("user");
var idToChange = 2;

window.onload = function() {
ourRequest.open('GET', '/users/'+idToChange);
//TO DO: change url to url of session user
ourRequest.send();
};
ourRequest.onload = function() {
    if (ourRequest.status >= 200 && ourRequest.status < 400) {
      var user = JSON.parse(ourRequest.responseText);
      render(user);
    } else {
      console.log("Bad connection");
    }
  }
function render(data){
    document.getElementById("username").insertAdjacentHTML('afterbegin', "username: "+data.username);
    document.getElementById("email").insertAdjacentHTML('afterbegin', "e-mail: "+data.email);
    document.getElementById("lastName").insertAdjacentHTML('afterbegin', "first name: "+data.firstName);
    document.getElementById("firstName").insertAdjacentHTML('afterbegin', "last name: "+data.lastName);
    var img = '';
    if(data.avatar!=null)
    img = '<img  src = "data:image/jpeg;base64, '+data.avatar+'"/>';
    else img = '<img src = "/img/blank_avatar.jpg"/>';

    document.getElementById("image-container").insertAdjacentHTML('afterbegin', img);


    var boardsContainer = document.getElementById("boards-container");
    //TO DO: конкретные борды добавить в лист, обрачивая тегом <div class="boards-info">1</div>


}