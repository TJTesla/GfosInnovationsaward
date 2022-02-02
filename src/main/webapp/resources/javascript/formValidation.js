//check consensus of emails
var mail = $("#email").value();
var mailR = $("#emailRepeat").value();

var checkMail = $("#checkMail");
if(mail === mailR) {
    checkMail.style.color = "green";
    checkMail.innerHTML = "";
} else {
    checkMail.style.color = "red";
    checkMail.innerHTML = "Die Eingaben stimmen nicht überein.";
}

//Check consensus of passwords
var pswd = $("#password").value();
var pswdR = $("#passwordRepeat").value();

var checkPswd = $("#checkPswd");
if(pswd===pswdR) {
    checkPswd.style.color = "green";
    checkPswd.innerHTML = "";
} else {
    checkPswd.style.color = "red";
    checkPswd.innerHTML = "Die Eingaben stimmen nicht überein.";
}