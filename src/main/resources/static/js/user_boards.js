class User {
    username = "";
    email = "";
    lastName = "";
    firstName = "";
    avatar = "";

    constructor(username, email, lastName, firstName, avatar) {
        this.username = username;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.avatar = avatar;
    }
}

var boardRequest = new XMLHttpRequest();

window.onload = function () {
    boardRequest.open('GET', '/users/1/boards');
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
        if (data[i].userBoardRelations[0].boardRole.name === "OWNER"
            || data[i].userBoardRelations[0].boardRole.name === "owner") {
            var boardsContainer = document.getElementById("own-boards-container");
            boardsContainer.insertAdjacentHTML('beforeend',
            '<div class="boards-info">' +
            '<a href="' + data[i].id + '">' +
            '<div class="board-name">' + data[i].name + '</div>' +
            '</a>' +
            '</div>');
        } else {
            var boardsContainer = document.getElementById("collab-boards-container");
            boardsContainer.insertAdjacentHTML('beforeend',
                '<div class="boards-info">' +
                '<a href="' + data[i].id + '">' +
                '<div class="board-name">' + data[i].name + '</div>' +
                '</a>' +
                '</div>');
        }
    }
}




