package us.lsi.common.views;

public class View4<D> {

    private final D a, b, c, d;

    public View4(D a, D b, D c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public D a() {
        return a;
    }

    public D b() {
        return b;
    }

    public D c() {
        return c;
    }

    public D d() {
        return d;
    }

    public static <D> View4<D> of(D a, D b, D c, D d) {
        return new View4<D>(a, b, c, d);
    }

}
