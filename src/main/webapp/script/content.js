/**
 * Javascript functions for content.html
 */

var theme;
var decade;
var content;
	
var thumbnailsPerRow;
var mediaRetrieveLimit;
var imageRetrieveLimit;

var favouritesArray;

function initPage() {
	
	imageRetrieveLimit = 30;
	mediaRetrieveLimit = 20;
	thumbnailsPerRow = 3;
	
	theme = getParameterByName('theme');
	decade = getParameterByName('decade');
	content = getParameterByName('content');

	console.log('recieved params content = ' + content + ' and theme = ' + theme + ' and decade = ' + decade);
	
	/*This should add in touch support to the carousel*/
	$(".carousel").swiperight(function() {  
		  $(this).carousel('prev');  
    		});  
	$(".carousel").swipeleft(function() {  
	  $(this).carousel('next');  
   			});

	$('#imgCarousel').on('slide.bs.carousel',function(e){

		var slideFrom = $(this).find('.active').index();
    	var slideTo = $(e.relatedTarget).index();

		if (content == 'audio') {
	    	
			//stop the current one
	    	document.getElementById('audioPlayer' + slideFrom).pause();
	    	document.getElementById('audioPlayer' + slideFrom).currentTime = 0;
	    	
	    	$('#audioplay_' + slideFrom).removeClass('audio-control-active');
	    	
	    	//start the next one
	    	$('#audioplay_' + slideTo).trigger("click");
	    	
		} else if (content == 'video') {

			//stop the current one
			document.getElementById('videoPlayer' + slideFrom).pause();
	    	document.getElementById('videoPlayer' + slideFrom).currentTime = 0;
	    	
	    	//start the next one
	    	document.getElementById('videoPlayer' + slideTo).play();
		}
	});
	
	// when the carousel has finished sliding, auto update selections
	$('#imgCarousel').on('slid.bs.carousel', function () {
		var id = $('.item.active').data('slide-number');
		
		//update the thumbnail selectors
		$('[id^=carousel-selector-]').removeClass('selected-img');
		$('[id=carousel-selector-'+id+']').addClass('selected-img');

		//now need to update the thumbnail carousel...
		var carouselId = Math.floor(id/thumbnailsPerRow);
		$('#thumbnailCarousel').carousel(carouselId);

		//and the favourites...
		updateFavourites();
		
	});

	$('#fav-text').click(function() {
		toggleFavourites();
	});

	configureFavourites();
	
	findContent(content, theme, decade);
	
}


//Gets Query String params
function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

//Retrieves locally stored favourites
function configureFavourites() {
	
	var favStr = localStorage["favourites-" + content];
	if (favStr == null) {
		favouritesArray = new Array();
	} else {
		favouritesArray = JSON.parse(favStr);
	}
}

//Checks to see if the named item is in the favourites array
function isInFavourites(name) {

	var result = false;
	if (name != null && name != "" & favouritesArray != null) {

		for (var i = 0; i < favouritesArray.length; i++ ) {
			if (favouritesArray[i] == name) {
				result = true;
				break;
			}
		}
		
	}

	return result;
}

//Removes named item from the favourites array
function removeFromFavourites(name) {

	var result = false;
	if (name != null && name != "" & favouritesArray != null) {

		for (var i = 0; i < favouritesArray.length; i++ ) {
			if (favouritesArray[i] == name) {
				favouritesArray.splice(i, 1);
				result = true;
				break;
			}
		}
	}

	return result;
}

function getCurrentContentUrl() {

	var contentUrl;

	if (content == 'images') {
		contentUrl = $(".item.active").find('img').attr('src');
	} else if (content == 'audio') {
		var id = $('.item.active').data('slide-number');
		contentUrl = $("#audioPlayer" + id).find('source').attr('src');
	} else if (content == 'video') {
		contentUrl = $(".item.active").find('source').attr('src');
	} 

	return contentUrl;
}

