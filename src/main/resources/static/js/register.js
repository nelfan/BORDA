function reg(dat) {
    event.preventDefault();
    fetch(event.target.action, {
        method: 'POST',
        body: new URLSearchParams(new FormData(event.target))
    }).then((resp) => {
    var p = Promise.resolve(resp.json());
    p.then(function(v) {
    localStorage.setItem('token', v.token);
    window.location = "http://localhost:9090/pages/boards.html";
    }, function(e) {
    });
    return resp.json();
    }).then((body) => {
    }).catch((error) => {
    });
};

