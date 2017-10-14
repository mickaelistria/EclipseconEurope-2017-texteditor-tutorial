package org.eclipsecon.editor;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.eclipse.core.runtime.Platform;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.lsp4e.server.StreamConnectionProvider;

public class JDTLSConnectionProvider extends ProcessStreamConnectionProvider implements StreamConnectionProvider {

	private static final String JDTLS_PATH = "/home/mistria/eclipseconeurope2017/tuto/jdt-language-server-0.6.0-201710162016";
	
	public JDTLSConnectionProvider() throws IOException {
		// See https://github.com/eclipse/eclipse.jdt.ls/blob/master/README.md#running-from-the-command-line
		super(
			Arrays.asList(new String[] { "java",
					//"-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,quiet=y,address=1044", // to enable debug
					"-Declipse.application=org.eclipse.jdt.ls.core.id1", "-Declipse.product=org.eclipse.jdt.ls.core.product",
					"-Dlog.protocol=true", "-Dlog.level=ALL", "-noverify", "-Xmx1G",
					"-jar", JDTLS_PATH + "/plugins/org.eclipse.equinox.launcher_1.4.0.v20161219-1356.jar",
					"-configuration", JDTLS_PATH + "/config_" + toConfigDir(Platform.getOS()),
					"-data", Files.createTempDirectory("jdtls").toFile().getAbsolutePath()
			}), System.getProperty("user.dir"));
	}

	private static String toConfigDir(String os) {
		if (Platform.OS_LINUX.equals(Platform.getOS())) {
			return "linux";
		} else if (Platform.OS_WIN32.equals(Platform.getOS())) {
			return "win";
		} else if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			return "mac";
		}
		return null;
	}

}
