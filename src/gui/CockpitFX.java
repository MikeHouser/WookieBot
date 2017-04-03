package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Wookiebot;
import statemachine.IStatemachineObserver;
import statemachine.RobotStateContext;
import statemachine.StatemachineEventArgs;

public class CockpitFX extends Application implements IStatemachineObserver {

    private final boolean DEBUG = false;

    private Wookiebot wookiebot;
    private RobotStateContext robotStateContext;

    //region JavaFX Controls
    private Scene scene;

    private GridPane gridPane;

    private HBox hBoxRobot;
    private Label tLabelRobot;
    private ToggleGroup tGroupRobot;
    private ToggleButton tButtonRobotOn;
    private ToggleButton tButtonRobotOff;

    // State
    private Label lblStateValue;

    // Angle
    private HBox hBoxAngle;
    private Label lblAngleCaption;
    private Label lblAngleValue;
    private ToggleGroup tGrpAngle;
    private ToggleButton tBtnAngleOff;
    private ToggleButton tBtnAngleOn;

    // Color
    private HBox hBoxColor;
    private Label lblColorCaption;
    private Label lblColorValue;
    private ToggleGroup tGrpColor;
    private ToggleButton tBtnColorOff;
    private ToggleButton tBtnColorOn;

    // Distance
    private HBox hBoxDistance;
    private Label lblDistanceCaption;
    private Label lblDistanceValue;
    private ToggleGroup tGrpDistance;
    private ToggleButton tBtnDistanceOff;
    private ToggleButton tBtnDistanceOn;

    // Movement
    private Label lblTurn;
    private HBox hBoxTurn;
    private Button btnTurnLeft;
    private Button btnTurnRight;
    private TextField txtTurnAngle;

    private HBox hBoxFollowLine;
    private Label lblFollowLineCpation;
    private ToggleGroup tGroupFollowLine;
    private ToggleButton tBtnFollowLineOn;
    private ToggleButton tBtnFollowLineOff;
    //endregion

    private String defaultString = "<?>";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.initRobotStuff();

        this.gridPane = new GridPane();
        this.gridPane.setGridLinesVisible(DEBUG);
        this.gridPane.setHgap(10);
        this.gridPane.setVgap(10);
        this.gridPane.setPadding(new Insets(10, 10, 10, 10));

        this.scene = new Scene(gridPane, 500, 500);

        //region Robot On-Off/State
        this.tLabelRobot = new Label("Robot:");

        // State
        this.lblStateValue = new Label();
        this.lblStateValue.setText(this.defaultString);

        // Toggle On/Off
        this.tGroupRobot = new ToggleGroup();

        this.tButtonRobotOff = new ToggleButton("Off");
        this.tButtonRobotOff.setToggleGroup(this.tGroupRobot);
        this.tButtonRobotOff.setSelected(true);
        this.tButtonRobotOff.setOnAction(e -> this.wookiebot.stop());

        this.tButtonRobotOn = new ToggleButton("On");
        this.tButtonRobotOn.setToggleGroup(this.tGroupRobot);
        this.tButtonRobotOn.setOnAction(e -> this.wookiebot.start());

        this.hBoxRobot = new HBox();
        this.hBoxRobot.getChildren().addAll(this.tButtonRobotOff, this.tButtonRobotOn);

        this.gridPane.add(this.tLabelRobot, 0, 0);
        this.gridPane.add(this.lblStateValue, 1, 0);
        this.gridPane.add(this.hBoxRobot, 2, 0);
        //endregion

        //region Angle
        this.lblAngleCaption = new Label();
        this.lblAngleCaption.setText("Angle:");
        this.lblAngleValue = new Label();
        this.lblAngleValue.setText(this.defaultString);
        // Toggle On/Off
        this.tGrpAngle = new ToggleGroup();
        this.tBtnAngleOff = new ToggleButton("Off");
        this.tBtnAngleOff.setToggleGroup(this.tGrpAngle);
        this.tBtnAngleOff.setSelected(true);
        this.tBtnAngleOn = new ToggleButton("On");
        this.tBtnAngleOn.setToggleGroup(this.tGrpAngle);

        this.hBoxAngle = new HBox();
        this.hBoxAngle.getChildren().addAll(this.tBtnAngleOff, this.tBtnAngleOn);

        this.gridPane.add(this.lblAngleCaption, 0, 2);
        this.gridPane.add(this.lblAngleValue, 1, 2);
        this.gridPane.add(this.hBoxAngle, 2, 2);
        //endregion

        //region Color
        this.lblColorCaption = new Label();
        this.lblColorCaption.setText("Color:");

        this.lblColorValue = new Label();
        this.lblColorValue.setText(this.defaultString);

        // Toggle On/Off
        this.tGrpColor = new ToggleGroup();
        this.tBtnColorOff = new ToggleButton("Off");
        this.tBtnColorOff.setToggleGroup(this.tGrpColor);
        this.tBtnColorOff.setSelected(true);
        this.tBtnColorOn = new ToggleButton("On");
        this.tBtnColorOn.setToggleGroup(this.tGrpColor);

