package gfos.converter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

// Umwandeln einer ID als URL-Parameter in das Motivationsschreiben der Bewerbung mit der ID

@Named
@RequestScoped
public class ApplicationTextConverter extends ApplicationConverter {
    @PostConstruct
    private void init() {
        object = false;
    }
}
