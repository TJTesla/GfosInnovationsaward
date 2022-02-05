function checkValidation() {
//check consensus of emails
    var mail = $("#email").value();
    var mailR = $("#emailRepeat").value();

    var checkMail = $("#checkMail");
    if (mail !== mailR) {
        checkMail.style.color = "red";
        checkMail.innerHTML = "Die Eingaben stimmen nicht überein.";
    }
    else {
        checkMail.style.color = "green";
        checkMail.innerHTML = "";
    }
//Check consensus of passwords
    var pswd = $("#password").value();
    var pswdR = $("#passwordRepeat").value();

    var checkPswd = $("#checkPswd");
    if (pswd !== pswdR) {
        checkPswd.style.color = "red";
        checkPswd.innerHTML = "Die Eingaben stimmen nicht überein.";
    }
    else {
        checkPswd.style.color = "green";
        checkPswd.innerHTML = "";
    }
}

function toRegistration() {
    $("#login").hide();
    $("#registration").show();
}

function toLogin() {
    $("#registration").show();
    $("#login").hide()
}