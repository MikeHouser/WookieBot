package shared;

/**
 * Created by Mike on 06.11.16.
 */
public class UserCommandHelper {
    public static UserCommand fromString(String value) {
        for (UserCommand cmd : UserCommand.values()) {
            if(cmd.name().equals(value)) {
                return cmd;
            }
        }
        return UserCommand.EMPTY;
    }
}
