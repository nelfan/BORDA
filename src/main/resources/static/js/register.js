function reg(dat) {
    event.preventDefault();
    fetch(event.target.action, {
        method: 'POST',
        body: new URLSearchParams(new FormData(event.target))
    }).then((resp) => {
    var p = Promise.resolve(resp.json());
    p.then(function(v) {
    document.cookie = "token="+v.token+"; secure";
    }, function(e) {
    });
        return resp.json();
    }).then((body) => {
    }).catch((error) => {
    });
    window.location = "http://localhost:9090/pages/boards.html";
};

