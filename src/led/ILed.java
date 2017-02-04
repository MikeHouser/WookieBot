package led;

public interface ILed {
    void init();
    void deInit();
    void activate();
    void deactivate();
    void setState(boolean activated);
}
