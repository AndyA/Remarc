/**
 * Javascript functions for manage.html
 */

function initPage() {

	$("#btn-save").click(function() {
		saveThemes();
	})
	
	$("input").focus(function () {
		
		if ($(this).val() == '<theme>') { 
			$(this).val(''); 
		}
	})
	
	getThemes();
}

function saveThemes() {
	
	$('#btn-save').addClass('disabled');
	
	var themeTextObj = {};
	var themeTextArray = [];
	
	for (var i = 1; i <= 10; i++) {
		themeTextArray.push(document.getElementById("theme-" + i).value);
	}
	
	themeTextObj["themes"] = themeTextArray;
	
	var jsonData = JSON.stringify(themeTextArray);
	
	$.ajax({
    	type:"PUT",
    	data: jsonData,
        contentType: "application/json",   	
    	dataType:"html",
	    url: "../ws/theme/",
	    success: onPutSuccess,
	    error: onPutError,
	    complete: onPutComplete
    });
	
}

function onPutSuccess(result) {

	$('#btn-save').removeClass('disabled');

}

function onPutError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
	alert("Error - Themes could not be updated");

	$('#btn-save').removeClass('disabled');
}

function onPutComplete(xhr,status) {

	location.href = "index.html"
}

function getThemes() {
	
	$.ajax({
    	type:"GET",
    	dataType: "json",
	    url: "../ws/theme/",
	    success: onGetSuccess,
	    error: onGetError,
	    complete: onGetComplete
    });

}

function onGetSuccess(result) {
	
	//update all the button labels
	$.each(result, function(i, obj) {
	
		$("#theme-" + (i+1)).val(obj.name);
		
	});
}

function onGetError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
}

function onGetComplete(xhr,status) {

}