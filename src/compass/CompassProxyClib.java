package compass;
public class CompassProxyClib implements ICompassProxy {

	private final static String LIBPATH_LINUX = "/home/pi/Documents/Wookiebot/hmc5883l_lib/src/";
	private final static String LIBNAME_LINUX = "hmc5883l_lib.so";

	private final static String LIBPATH_OSX = "/Users/Mike/Documents/IdeaProjects/WookieBot/src/clib/";
	private final static String LIBNAME_OSX = "libhmc5883l_lib.dylib";

	static {
		String osName = System.getProperty("os.name");
		if(osName.startsWith("Mac")) {
			System.load(LIBPATH_OSX + LIBNAME_OSX);
		} else {
			System.load(LIBPATH_LINUX + LIBNAME_LINUX);
		}
    }
	
	public native CompassResult getCompassData(float xOffset, float yOffset);
   
}
