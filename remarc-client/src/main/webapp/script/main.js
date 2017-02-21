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

var theme;
var decade;

var themeOpen;
var decadeOpen;

function initPage() {
	
	theme = null;
	decade = null;

	themeOpen = false;
	decadeOpen = false;

	$("body").addClass("decade-bg");
	
	$("#container-split-theme").click(function() {
		toggleTheme();			
	})

	$("#container-split-decade").click(function() {
		toggleDecade(); 
	})

	//displays the media row for theme
	$(".btn-theme").click(function() {
		document.getElementById('row-media-theme').style.display = 'block';
		theme = $(this).data("theme");
		
		decade = null;
	})

	//displays the media row for decade
	$(".btn-decade").click(function() {
		document.getElementById('row-media-decade').style.display = 'block';
		theme = null;
		decade = $(this).attr('id');
	})

	//depending on which back button was pressed, toggle theme or decade visibility & hide media rows
	$(".btn-back").click(function() {
		if (this.id == "btn-back-theme") {
			document.getElementById('row-media-theme').style.display = 'none';
			toggleTheme();
		} else if (this.id == "btn-back-decade") {
			document.getElementById('row-media-decade').style.display = 'none';
			toggleDecade();
		}
	})

	//when the media image button is pressed, navigate to images.html
	$(".btn-media-image").click(function() {
		navigateToContentUrl('images');
	})

	//when the media audio button is pressed, do nothing
	$(".btn-media-audio").click(function() {
		navigateToContentUrl('audio');
	})

	//when the media audio button is pressed, do nothing
	$(".btn-media-video").click(function() {
		navigateToContentUrl('video');
	})
	
	$("#nav-information").click(function() {
		$('#introduction-modal').modal('show');
	})
	
	$("#nav-about").click(function() {
		$('#about-modal').modal('show');
	})

	displayIntroductionModal();

	getThemes();
}

function displayIntroductionModal() {

	var shownIntro = localStorage.getItem('introductionShown');

	if (shownIntro == null) {
		$('#introduction-modal').modal('show');
		localStorage.setItem('introductionShown', 'true');
	}
}

function navigateToContentUrl(contentType) {

	var baseUrl = "content.html?content=" + contentType;
	if (theme != null) {
		baseUrl = baseUrl + "&theme=" + theme;
	}
	if (decade != null) {
		baseUrl = baseUrl + "&decade=" + decade;
	}

	location.href = baseUrl;
	
}

function toggleTheme() {
	$("#container-options").toggle();
	$("#container-theme").toggle();

	themeOpen = !themeOpen;
	if (themeOpen) {
		$("body").addClass("theme-bg");
		$("body").removeClass("decade-bg");
	} else {
		$("body").addClass("decade-bg");
		$("body").removeClass("theme-bg");
	}
	
	return;
};

function toggleDecade() {
	$("#container-options").toggle();
	$("#container-decade").toggle();

	decadeOpen = !decadeOpen;
	if (decadeOpen) {
		$("body").addClass("decade-bg");
		$("body").removeClass("theme-bg");
	}
	
	return;
};

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
	
		$("#theme-" + (i+1)).html(obj.name);
		$("#theme-" + (i+1)).data("theme", obj.name);
		
	});
}

function onGetError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
}

function onGetComplete(xhr,status) {

}