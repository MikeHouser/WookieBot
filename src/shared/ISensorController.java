package shared;

public interface ISensorController {
    void init();
    void deInit();
    void startListening();
    void stopListening();
}
