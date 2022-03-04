function openNav() {
    document.getElementById("sideNav").style.width = "250px";
    $("#sideNav").show(500, "linear");
}

function closeNav() {
    document.getElementById("sideNav").style.width = "0";
    $("#sideNav").hide(500, "linear");
}

function redirect(url) {
    var pageUrl = $(location).attr("href");
    url = pageUrl.toString() + url.toString();
    window.location.href = url;
}