package gfos.inputFields;

import java.util.ArrayList;

public class StandardField {
    protected String value;
    protected String errorMsg;

    public StandardField() {
        value = "";
    }

    public boolean check() {
        return !value.isEmpty();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static boolean checkAmount(ArrayList<StandardField> fields) {
        for (StandardField field : fields) {
            if (!field.check()) {
                return false;
            }
        }

        return true;
    }
}
