package sy.bishe.ygou.delegate.personal.myrelease;

public abstract interface MyReleaseEventLisenter {
    abstract void deleteOnsell(int position);
    abstract void deleteOnorder(int position);
    abstract void send(int position);
    abstract void receive(int position);
    abstract void evl(int position);
    abstract void look(int position);
}
