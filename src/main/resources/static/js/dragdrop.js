$(document).ready(() => {
	DragAndDrop();
});

function DragAndDrop(e){
	if(!document.querySelectorAll || !('draggable' in document.createElement('span')) || window.opera) {
		return;
	}


	for(var items = document.querySelectorAll('[data-draggable="item"]'), len = items.length,
			i = 0; i < len; i ++){
		console.log(`David`);
		console.log(items);
		items[i].setAttribute('draggable', 'true');
	}

	$(document).on('dragstart', function(e){
		item = e.target;
		e.dataTransfer.setData('text', '');
	});

	$(document).on('dragover', (e) => {
		if(item){
			e.preventDefault();
		}

	});

	$(document).on('drop', (e) => {
		if(e.target.getAttribute('data-draggable') == 'target'){
			e.target.appendChild(item);
			e.preventDefault();
		}

		// console.log($(e.target).find('li').length);
	});

	$(document).on('dragend', (e) => {
		item = null;
	});
}

