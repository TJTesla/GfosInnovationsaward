package gfos.converter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ApplicationObjectConverter extends ApplicationConverter {
    @PostConstruct
    private void init() {
        object = true;
    }
}
