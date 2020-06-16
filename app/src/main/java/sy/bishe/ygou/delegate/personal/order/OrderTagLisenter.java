package sy.bishe.ygou.delegate.personal.order;

public interface OrderTagLisenter {
    void send(int position);
    void receive(int position);
    void evl(int position);
    void delete(int position);
    void look(int position);
}
