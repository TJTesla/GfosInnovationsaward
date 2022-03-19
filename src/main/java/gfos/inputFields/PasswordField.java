package gfos.inputFields;

import gfos.Regexes;

import java.util.regex.Pattern;

public class PasswordField extends RepeatingField {
    public PasswordField() {
        super();
    }

    @Override
    public boolean check() {
        super.check();

        return !Pattern.compile(Regexes.password).matcher(value).find() && !value.isEmpty();
    }
}
