function openNav() {
    document.getElementById("sideNav").style.width = "350px";
    $("#sideNav").show(500, "linear");
}

function closeNav() {
    document.getElementById("sideNav").style.width = "0";
    $("#sideNav").hide(500, "linear");
}
