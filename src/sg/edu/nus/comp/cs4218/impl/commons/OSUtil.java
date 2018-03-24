package sg.edu.nus.comp.cs4218.impl.commons;

import java.io.File;
import java.util.Locale;

/**
 * Check the operating system that this program is running on.
 */
public final class OSUtil {
	public static final String NEWLINE = System.lineSeparator();
	public static final String SEP = File.separator;
	private static String operatingSystem = System.getProperty("os.name").toLowerCase(Locale.US);

	private OSUtil() {
	}

	/**
	 * Returns true if the operating system is Windows.
	 */
	public static boolean isWindows() {
		return (operatingSystem.indexOf("win") >= 0);
	}

	/**
	 * Returns true if the operating system is Mac.
	 */
	public static boolean isMac() {
		return (operatingSystem.indexOf("mac") >= 0);
	}

	/**
	 * Returns true if the operating system is Unix.
	 */
	public static boolean isUnix() {
		return (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0);
	}

	/**
	 * Returns true if the operating system is Solaris.
	 */
	public static boolean isSolaris() {
		return (operatingSystem.indexOf("sunos") >= 0);
	}
}