        this.hBoxColor = new HBox();
        this.hBoxColor.getChildren().addAll(this.tBtnColorOff, this.tBtnColorOn);

        this.gridPane.add(this.lblColorCaption, 0, 3);
        this.gridPane.add(this.lblColorValue, 1, 3);
        this.gridPane.add(this.hBoxColor, 2, 3);
        //endregion

        //region Distance
        this.lblDistanceCaption = new Label();
        this.lblDistanceCaption.setText("Distance:");

        this.lblDistanceValue = new Label();
        this.lblDistanceValue.setText(this.defaultString);

        // Toggle On/Off
        this.tGrpDistance = new ToggleGroup();
        this.tBtnDistanceOff = new ToggleButton("Off");
        this.tBtnDistanceOff.setToggleGroup(this.tGrpDistance);
        this.tBtnDistanceOff.setSelected(true);
        this.tBtnDistanceOn = new ToggleButton("On");
        this.tBtnDistanceOn.setToggleGroup(this.tGrpDistance);

        this.hBoxDistance = new HBox();
        this.hBoxDistance.getChildren().addAll(this.tBtnDistanceOff, this.tBtnDistanceOn);

        this.gridPane.add(this.lblDistanceCaption, 0, 4);
        this.gridPane.add(this.lblDistanceValue, 1, 4);
        this.gridPane.add(this.hBoxDistance, 2, 4);
        //endregion

        //region Turn
        this.lblTurn = new Label("Turn");

        this.btnTurnLeft = new Button("Turn left");
        this.btnTurnRight = new Button("Turn right");
        this.txtTurnAngle = new TextField();
        this.txtTurnAngle.setText("90");

        this.hBoxTurn = new HBox();
        this.hBoxTurn.getChildren().addAll(this.btnTurnLeft, this.btnTurnRight);

        this.gridPane.add(this.lblTurn, 0, 5);
        this.gridPane.add(this.txtTurnAngle, 1, 5);
        this.gridPane.add(this.hBoxTurn, 2, 5);
        //endregion

        //region Follow Line
        this.lblFollowLineCpation = new Label("Follow line:");
        this.tGroupFollowLine = new ToggleGroup();
        this.tBtnFollowLineOff = new ToggleButton("Off");
        this.tBtnFollowLineOff.setToggleGroup(this.tGroupFollowLine);
        this.tBtnFollowLineOff.setSelected(true);
        this.tBtnFollowLineOn = new ToggleButton("On");
        this.tBtnFollowLineOn.setToggleGroup(this.tGroupFollowLine);

        this.hBoxFollowLine = new HBox();
        this.hBoxFollowLine.getChildren().addAll(this.tBtnFollowLineOff, this.tBtnFollowLineOn);

        this.gridPane.add(this.lblFollowLineCpation, 0, 6);
        this.gridPane.add(this.hBoxFollowLine, 2, 6);
        //endregion

        primaryStage.setTitle("Robot GUI");
        primaryStage.setScene(scene);
        primaryStage.show();

        this.setOnlineState(false);
    }

    @Override
    public void stop(){
        this.exitProcedure();
    }

    private void initRobotStuff() {
        this.wookiebot = new Wookiebot();
        this.robotStateContext = this.wookiebot.getRobotStateContext();
        this.robotStateContext.subscribe(this);
    }

    @Override
    public void handleStatemachineEvent(StatemachineEventArgs args) {
        if (args.angleChanged) {
            Platform.runLater(() -> this.lblAngleValue.setText(args.angle + "°"));
        } else if (args.stateNameChanged) {
            Platform.runLater(() -> this.lblStateValue.setText(args.stateName));
            boolean isOnline = !(args.stateName.toUpperCase().equals("OFFLINE"));
            Platform.runLater(() -> this.setOnlineState(isOnline));
        } else if (args.colorNameChanged) {
            Platform.runLater(() -> this.lblColorValue.setText(args.colorName));
        } else if (args.distanceChanged) {
            Platform.runLater(() -> this.lblDistanceValue.setText(args.distance + " cm"));
        }
    }

    public void exitProcedure() {
        this.robotStateContext.unsubscribe(this);
        System.exit(0);
    }

    private void setOnlineState(boolean isOnline) {
        boolean isOffline = !isOnline;

        // Distance
        this.tBtnDistanceOn.setDisable(isOffline);
        this.tBtnDistanceOff.setDisable(isOffline);

        // Follow line
        this.tBtnFollowLineOff.setDisable(isOffline);
        this.tBtnFollowLineOn.setDisable(isOffline);

        // Turn
        this.btnTurnRight.setDisable(isOffline);
        this.btnTurnLeft.setDisable(isOffline);
        this.txtTurnAngle.setDisable(isOffline);

        // Color
        this.tBtnColorOff.setDisable(isOffline);
        this.tBtnColorOn.setDisable(isOffline);

        // Angle
        this.tBtnAngleOff.setDisable(isOffline);
        this.tBtnAngleOn.setDisable(isOffline);
    }
}
