function openNav() {
    document.getElementById("sideNav").style.width = "350px";
    $("#sideNav").show(500, "swing");
}

function closeNav() {
    $("#sideNav").hide(500, "swing");
    document.getElementById("sideNav").style.width = "0";
}
function showPopUp() {
    document.getElementById("popup").style.display = "block";
    $('#popup').show();
}
function hidePopUp() {
    document.getElementById("popup").style.display = "none";
    $('#popup').hide();
}