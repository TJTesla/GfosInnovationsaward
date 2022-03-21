package gfos.tests;

import gfos.controller.EmployeeRegistrationController;
import gfos.controller.SuperUserController;

public class UserTests {
    static EmployeeRegistrationController emp = new EmployeeRegistrationController();

    public static void main(String[] args) {
        emp.setName("theotes");
        emp.setKey("key");
        emp.setPassword("password");
        emp.setPasswordRepeat("password");

        emp.register();
    }
}
