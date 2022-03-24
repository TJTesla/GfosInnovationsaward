window.onclick = function (e) {
    if ($(".dropdown")[0].contains(e.target)) {}
    else {
        $("#dropdownField").css("display", "none");
    }
    if ($(".dropdown")[1].contains(e.target)) {}
    else {
        $("#dropdownLevel").css("display", "none");
    }
    if ($(".dropdown")[2].contains(e.target)) {}
    else {
        $("#dropdownTime").css("display", "none");
    }
}
window.onload = (event) => {
    $("#dropbtnField").click(function() {
        var filter = $("#dropdownField");
        var display = filter.css("display");
        if(display !== "block") {
            filter.css("display", "block");
        } else {
            filter.css("display", "none");
        }
        $("#dropdownLevel").css("display", "none");
        $("#dropdownTime").css("display", "none");
    })
    $("#dropbtnLevel").click(function() {
        var filter = $("#dropdownLevel");
        var display = filter.css("display");
        if(display !== "block") {
            filter.css("display", "block");
        } else {
            filter.css("display", "none");
        }
        $("#dropdownField").css("display", "none");
        $("#dropdownTime").css("display", "none");
    })
    $("#dropbtnTime").click(function() {
        var filter = $("#dropdownTime");
        var display = filter.css("display");
        if(display !== "block") {
            filter.css("display", "block");
        } else {
            filter.css("display", "none");
        }
        $("#dropdownField").css("display", "none");
        $("#dropdownLevel").css("display", "none");
    })
}

