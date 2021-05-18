$('.load').on('click', function () {
    $('.load_doc').trigger('click');
});
let img;
$('.load_doc').on('change', function (e) {
    const file = e.target.files[0];
    const reader = new FileReader();

    $('.upload_fileName').text(file.name);
    img = file.name;
    reader.addEventListener("load", function () {
        $(e.currentTarget).parent().find('img').prop('src', reader.result);
    }, false);
    if (file) { reader.readAsDataURL(file); }
});


$("#SignUpForm").submit(function (e) {
    e.preventDefault();
    const inputData = $(e.currentTarget).serializeArray();
    let input = {};
    inputData.map(({name, value}) => input[name] = value);
    input.avatar = img;
    $(e.currentTarget).parents('.form').find('.tab-group li').not('.active').find('a').trigger('click');
    console.log(input);
    $.post('/register', {...input})
});