/**
 * Javascript functions for manage.html
 */

var selectedContentType;
var selectedFile;

function initPage() {

	$('#resource-file-tree').fileTree({ root: 'content/', script: 'connectors/jqueryFileTree.jsp' }, function(file) { 
		parseAndProcessFile(file);
	});

	$('#btn-save').click(function() {
		saveResource();
	});
			
	$('#btn-delete').click(function() {
		deleteResource();
	});
	
}

function getResource(type, id) {

	$.ajax({
    	type:"GET",
        dataType:"json",
	    url: "../ws/"+ type + "/metadata/" + id,
	    success: onGetSuccess,
	    error: onGetError,
	    complete: onGetComplete
    });
    
}

function parseAndProcessFile(file) {

	var fileAndExt = file.substr(file.lastIndexOf('/') + 1);
	var dirMinusFile = file.substr(0, file.lastIndexOf('/'));
	var content = dirMinusFile.substr(dirMinusFile.lastIndexOf('/') + 1);

	var fileExt = fileAndExt.substr(fileAndExt.lastIndexOf('/') + 1);
	var file = fileExt.substr(0, fileExt.lastIndexOf('.'));

	selectedContentType = content;
	selectedFile = file;
	
	getResource(selectedContentType, selectedFile);
    
}

function processGet(jsonResult) {

	//make the media display visible
	document.getElementById('display-buttons').style.display = 'block';

	$("#display-content").empty();
	$("#display-meta").empty();

	document.getElementById('preview-text').innerHTML = 'Resource Preview (' + jsonResult['id'] + ')';
	
	if (jsonResult.imageUrl == null) {
		if (selectedContentType == 'images') {
			jsonResult.imageUrl = "../images/test_card_colour.jpg";
		} else {
			jsonResult.imageUrl = "../images/test_card_bw.jpg";
		}
	}

	var displayContent;
	if (selectedContentType == 'images') {
		displayContent = "<img src=\"" + jsonResult.imageUrl + "\" class=\"img-content\" alt=\"" + jsonResult.id + "\" />";
	} else if (selectedContentType == 'audio') {
		displayContent = "<img src=\"" + jsonResult.imageUrl + "\" class=\"img-content img-content-audio\" alt=\"" + jsonResult.id + "\" />";

		displayContent += "<div class=\"audio-controls\">" +
			"<span id=\"audioplay\" class=\"glyphicon glyphicon-play audio-control audio-play\"></span>" + 
			"<span id=\"audiopause\" class=\"glyphicon glyphicon-pause audio-control audio-pause\"></span></div>";
		
		displayContent += "<audio id=\"audioPlayer\" preload=\"auto\">" +
			"<source src=\"" + jsonResult.mp3ContentUrl + "\" type=\"audio/mpeg\"/>" +
			"<source src=\"" + jsonResult.oggContentUrl + "\" type=\"audio/ogg\"/></audio>";
		
	} else if (selectedContentType == 'video') {
		displayContent = "<video id=\"videoPlayer\" controls preload=\"auto\" height=\"100%\">" +
			"<source src=\"" + jsonResult.mp4ContentUrl + "\" type=\"video/mp4\"/>" +
			"<source src=\"" + jsonResult.ogvContentUrl + "\" type=\"video/ogg\"/></video>";
	}
	
	//get the theme and decade meta data for the resource
	var meta = "theme = " + jsonResult['theme'] + "&#13;&#10;";
	meta += "decade = " + jsonResult['decade'];
			
	$("#display-content").append(displayContent);
	$("#display-meta").append("<textarea rows=\"2\" id=\"metadata\" class=\"meta-textarea\">" + meta + "</textarea>");


	if (selectedContentType == 'audio') {

		$(".audio-play").click(function() {

			document.getElementById('audioPlayer').play();
			$('#audioplay').addClass('audio-control-active');
		});
	
		$(".audio-pause").click(function() {

			document.getElementById('audioPlayer').pause();
			$('#audioplay').removeClass('audio-control-active');
		});
		
	}
}

//WS Callback - Get by File - Success
function onGetSuccess(result) {
	processGet(result);
}

//WS Callback - Get by File - Error
function onGetError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
}

//WS Callback - Get by File - Complete
function onGetComplete(xhr,status) {
}

function saveResource() {

	//disable the Save and Delete buttons
	$('#btn-save').addClass('disabled');
	$('#btn-delete').addClass('disabled');

	//get theme and decade params
	var theme = null;
	var decade = null;
	
	var newParams = document.getElementById("metadata").value;

	var lines = newParams.split('\n');
	for(var i = 0; i < lines.length;i++){
		var contentLine = lines[i];

		if (contentLine.length > 0) {
	    	contentLine.trim();
	    	var lowercaseContentLine = contentLine.toLowerCase();
	    	var themeIndex = lowercaseContentLine.indexOf("theme");
	    	var decadeIndex = lowercaseContentLine.indexOf("decade");
	    	if (themeIndex != -1) {
				var content = contentLine.split('=');
				theme = content[1].trim();
			} else if (decadeIndex != -1) {
				var content = contentLine.split('=');
				decade = content[1].trim();
			}
		}
	}

	var resultObj = {};
	if (theme != null) {
		resultObj['theme'] = theme;
	}
	if (decade != null) {
		resultObj['decade'] = decade;
	}
	
	var jsonData = JSON.stringify(resultObj);
	
	$.ajax({
    	type:"POST",
    	data: jsonData,
        contentType: "application/json",   	
    	dataType:"html",
	    url: "../ws/"+ selectedContentType + "/metadata/" + selectedFile,
	    success: onPostSuccess,
	    error: onPostError,
	    complete: onPostComplete
    });
}

//WS Post - Get by File - Success
function onPostSuccess(result) {
	//enable the Save and Delete buttons
	$('#btn-save').removeClass('disabled');
	$('#btn-delete').removeClass('disabled');

	getResource(selectedContentType, selectedFile);
}

//WS Callback - Get by File - Error
function onPostError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
	alert("Error - Resource could not be updated");

	//enable the Save and Delete buttons
	$('#btn-save').removeClass('disabled');
	$('#btn-delete').removeClass('disabled');
}

//WS Callback - Get by File - Complete
function onPostComplete(xhr,status) {

}

function deleteResource() {

	//first we need to get rid of any files locks if its audio or video...
	if (selectedContentType == 'audio') {
		document.getElementById('audioPlayer').pause();
		$('#audioPlayer').src = '';
		$('#audioPlayer').children('source').prop('src', '');
		$('#audioPlayer').remove();

		$(".audio-play").off('click');
		$(".audio-pause").off('click');

		$('#audioplay').removeClass('audio-control-active');
	} else if (selectedContentType == 'video') {
		document.getElementById('videoPlayer').pause();
		$('#videoPlayer').src = '';
		$('#videoPlayer').children('source').prop('src', '');
		$('#videoPlayer').remove();
	}

	$.ajax({
    	type:"DELETE",
    	dataType:"html",
	    url: "../ws/"+ selectedContentType + "/metadata/" + selectedFile,
	    success: onDeleteSuccess,
	    error: onDeleteError,
	    complete: onDeleteComplete
    });
}

//WS Callback - Get by File - Success
function onDeleteSuccess(result) {

	//need to do this to refresh the file tree.
	window.location.reload();

}

//WS Callback - Get by File - Error
function onDeleteError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
	alert("Error - Resource could not be deleted");
	window.location.reload();
}

//WS Callback - Get by File - Complete
function onDeleteComplete(xhr,status) {

}
