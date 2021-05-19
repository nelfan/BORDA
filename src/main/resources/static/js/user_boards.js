var boardRequest = new XMLHttpRequest();

window.onload = function () {
    boardRequest.open('GET', '/users/'+localStorage.getItem('token')+'/boards');
    boardRequest.setRequestHeader('Authorization', 'Bearer '+localStorage.getItem('token'));
    boardRequest.send();
};

boardRequest.onload = function () {
    if (boardRequest.status >= 200 && boardRequest.status < 400) {
        var boards = JSON.parse(boardRequest.responseText);
        renderBoard(boards);
    } else {
        console.log("Fail connection");
    }
}

function renderBoard(data) {
    for (let i = 0; i < data.length; i++) {
        if (data[i].userBoardRelations[0].boardRole.name === "OWNER"
            || data[i].userBoardRelations[0].boardRole.name === "owner") {
            var boardsContainer = document.getElementById("own-boards-container");
            boardsContainer.insertAdjacentHTML('beforeend',
            '<div class="boards-info">' +
            '<a href="http://localhost:9090/pages/main_page.html" data-id="'+data[i].id+'"' +
            '<div class="board-name">' + data[i].name + '</div>' +
            '</a>' +
            '</div>');
        } else {
            var boardsContainer = document.getElementById("collab-boards-container");
            boardsContainer.insertAdjacentHTML('beforeend',
                '<div class="boards-info">' +
                '<a href="http://localhost:9090/pages/main_page.html" data-id="'+data[i].id+'">' +
                '<div class="board-name">' + data[i].name + '</div>' +
                '</a>' +
                '</div>');
        }
    }
}


$(document).on('click', '.boards-info a', (e) => {
    let s = $(e.currentTarget).data('id');
    localStorage.setItem("current_board", s);
    console.log(s);
});



