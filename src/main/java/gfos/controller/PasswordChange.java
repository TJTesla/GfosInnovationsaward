package gfos.controller;

import gfos.Regexes;
import gfos.beans.Applicant;
import gfos.beans.Employee;
import gfos.beans.User;
import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

@Named
@ViewScoped
public class PasswordChange implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;

    private String oldPwd;
    private String newPwd;
    private String newRepeat;

    boolean success;
    HashMap<String, String> errorMsgs = new HashMap<>();

    public String changePwd() {
        errorMsgs.clear();
        success = true;
        checkData();
        if (!success) {
            return "";
        }

        if (!adbs.changePwd((Applicant)(cu.getCurrentUser()), newPwd)) {
            return "";
        }
        return "/01-user/userProfile.xhtml?faces-redirect=true";
    }

    private void checkData() {
        if (oldPwd.isEmpty()) {
            success = false;
            errorMsgs.put("oldPwd", "Es muss das alte Passwort angegeben werden.");
        }
        if (newPwd.isEmpty()) {
            success = false;
            errorMsgs.put("newPwd", "Es muss ein neues Passwort angegeben werden.");
        }

        if (!newPwd.equals(newRepeat)) {
            success = false;
            errorMsgs.put("pwdRepeat", "Neue Passwörter müssen übereinstimmen.");
        }

        if (!Pattern.compile(Regexes.password).matcher(newPwd).find() && !newPwd.isEmpty()) {
            success = false;
            errorMsgs.put("newPwd", "Das Passwort muss mindestens einen Großbuchstaben, einen Kleinbuchstaben, ein Sonderzeichen und eine Zahl beinhalten.");
        }

        User u = adbs.loginAttempt(cu.getCurrentUser().getName(), oldPwd);
        if (u == null || u instanceof Employee) {
            success = false;
            errorMsgs.put("oldPwd", "Das angegebene Passwort ist nicht korrekt.");
        }
    }

    public String errorMsg(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getNewRepeat() {
        return newRepeat;
    }

    public void setNewRepeat(String newRepeat) {
        this.newRepeat = newRepeat;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
