package statemachine;

public class StatemachineEventArgs {
    public float angle = 0;
    public boolean angleChanged = false;

    public String stateName = "";
    public boolean stateNameChanged = false;
    public boolean stateNameWillChange = false;

    public String colorName = "";
    public boolean colorNameChanged = false;

    public float distance = 0;
    public boolean distanceChanged = false;

    public StatemachineEventArgs() {

    }
}
