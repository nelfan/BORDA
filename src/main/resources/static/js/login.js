$('.tab a').on('click', function (e) {
    e.preventDefault();
    $(this).parent().addClass('active');
    $(this).parent().siblings().removeClass('active');

    let target = $(this).attr('href');
    $('.tab-content > div').not(target).hide();
    $(target).fadeIn(600);
});


function checkPass(e){
    let inputField = $(e).parent().find('input');
    if($(e).parent().find('input').prop('type') == "password"){
        inputField.prop('type', 'text');
    } else {
        inputField.prop('type', 'password');
    }
};

$('#SignInForm').submit((e) => {
    e.preventDefault();
    const inputData = $(e.currentTarget).serializeArray();
    let input = {};
    inputData.map(({name, value}) => input[name] = value);
    $.post('/auth', {...input})
});