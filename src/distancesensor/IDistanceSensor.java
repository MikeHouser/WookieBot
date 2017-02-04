package distancesensor;

public interface IDistanceSensor {
    void init();
    void deInit();
    float measureDistance();
}
