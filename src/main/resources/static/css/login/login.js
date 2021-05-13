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

