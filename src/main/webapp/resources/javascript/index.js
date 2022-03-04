function fieldDrop() {
    $("#dropdownField").classList.toggle("show");
}

function levelDrop() {
    $("#dropdownLevel").classList.toggle("show");
}

function timeDrop() {
    $("#dropdownTime").classList.toggle("show");
}

window.onclick = function(e) {
    var filters;
    if(!e.target.matches('#dropdbtnField')) {
        filters = $("#dropdownField");
        if (filters.classList.contains("show")) {
            filters.classList.remove("show");
        }
    }
    if(!e.target.matches('#dropdbtnLevel')) {
        filters = $("#dropdownLevel");
        if (filters.classList.contains("show")) {
            filters.classList.remove("show");
        }
    }
    if(!e.target.matches('#dropdbtnTime')) {
        filters = $("#dropdownTime");
        if (filters.classList.contains("show")) {
            filters.classList.remove("show");
        }
    }
}