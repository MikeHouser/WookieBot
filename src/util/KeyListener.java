package util;

import shared.UserCommand;
import shared.UserCommandContainer;
import shared.UserCommandHelper;

import java.util.Scanner;

/**
 * Created by Mike on 24.07.16.
 */
public class KeyListener implements Runnable {

    private RobotRunner robotRunner;
    private boolean execute = false;

    public KeyListener(RobotRunner robotRunner) {
        this.robotRunner = robotRunner;
    }

    public void run() {
        this.execute = true;
        Scanner sc = new Scanner(System.in);
        while (execute) {
            String nextLine = sc.nextLine();
            System.out.println("Userinput: " + nextLine);

            if(nextLine.startsWith(":")) {
                // String handling -> nextLine -> e.g. :LEFT-90
                String command = nextLine.toUpperCase().substring(1).trim(); // remove :
                String argumentList = "";
                String[] parts = command.split("-");
                String commandType = parts[0];
                if (parts.length >= 2) {
                    argumentList = parts[1];
                }

                UserCommand userCommand = UserCommandHelper.fromString(commandType);
                UserCommandContainer cmdContainer = new UserCommandContainer();
                cmdContainer.userCommand = userCommand;
                switch (userCommand) {
                    case EXIT: {
                        execute = false;
                        this.robotRunner.stop();
                        break;
                    }
                    case LEFT:
                    case RIGHT: {
                        Integer argument = new Integer(argumentList);
                        cmdContainer.intArg = argument;
                        this.robotRunner.forwardCommand(cmdContainer);
                        break;
                    }
                    case LINE: {
                        this.robotRunner.forwardCommand(cmdContainer);
                        break;
                    }
                }
            }
        }

    }

    public void abort() {
        this.execute = false;
    }
}
