package distancesensor;

public interface IDistanceObservable {
    void subscribeToDistanceChange(IDistanceObserver observer);
    void unsubscribeToDistanceChange(IDistanceObserver observer);
    void finishUnsubscribeToDistanceChange();
}
