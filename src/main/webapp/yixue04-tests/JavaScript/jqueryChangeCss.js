var s = document.createElement("script");
s.src = "https://code.jquery.com/jquery-3.6.0.js";
s.onload = function (e) {}

function ChangeCss(){
    $('p').css('font-family', 'monospace');
    $('code').css('border', '1px solid #aaa');
}

color = $('#elem').css('color'); //gets the color of the id elem
color = $('elem').css('color'); //same with element selector
color = $('.elem').css('color'); //same with class selector
$('hi', '.bye', '#cu').css('color', 'blue'); //combining selectors 