//Toggle Favourites
function toggleFavourites() {

	var favText = $('#fav-text');
	var favStar = $('#fav-star');

	favStar.toggleClass("favourite-on");

	var contentUrl = getCurrentContentUrl();

	if (contentUrl != null && contentUrl != "") {
		var contentId = contentUrl.substr(contentUrl.lastIndexOf("/") +1);
		contentId = contentId.substr(0, contentId.lastIndexOf("."));
	
		if (favText.text() == favText.data("text-add-fav") ) { 
		
			if (!isInFavourites(contentId)) {
				favouritesArray.push(contentId);
				localStorage["favourites-" + content] = JSON.stringify(favouritesArray);
			}
		
			favText.contents().first().replaceWith(favText.data("text-rem-fav")); 
		
		} else {

			removeFromFavourites(contentId);
			localStorage["favourites-" + content] = JSON.stringify(favouritesArray);

			favText.contents().first().replaceWith(favText.data("text-add-fav"));
		}
	}
}

//Update Favourites Text/Icon
function updateFavourites() {

	var favText = $('#fav-text');
	var favStar = $('#fav-star');
	
	var contentUrl = getCurrentContentUrl();

	if (contentUrl != null && contentUrl != "") {
		
		var contentId = contentUrl.substr(contentUrl.lastIndexOf("/") +1);
		contentId = contentId.substr(0, contentId.lastIndexOf("."));
	
		if (isInFavourites(contentId)) {
			if (!favStar.hasClass("favourite-on") ) {
				favStar.addClass("favourite-on");
				favText.contents().first().replaceWith(favText.data("text-rem-fav"));
			}
		} else {
			if (favStar.hasClass("favourite-on") ) {
				favStar.removeClass("favourite-on");
				favText.contents().first().replaceWith(favText.data("text-add-fav"));
			} 
		}
	}
}

function findFavourites() {

	var retrieveLimit;
	if (content == 'images') {
		retrieveLimit = imageRetrieveLimit;
	} else {
		retrieveLimit = mediaRetrieveLimit;
	}
	
	var shuffledFavourites = shuffleArray(favouritesArray);
	var favouritesToRetrieve = shuffledFavourites.slice(0, retrieveLimit);
	
	//retrieve each element individually, then build up a json array
	var favouriteJson = new Array();

	retrieveFavouriteList(favouritesToRetrieve, 0, favouriteJson);
}

function retrieveFavouriteList(favouritesToRetrieve, i, resultArray) {

	var id = favouritesToRetrieve[i];
	
	$.ajax({
    	type:"GET",
        dataType:"json",
	    url: "ws/"+ content + "/metadata/" + id,
	    success: onRetrieveFavSuccess(favouritesToRetrieve, i, resultArray),
	    error: onRetrieveFavError,
	    complete: onRetrieveFavComplete(favouritesToRetrieve, i, resultArray)
    });
}

function onRetrieveFavSuccess(favouritesToRetrieve, i, resultArray) {
	return function (result) {
		resultArray.push(result);
	}
}

function onRetrieveFavError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
}

function onRetrieveFavComplete(favouritesToRetrieve, i, resultArray) {
	return function (xhr,status) {

		var newIndex = i + 1;
		if (newIndex < favouritesToRetrieve.length) {
			retrieveFavouriteList(favouritesToRetrieve, newIndex, resultArray);
		} else {

			//save the results in local storage
			writeToLocalData(resultArray);
			
			//call the update method
			displayContent();
		}
	}
}

//Makes the call to the WS to get our carousel data
function findContent(content, theme, decade) {

	//if we've asked for Favourites, then we need to retrieve in a different manner...
	if (theme == 'Favourites-Theme' || decade == 'Favourites-Decade') {

		findFavourites();
		
	} else {

		var findParams = "";
		if (theme != null & theme != "") {
			findParams = "&theme=" + theme;
		}
		if (decade != null & decade != "") {
			findParams = "&decade=" + decade;
		}

		var retrieveLimit;
		if (content == 'images') {
			retrieveLimit = imageRetrieveLimit;
		} else {
			retrieveLimit = mediaRetrieveLimit;
		}
		
		//Retrieve content normally
		$.ajax({
    		type:"GET",
        	dataType:"json",
	    	url: "ws/"+ content + "/find?limit=" + retrieveLimit + findParams,
	    	success: onFindSuccess,
	    	error: onFindError,
	    	complete: onFindComplete
    	});
	}	
}

