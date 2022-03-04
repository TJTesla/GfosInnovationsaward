package gfos.controller;


import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class TestController implements Serializable {
    private boolean login;

    @PostConstruct
    public void init() {
        this.login = false;
    }

    public boolean isLogin() {
        return login;
    }

    public void changeToLogin() {
        login = true;
    }

    public void changeToRegistration() {
        login = false;
    }
}
