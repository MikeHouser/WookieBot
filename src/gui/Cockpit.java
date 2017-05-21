package gui;

import config.RobotConfig;
import statemachine.IStatemachineObserver;
import statemachine.MockupRobotStateContext;
import statemachine.RobotStateContext;
import statemachine.StatemachineEventArgs;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Cockpit extends JFrame implements IStatemachineObserver {

    private JPanel panelMain;
    private JButton btnStart;
    private JButton btnStop;
    private JLabel lblAngle;
    private JButton turnLeftButton;
    private JTextField txtTurnLeftAngle;
    private JButton btnRight;
    private JTextField txtTurnRightAngle;
    private JLabel lblState;
    private JLabel lblColor;
    private JButton btnFollowLine;
    private JButton btnStartListenDistance;
    private JButton btnStopListenDistance;
    private JButton btnStartListenColor;
    private JButton btnStopListenColor;
    private JButton btnStartListenCompass;
    private JButton btnStopListenCompass;
    private JLabel lblDistance;
    private JButton btnFollowLineStop;

    private RobotStateContext robotStateContext;
    private GuiHelper guiHelper;

    public Cockpit() {
        super("Wookiebot");

        setContentPane(this.panelMain);

        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });

        pack();
        setVisible(true);

        // Add click listener
        btnStart.addActionListener(e -> this.guiHelper.startRobot());
        btnStop.addActionListener(e -> this.guiHelper.stopRobot());
        turnLeftButton.addActionListener(e -> {
            int degrees = Integer.parseInt(this.txtTurnLeftAngle.getText());
            this.guiHelper.turnLeft(degrees);
        });
        btnRight.addActionListener(e -> {
            int degrees = Integer.parseInt(this.txtTurnRightAngle.getText());
            this.guiHelper.turnRight(degrees);
        });
        btnFollowLine.addActionListener(e -> {
            this.guiHelper.startFollowLine();
        });
        btnFollowLineStop.addActionListener(e -> {
            this.guiHelper.stopFollowLine();
        });
        btnStopListenDistance.addActionListener(e -> {
            this.guiHelper.stopListenDistane();
        });
        btnStartListenDistance.addActionListener(e -> {
            this.guiHelper.startListenDistance();
        });
        btnStartListenColor.addActionListener(e -> {
            this.guiHelper.startListenColor();
        });
        btnStopListenColor.addActionListener(e -> {
            this.guiHelper.stopListenColor();
        });
        btnStartListenCompass.addActionListener(e -> {
            this.guiHelper.startListenCompass();
        });
        btnStopListenCompass.addActionListener(e -> {
            this.guiHelper.stopListenCompass();
        });

        if (RobotConfig.getUseMockupStateContext()) {
            this.robotStateContext = new MockupRobotStateContext();
        } else {
            this.robotStateContext = new RobotStateContext();
        }
        this.guiHelper = new GuiHelper(this.robotStateContext);
        this.robotStateContext.subscribe(this);
    }

    @Override
    public void handleStatemachineEvent(StatemachineEventArgs args) {
        if (args.angleChanged) {
            this.lblAngle.setText(args.angle + "°");
        } else if (args.stateNameChanged) {
            this.lblState.setText(args.stateName);
        } else if (args.colorNameChanged) {
            this.lblColor.setText(args.colorName);
        } else if (args.distanceChanged) {
            this.lblDistance.setText(args.distance + " cm");
        }
    }

    public void exitProcedure() {
        this.robotStateContext.unsubscribe(this);
        dispose();
        System.exit(0);
    }
}
