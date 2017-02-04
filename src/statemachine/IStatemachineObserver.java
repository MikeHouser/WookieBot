package statemachine;

public interface IStatemachineObserver {
    void handleStatemachineEvent(StatemachineEventArgs args);
}
