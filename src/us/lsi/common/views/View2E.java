package us.lsi.common.views;

public class View2E<D, E> {

    private final E e;
    private final D left, right;

    public View2E(E e, D left, D right) {
        this.e = e;
        this.left = left;
        this.right = right;
    }

    public E e() {
        return e;
    }

    public D left() {
        return left;
    }

    public D right() {
        return right;
    }

    public static <D, E> View2E<D, E> of(E e, D left, D right) {
        return new View2E<>(e, left, right);
    }

}
