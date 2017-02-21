/*
 * Copyright (C) 2016 BBC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	
	for (var i = 1; i <= 8; i++) {
		themeTextArray.push(document.getElementById("theme-" + i).value);
	}
	
	themeTextObj["themes"] = themeTextArray;
	
	var jsonData = JSON.stringify(themeTextArray);
	
	$.ajax({
    	type:"PUT",
    	data: jsonData,
        contentType: "application/json",   	
    	dataType:"html",
	    url: "ws/theme/",
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
	    url: "ws/theme/",
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