//WS Callback - Success
function onFindSuccess(result) {
	writeToLocalData(result);		
}

//WS Callback - Error
function onFindError(xhr, status, result) {
	console.log("Error: "+xhr.responseText);
}

//WS Callback - Complete
function onFindComplete(xhr,status) {
	displayContent();
}

function displayContent() {
	
	$("#carouselContent").empty();
	$("#thumbnailContent").empty();

		var metaImageJson = readFromLocalData();
		console.log("Media Meta Cache: " + JSON.stringify(metaImageJson));

		//the class used for the carousel images needs modifying if we're in audio mode
		var carouselImgClass = "carousel-img";
		if (content == 'audio') {
			carouselImgClass += " carousel-img-audio";
		}

		var section = -1;
		
		$.each(metaImageJson, function(i, obj) {

			//the carousel div and thumbnail img classes need modifying for the 1st element to make it active and selected
			var itemDivClass = "item";
			var thumbnailImgClass = "";
			if (i == 0) {
				itemDivClass += " active";
				thumbnailImgClass += " selected-img";
			}

			//if obj.imageUrl is null, we want to replace it with the TestCard
			if (obj.imageUrl == null) {

				if (content == 'images') {
					obj.imageUrl = "./images/test_card_colour.jpg";
				} else {
					obj.imageUrl = "./images/test_card_bw.jpg";
				}
			}
			
			//The inner content of the carousel will either be an image, an image plus an audio tag, or a video
			var carouselInnerContent = "<img class=\"" + carouselImgClass +"\" src=\"" + obj.imageUrl + "\" alt=\""+ obj.id +"\">";

			//removing the background image blur, to re-enable, set this: "<div id=\"img-bg-" + i + "\" class=\"carousel-img-bg\"></div>";
			var carouselInnerContentBg = ""; 
			
			var audioPlayerDiv = "";
			var videoPlayerDiv = "";
			
			if (content == 'audio') {
							
				//if the content we've loaded is audio, we'll want to add the audio player
				$("#audioPlayers").append("<audio id=\"audioPlayer" + i + "\" preload=\"none\">" +
  					"<source src=\"" + obj.mp3ContentUrl + "\" type=\"audio/mpeg\"/>" +
  					"<source src=\"" + obj.oggContentUrl + "\" type=\"audio/ogg\"/></audio>");

				//create the controls for each added audio player and insert them as a caption into the carousel later
				audioPlayerDiv = "<div class=\"carousel-caption\">" +
					"<span id=\"audioplay_" + i + "\" class=\"glyphicon glyphicon-play audio-control audio-play\"></span><span class=\"sr-only\">Play Audio</span>" + 
					"<span id=\"audiopause_" + i + "\" class=\"glyphicon glyphicon-pause audio-control audio-pause\"></span><span class=\"sr-only\">Pause Audio</span></div>"
					
				//add the audio div onto the carouselInnerContent so that it's an image plus audio controls
				carouselInnerContent += audioPlayerDiv;
			
			} else if (content == 'video') {
				
				videoPlayerDiv = "<video id=\"videoPlayer" + i + "\" controls width=\"100%\" height=\"100%\" preload=\"none\">" +
					"<source src=\"" + obj.mp4ContentUrl + "\" type=\"video/mp4\"/>" +
					"<source src=\"" + obj.ogvContentUrl + "\" type=\"video/ogg\"/></video>";

				//replace the carouselInnerContent with the video tag
				carouselInnerContent = videoPlayerDiv;
			}

			
			//finally, we always want to add the carouselContent and the thumbnails
			$("#carouselContent").append("<div class=\"" + itemDivClass + "\" data-slide-number=\"" + i +
					"\">" + carouselInnerContentBg + carouselInnerContent + "</div>");

			if (i == 0 || (i % thumbnailsPerRow) == 0) {
				
				//if we're creating a new thumbnail row, finish this one with a next button
				if (i != 0) {
					$("#thumbnails-slide-" + section).append("<div id=\"thumbnails-next-" + section + "\" class=\"container-thumbnail-content thumbnail-nav\" data-slide=\"next\"><div class=\"thumbnail-next-text\">Next <span class=\"glyphicon glyphicon-chevron-right\"></span></div></div>");
				}
				
				section ++;
				$("#thumbnailContent").append("<div id=\"thumbnails-slide-" + section + "\" class=\"container-thumbnail-navigation " + itemDivClass + "\"></div>");
				
				//add the previous button to start a new thumbnail row
				$("#thumbnails-slide-" + section).append("<div id=\"thumbnails-prev-" + section + "\" class=\"container-thumbnail-content thumbnail-nav\" data-slide=\"prev\"><div class=\"thumbnail-prev-text\"><span class=\"glyphicon glyphicon-chevron-left\"></span> Prev</div></div>");
			}

			$("#thumbnails-slide-" + section).append("<div id=\"carousel-selector-" + i + "\" class=\"container-thumbnail-content" + thumbnailImgClass + "\"></div>");
		
			$("#carousel-selector-" + i).css('background-image', "url(" + obj.imageUrl + ")");

			//removing the image blur javascript
			//$("#img-bg-" + i).css('background-image', "url(" + obj.imageUrl + ")");
			
		});
		
		//always end by adding a final cell for the next button, to balance the thumbnails out
		$("#thumbnails-slide-" + section).append("<div id=\"thumbnails-next-" + section + "\" class=\"container-thumbnail-content thumbnail-nav\" data-slide=\"next\"><div class=\"thumbnail-next-text\">Next <span class=\"glyphicon glyphicon-chevron-right\"></span></div></div>");
		
		configureNewElements();

		updateFavourites();
		
		startFirstMedia();
}

