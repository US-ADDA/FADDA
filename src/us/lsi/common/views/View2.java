package us.lsi.common.views;

public class View2<D> {


    private final D left, right;

    public View2(D left, D right) {
        this.left = left;
        this.right = right;
    }

    public D left() {
        return left;
    }

    public D right() {
        return right;
    }

    public static <D> View2<D> of(D left, D right) {
        return new View2<>(left, right);
    }

}
