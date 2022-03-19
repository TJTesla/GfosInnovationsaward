package gfos.inputFields;

import gfos.Regexes;
import gfos.database.ApplicantDatabaseService;

import javax.inject.Inject;
import java.util.regex.Pattern;

public class EmailField extends RepeatingField {
    @Inject
    ApplicantDatabaseService adbs;

    public EmailField() {
        super();
    }

    @Override
    public boolean check() {
        super.check();

        if (!Pattern.compile(Regexes.email).matcher(value).find() && !value.isEmpty()) {
            return false;
        }

        return !adbs.emailExists(value);
    }
}
