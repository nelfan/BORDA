class User{
    username = "";
    email = "";
    lastName = "";
    firstName = "";
    avatar = "";
    //userBoardRelations: [];
    //comments: [];
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

window.onload = function () {
    userRequest.open('GET', '/users/user');
    userRequest.send();
    boardRequest.open('GET', '/users/user/boards');
    boardRequest.send();
};

boardRequest.onload = function () {
    if (boardRequest.status >= 200 && boardRequest.status < 400) {
        var boards = JSON.parse(boardRequest.responseText);
        renderBoard(boards);
    } else {
        console.log("Fail connection (/users/user/boards)");
    }
}

function renderBoard(data) {
    for (let i = 0; i < data.length; i++) {
        if (data[i].userBoardRelations[0].boardRoles[0].name === "OWNER"
            || data[i].userBoardRelations[0].boardRoles[0].name === "owner"){
            var boardsContainer = document.getElementById("own-boards-container");
            boardsContainer.insertAdjacentHTML('beforeend',
                '<div class="boards-info"><p>'+data[i].name+'</p></div>');
        } else {
            var boardsContainer = document.getElementById("collab-boards-container");
            boardsContainer.insertAdjacentHTML('beforeend',
                '<a class="boards-info" href="'+data[i].name+'"><p>'+data[i].name+'</p></a>');
        }
    }
}




