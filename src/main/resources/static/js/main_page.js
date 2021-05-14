const header = $('.header');
const content = $('.content');
const $main = $('.main_content');

$(document).ready(() => {
	DragAndDrop();
});	


$('.colorpicker').on('input', function() {
    $('.hexcolor').val(this.value);
});
    
$('.hexcolor').on('input', function() {
    $('.colorpicker').val(this.value);
}); 


class Task{
    title = null;
    members=[];
    labels=[];
    label_colors=[];
    desc = null;
    date = null;
    bg = null;
    board = null;

    set addLabelColor(color){ this.label_colors.push(color);}
    set taskBoard(board) { this.board = board;}
    get board() {return this.board;}
    set taskTitle(title){ this.title = title;}
    get Title() {return this.title;}
    set addMember(member){ this.members.push(member);}
    set addLabel(label){ this.labels.push(label);}
    set taskDescription(desc){ this.desc = desc;}
    set taskDate(date){ this.date = date;}
    set taskBg(bg){ this.bg = bg;}
}



$('.ok_board').on('click', () => {
    let title = $('.board_title input').val();
    let a = $('.colorpicker').val();

    let element = `<div class="default_ul">
                        <div class="list_header" style="background: ${a}">
                            <span>${title}</span>
                            <div class="edit_conf">
                                <a href="#">
                                    <i class="fas fa-ellipsis-v"></i>
                                    <ul class="submenu_for_list" style="display: none;">
                                        <li>
                                            <span>Edit</span>
                                            <i class="edit far fa-edit"></i>
                                        </li>
                                        <li>
                                            <span>Delete</span>
                                            <i class="delete fas fa-trash-alt"></i>
                                        </li>
                                        <li>
                                            <span>Color</span>
                                            <i class="color fas fa-palette"></i>
                                        </li>
                                    </ul>
                                </a>
                            </div>
                        </div>
                        <ul data-draggable="target" class="target">
                            <div class="btn_new_activity">
                                
                            </div>
                        </ul>
                        <div class="btn_add_item">
                            <a href="#">
                                <span>Add a card</span>
                                <i class="far fa-plus-square"></i>
                            </a>
                        </div>
                    </div>`;
    
    $('.default_main').before(element);   
});


$(document).on('click', '.edit_conf a', (e) => {
    let a = $(e.currentTarget).closest('.list_header').css('background').substring(0,16);
    $(e.currentTarget).find('.submenu_for_list').css('border', `1px solid ${a}`);
    $(e.currentTarget).find('.submenu_for_list').toggle();
});

  
let task;
$(document).on('click', '.btn_add_item', (e) => {    
    task = new Task();
    let element = $(e.currentTarget).parent().find('.target');

    task.board = e;
    let a = element.parent().find('.list_header').css('background').substring(0,15);
    $(e.currentTarget).parent().find('.target li:last').css('border', `1px solid ${a}`); 
    $('.add_new_task_toBoard').show();
   
    header.css('filter', 'blur(6px)');
    content.css('filter', 'blur(6px)');
});

let objectTask = {};
$("#taskWindow").submit(function (e) {
    e.preventDefault();
    const inputData = $(e.currentTarget).serializeArray();

    let input = {};
    inputData.map(({name, value}) => input[name] = value);
    objectTask = {...task, ...input};

    let labels_list = readLabels(objectTask.labels, task.label_colors);
    let row = `<li data-draggable="item" data-task="1" class="item">
                    <div class="task_header">
                        <span>${objectTask.task_title}</span>
                        <i class="fas fa-ellipsis-v"></i>
                    </div> 
                    <div class="align_task_bg">
                        <div class="task_bg">
                            <img src="${objectTask.bg}"/>
                        </div>
                    </div>
                    <div class="align_task_labels_win">
                        <div class="task_labels_win">
                            <ul>${labels_list}</ul>
                        </div>
                    </div>
                    <div class="task_description">
                        ${objectTask.description}
                    </div>
                    <div class="task_items">
                        <div class="task_severity">
                            <i class="task far fa-check-square"></i>
                            <i class="severity fas fa-exclamation-circle"></i>
                            <i class="calendar far fa-calendar-alt"></i>
                        </div>
                        <div class="short_inf_task">
                            <span>${objectTask.created}</span>
                        </div>
                    </div>
                </li>`;
    
    $(task.board.currentTarget).closest('.default_ul').find('.target').append(row);
    $('.add_new_task_toBoard').hide();
    header.css('filter', 'blur(0px)');
    content.css('filter', 'blur(0px)');
    DragAndDrop(e);
});



// Proceed data for tooltip 
function readLabels(labels, colors){
    let list = ``;

    let object = {};
    labels.forEach((key, i) => object[key] = colors[i]);
    $.each(object, (key, value) => {
        list += `<li style="background: ${value}">
        <span style="">${key}</span></li>`;
    });
       
    return list;
}

