var boardNameInput = document.getElementById("create_board");
var createBoardBtn = document.getElementById("create_board_btn");
createBoardBtn.addEventListener("click", createBoard);

function createBoard() {
    var data = {
        name: boardNameInput.value.trim(),
    }
    var json = JSON.stringify(data);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/boards", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(json);

}
