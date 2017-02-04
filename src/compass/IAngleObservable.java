package compass;

public interface IAngleObservable {
	void subscribeToAngleChange(IAngleObserver observer);
	void unsubscribeToAngleChange(IAngleObserver observer);
    void finishUnsubscribeToAngleChange();
}
