package gfos.pojos;

// Eigene generische Klasse, die ein Paar mit zwei verschiedene Datentypen darstellt
// Wird anstatt einer HashMap verwendet, da JSF bei HashMaps ein Verhalten benutzt, dass nicht Zielführend war

public class Pair <Type1, Type2>{
    Type1 t1;
    Type2 t2;

    public Pair(Type1 t1, Type2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Type1 first() {
        return this.t1;
    }

    public Type2 second() {
        return this.t2;
    }

    public void set(Pair<Type1, Type2> p) {
        this.t1 = p.t1;
        this.t2 = p.t2;
    }

    public void set(Type1 t1, Type2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
}

