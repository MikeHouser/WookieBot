package statemachine;

public interface IStatemachineObservable {
    void subscribe(IStatemachineObserver observer);
    void unsubscribe(IStatemachineObserver observer);
    void notifyObservers(StatemachineEventArgs e);
}
