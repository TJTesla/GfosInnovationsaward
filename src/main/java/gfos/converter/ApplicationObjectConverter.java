package gfos.converter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

// Umwandeln einer ID als URL-Parameter in ein Bewerbung-Objekt

@Named
@RequestScoped
public class ApplicationObjectConverter extends ApplicationConverter {
    @PostConstruct
    private void init() {
        object = true;
    }
}
