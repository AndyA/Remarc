/**
 * Javascrip functions for upload.html
 */

function initPage() {
	
	//when the input selection changes (selects a file), remove the disabled flag on submit
	$('input:file').change( function(){
		if ($(this).val()) {
			$('button:submit').attr('disabled',false);
	    } 
	});
	
}