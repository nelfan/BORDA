$('.user_icon').on('click', () => {
    $('.user_submenu').toggle();
    $('.message_box').hide();
});

document.getElementById('logout').onclick = function(){
localStorage.clear();
window.location = "http://localhost:9090/pages/index.html";
};

document.getElementById('profile').onclick = function(){
window.location = "http://localhost:9090/pages/personal-account.html";
};

document.getElementById('projects').onclick = function(){
window.location = "http://localhost:9090/pages/boards.html";
};