$(document).on('mouseover', '.task_labels_win li', (e) => {
    $(e.currentTarget).find('span').show();
});

$(document).on('mouseout', '.task_labels_win li', (e) => {
    $(e.currentTarget).find('span').hide();
});

$('.change_bg_of_task a').on('click', function () {
    $('.bg_input').trigger('click');
});

$('.bg_input').on('change', function (e) {
    const file = e.target.files[0];
    const reader = new FileReader();

    reader.addEventListener("load", function () {
        $(e.currentTarget).parents('.add_new_task_toBoard').find('.bg_of_task img').prop('src', reader.result); 
        task.taskBg = reader.result;
    }, false);
    if (file) { reader.readAsDataURL(file); } 
});




$('.add_a_members_to_task').on('click', ()=>{
    $('.list_of_members').toggle();
});

$(document).on('click', '.list_of_members li', (e) => {
    let a = $(e.currentTarget).find('.member_name span').text();
    let d = $(e.currentTarget).find('.member_avatar img').prop('src');
    
    $('.task_members_ul span').remove();
    const $list = $('.task_members_ul');
    const $members = $('.display_task_members');

    $list.parent().css('border', '1px solid #e3e3e3');

    let counter = $list.find('li').length;
    if(counter < 3){
        if(counter == 2){
            counter++;
            $list.append(`<li><p class="counter">${counter-2}</p></li>`);  
            $list.append(`<li style="display: none" data-name="${a}"><img src="${d}"></li>`);       
            $members.append(`<li><img src="${d}"><span>${a}</span></li>`);
            task.addMember = a;    
        } else {
            $list.append(`<li data-name="${a}"><img src="${d}"></li>`);
            $members.append(`<li><img src="${d}"/> <span>${a}</span></li>`);       
            task.addMember = a;
            counter++;
        }
    } else {
        $list.append(`<li style="display: none" data-name="${a}"><img src="${d}"></li>`);       
        $members.append(`<li><img src="${d}"><span>${a}</span></li>`);      
        task.addMember = a;
        counter++;
        $('.counter').text(counter-3);          
    }
});


$(document).on('click', '.task_members_ul', ()=> {
    $('.display_task_members').toggle();
});



$('.add_a_labels_to_task').on('click', ()=>{
    $('.list_of_labels').toggle();
});

$(document).on('click', '.list_of_labels li', (e) => {
    let a = $(e.currentTarget).find('.label_name span').text();
    let d = $(e.currentTarget).find('.label_value span').css('background').substring(0,16);

    $('.task_labels_ul span').remove();
    const $list = $('.task_labels_ul');
    const $members = $('.display_task_labels');

    $list.parent().css('border', '1px solid #e3e3e3');

    let counter = $list.find('li').length;
    if(counter < 3){
        if(counter == 2){
            counter++;
            $list.append(`<li><p class="counter_labels">${counter-2}</p></li>`);  
            $list.append(`<li style="display: none" data-name="${a}"><p style="background: ${d}"></p></li>`);       
            $members.append(`<li><p style="background: ${d}"></p><span>${a}</span></li>`);
            task.addLabelColor = d;
            task.addLabel = a;
        } else {
            $list.append(`<li data-name="${a}"><p style="background: ${d}"></p></li>`);       
            $members.append(`<li><p style="background: ${d}"></p><span>${a}</span></li>`);          
            counter++;
            task.addLabelColor = d;
            task.addLabel = a;
        }
    } else {
        $list.append(`<li style="display: none" data-name="${a}"><span style="background: ${d}"></span></li>`);       
        $members.append(`<li><p style="background: ${d}"></p><span>${a}</span></li>`);          
        task.addLabel = a;
        task.addLabelColor = d;
        counter++;
        $('.counter_labels').text(counter-3);          
    }
});

$(document).on('click', '.task_labels_ul', ()=> {
    $('.display_task_labels').toggle();
});

$('.add_board').on('click', () => {
    $('.textfield_of_newboard').toggle();
    $('.add_board').hide();
    $('.add_board_ok_cancel').show();       
});

$('.cancel_board').on('click', () => {
    $('.textfield_of_newboard').toggle();
    $('.add_board').show();
    $('.add_board_ok_cancel').hide();
});

$('.user_icon').on('click', () => {
    $('.user_submenu').toggle();
    $('.message_box').hide();
});

$('.notification_icon').on('click', () => {
    $('.message_box').toggle();
    $('.user_submenu').hide();
});

$('.header_row_addNew i').on('click', () => {
    $('.add_new_task_toBoard').hide();
    header.css('filter', 'blur(0px)');
    content.css('filter', 'blur(0px)');
});