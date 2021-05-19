/*
    The User is able to cast a look at the current TASK in the list
    Clicked by 'View' button
*/
$(document).on('click', '.view_task', (e) => {
    let $elementId = $(e.currentTarget).parents('.item').data('task');
    let $edit = $('.add_new_task_toBoard');
    $edit.addClass('editTask').show();
    $edit.find('.align_addTask_btn').remove();
    /*
        add ajax query to get element by ID
        data: {
            title: "Title",
            background: "background image",
            members: [
                {name: "David", avatar: "avatarka"}
            ],
            labels: [
                {name: "task", color: "avatarka"}
            ],
            date: "",
            description: ""
        }
    */


    console.log(`Element id => ${$elementId}`);

    header.css('filter', 'blur(6px)');
    content.css('filter', 'blur(6px)');
});


/*
    The User is able to delete the task from the list
    Clicked by 'Delete' button
*/
$(document).on('click', '.delete_task', (e) => {
    let li = $(e.currentTarget).parents('.item');
    console.log(`1 -> ${li.parent().length}`);
});




/*
    The User is able to edit the task and save it again otherwise click cancel button
    Clicked by 'Edit' button
*/
$(document).on('click', '.view_task', (e) => {
    let $elementId = $(e.currentTarget).parents('.item').data('task');
    let $edit = $('.add_new_task_toBoard');
    $edit.addClass('editTask').show();
    $edit.find('.align_addTask_btn').remove();
    /*
        add ajax query to get element by ID
        data: {
            title: "Title",
            background: "background image",
            members: [
                {name: "David", avatar: "avatarka"}
            ],
            labels: [
                {name: "task", color: "avatarka"}
            ],
            date: "",
            description: ""
        }
    */


    console.log(`Element id => ${$elementId}`);

    header.css('filter', 'blur(6px)');
    content.css('filter', 'blur(6px)');
});

