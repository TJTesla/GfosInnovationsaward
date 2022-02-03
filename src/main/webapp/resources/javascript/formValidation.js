//check consensus of emails
var mail = $("#email").value();
var mailR = $("#emailRepeat").value();

var checkMail = $("#checkMail");
while (mail !== mailR) {
    checkMail.style.color = "red";
    checkMail.innerHTML = "Die Eingaben stimmen nicht überein.";
}
while (mail === mailR) {
    checkMail.style.color = "green";
    checkMail.innerHTML = "";
}

//Check consensus of passwords
var pswd = $("#password").value();
var pswdR = $("#passwordRepeat").value();

var checkPswd = $("#checkPswd");
while (pswd !== pswdR) {
    checkPswd.style.color = "red";
    checkPswd.innerHTML = "Die Eingaben stimmen nicht überein.";
}
while (pswd === pswdR) {
    checkPswd.style.color = "green";
    checkPswd.innerHTML = "";
}