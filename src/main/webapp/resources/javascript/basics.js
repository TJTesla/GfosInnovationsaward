function openNav() {
    document.getElementById("sideNav").style.width = "350px";
    $("#sideNav").show(500, "swing");
}

function closeNav() {
    $("#sideNav").hide(500, "swing");
    document.getElementById("sideNav").style.width = "0";
}
function showPopUp(i) {
    document.getElementById("popup" + i).style.display = "block";
    $('#popup').show();
}
function hidePopUp(i) {
    document.getElementById("popup" + i).style.display = "none";
    $('#popup').hide();
}