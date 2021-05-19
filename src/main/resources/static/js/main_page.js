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
    members=[];
    labels=[];
    label_colors=[];
    desc = null;
    bg = null;
    board = null;

    set addLabelColor(color){ this.label_colors.push(color);}
    set taskBoard(board) { this.board = board;}
    get board() {return this.board;}
    set addMember(member){ this.members.push(member);}
    set addLabel(label){ this.labels.push(label);}
    set taskDescription(desc){ this.desc = desc;}
    set taskBg(bg){ this.bg = bg;}
}


$('.ok_board').on('click', () => {
    let title = $('.board_title input').val();
    let colorValue = $('.colorpicker').val();

    /*
    $.post('/addNewBoardList', {title, colorValue}).done(
        entry => {
            перерендерить борду всю
        }
    )
    */

    let element = `<div class="default_ul">
                        <div class="list_header" style="background: ${colorValue}">
                            <span>${title}</span>
                            <div class="edit_conf">
                                <a href="#">
                                    <i class="fas fa-ellipsis-v"></i>
                                    <ul class="submenu_for_list" style="display: none;">
                                        <li class="edit_lsit">
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
    console.log(objectTask);
    // $.post('/addTaskToBoard', {objectTask}).done(
    //     result => {

    //     }
    // );

    let row = `<li data-draggable="item" data-task="1" class="item">
                    <div class="task_header">
                        <span>${objectTask.task_title}</span>
                        <i class="fas fa-ellipsis-v">
                            <ul class="edit_task_window">
                                <li class="view_task"><span>View</span><i class="show_task_i fas fa-file"></i></li>
                                <li class="edit_task"><span>Edit</span><i class="edit_task_i fas fa-pen-square"></i></li>
                                <li class="delete_task"><span>Delete</span><i class="delete_task_i far fa-trash-alt"></i></li>
                            </ul>
                        </i>
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
                    <div class="align_task_members_win">
                        <div class="task_members_win">
                            <ul>${labels_list}</ul>
                        </div>
                    </div>
                    <div class="task_description">
                        ${objectTask.description}
                    </div>
                    <div class="task_items">
                        <ul class="task_members_list">
                            
                        </ul>
                        <div class="short_inf_task">
                            <span>${objectTask.created}</span>
                        </div>
                    </div>
                </li>`;

    $(task.board.currentTarget).closest('.default_ul').find('.target').append(row);

    $('#taskWindow')[0].reset();
    resetData();
    $(this).find('.task_members_ul').empty();
    $(this).find('.task_labels_ul').empty();
    header.css('filter', 'blur(0px)');
    content.css('filter', 'blur(0px)');
    DragAndDrop(e);
});


function resetData(){
    $('.add_new_task_toBoard').find('.bg_of_task img').prop('src', 'images/default.jpg');
    $('.add_new_task_toBoard').hide();
}

// Proceed data for tooltip
function readLabels(labels, colors){
    let list = ``;

    let object = {};
    labels.forEach((key, i) => object[key] = colors[i]);
    console.log(object);
    $.each(object, (key, value) => {
        list += `<li style="background: ${value}">
        <span style="">${key}</span></li>`;
    });
    return list;
}

$(document).on('mouseover', '.task_labels_win li', (e) => {
    let a = $(e.currentTarget).find('span').text();
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




function getMembers() {
    $.post('/getMembersNameAndAvatar').done(result => {
        // let result = `[
        //     {"name":"David", "avatar":"images/nature.jpg"},
        //     {"name":"David_1", "avatar":"images/nature.jpg"}
        // ]`;

        $('.list_of_members').empty();
        let membersList = ``;

        $.each(JSON.parse(result), (key, object) => {
            let {name, avatar} = object;

            membersList += `<li>
                                <div class="user_info_task_members">
                                    <div class="member_avatar">
                                        <img src="${avatar}" default="images/default.jpg"> 
                                    </div>
                                    <div class="member_name">
                                        <span>${name}</span>
                                    </div>
                                </div>
                            </li>`;
        });

        $('.list_of_members').append(membersList);
    });
}

function chooseMember(subject, point) {
    console.log(`${subject} => ${point}`);
}

$('.add_a_members_to_task').on('click', (e) => {
    getMembers()
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

$(document).on('click', '.task_header i', (e) => {
    $(e.currentTarget).find('.edit_task_window').toggle();
});