package gui;

import main.Wookiebot;
import statemachine.IStatemachineObserver;
import statemachine.RobotStateContext;
import statemachine.StatemachineEventArgs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private Wookiebot wookiebot;
    private RobotStateContext robotStateContext;

    public Cockpit() {
        super("Wookiebot");

        this.wookiebot = new Wookiebot();

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
        btnStart.addActionListener(e -> this.wookiebot.start());
        btnStop.addActionListener(e -> this.wookiebot.stop());
        turnLeftButton.addActionListener(e -> {
            int degrees = Integer.parseInt(this.txtTurnLeftAngle.getText());
            this.wookiebot.turnLeft(degrees);
        });
        btnRight.addActionListener(e -> {
            int degrees = Integer.parseInt(this.txtTurnRightAngle.getText());
            this.wookiebot.turnRight(degrees);
        });
        btnFollowLine.addActionListener(e -> {
            this.wookiebot.followLine();
        });
        btnStopListenDistance.addActionListener(e -> {
            this.wookiebot.stopListenDistane();
        });
        btnStartListenDistance.addActionListener(e -> {
            this.wookiebot.startListenDistance();
        });
        btnStartListenColor.addActionListener(e -> {
            this.wookiebot.startListenColor();
        });
        btnStopListenColor.addActionListener(e -> {
            this.wookiebot.stopListenColor();
        });
        btnStartListenCompass.addActionListener(e -> {
            this.wookiebot.startListenCompass();
        });
        btnStopListenCompass.addActionListener(e -> {
            this.wookiebot.stopListenCompass();
        });

        this.robotStateContext = this.wookiebot.getRobotStateContext();
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
