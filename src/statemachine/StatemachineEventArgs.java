package statemachine;

public class StatemachineEventArgs {
    public float angle = 0;
    public boolean angleChanged = false;

    public String stateName = "";
    public boolean stateNameChanged = false;

    public String colorName = "";
    public boolean colorNameChanged = false;

    public StatemachineEventArgs() {

    }
}