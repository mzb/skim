package pk.dyplom.event;

public interface EventHandler<E> {

    public void handle(E event);
}
