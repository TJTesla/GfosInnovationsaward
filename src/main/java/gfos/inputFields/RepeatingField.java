package gfos.inputFields;

public class RepeatingField extends StandardField {
    protected String repitition;

    public RepeatingField() {
        super();
    }

    @Override
    public boolean check() {
        super.check();

        return value.equals(repitition);
    }

    public String getRepitition() {
        return repitition;
    }

    public void setRepitition(String repitition) {
        this.repitition = repitition;
    }
}
