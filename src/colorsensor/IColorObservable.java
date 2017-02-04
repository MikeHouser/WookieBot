package colorsensor;

public interface IColorObservable {
    void subscribeToColorChange(IColorObserver observer);
    void unsubscribeToColorChange(IColorObserver observer);
    void finishUnsubscribeToColorChange();
}