//Stores the JSON image data to local storage
function writeToLocalData(data) {
	localStorage.setItem('lastMetaData', JSON.stringify(data));
}

//Retrieves the JSON image data from local storage
function readFromLocalData() {
	return JSON.parse(localStorage.getItem('lastMetaData'));
}

//Adds click methods to the carousel selectors (thumbnails) & audio controls
function configureNewElements() {
	$('[id^=carousel-selector-]').click( function(){
	  var id_selector = $(this).attr("id");

	  var id = id_selector.substr(id_selector.lastIndexOf("-") +1);
	  
	  id = parseInt(id);
	  $('#imgCarousel').carousel(id);
	  $('[id^=carousel-selector-]').removeClass('selected-img');
	  $(this).addClass('selected-img');
	});

	$('.thumbnail-nav').click( function() {
		
		var operation = $(this).data('slide');
		$('#thumbnailCarousel').carousel(operation)

	});
	
	if (content == 'audio') {
	
		$(".audio-play").click(function() {
	
			//get the id of the button and work out the index from it
			var index = getAudioButtonIndex($(this).attr('id'));
			document.getElementById('audioPlayer' + index).load();
			document.getElementById('audioPlayer' + index).play();

			$('#audioplay_' + index).addClass('audio-control-active');
		});
	
		$(".audio-pause").click(function() {

			//get the id of the button and work out the index from it
			var index = getAudioButtonIndex($(this).attr('id'));
			document.getElementById('audioPlayer' + index).pause();

			$('#audioplay_' + index).removeClass('audio-control-active');
		});

	}
}

//Starts the first element of media playing (this negates the need for the autoplay tag, which causes buffering issues on start/stop)
function startFirstMedia() {
	
	if (content == 'audio') {
		//if exists
		$('#audioplay_0').trigger("click");
	} else if (content == 'video') {
		//if exists
		document.getElementById('videoPlayer0').play();
	}
	
}

//Takes the ID of an audio button in the format audioplay_ID, and returns the ID portion
function getAudioButtonIndex(id) {
	var tokens = id.split("_");
	return tokens[1];
}

//shuffles a given array using the Fisher-Yates (aka Knuth) Shuffle
function shuffleArray(array) {
	var currentIndex = array.length, temporaryValue, randomIndex ;

	// While there remain elements to shuffle...
	while (0 !== currentIndex) {

		// Pick a remaining element...
	    randomIndex = Math.floor(Math.random() * currentIndex);
	    currentIndex -= 1;

	    // And swap it with the current element.
	    temporaryValue = array[currentIndex];
	    array[currentIndex] = array[randomIndex];
	    array[randomIndex] = temporaryValue;
	}

	return array;
}