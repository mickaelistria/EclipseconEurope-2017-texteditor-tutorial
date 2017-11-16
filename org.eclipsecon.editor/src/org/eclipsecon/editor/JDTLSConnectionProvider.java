package org.eclipsecon.editor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.core.runtime.Platform;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.lsp4e.server.StreamConnectionProvider;

public class JDTLSConnectionProvider extends ProcessStreamConnectionProvider implements StreamConnectionProvider {

	private static final String JDTLS_PATH = "/home/mistria/Downloads/jdt-language-server-0.9.0-201711151401";

	public JDTLSConnectionProvider() throws IOException {
		// See https://github.com/eclipse/eclipse.jdt.ls/blob/master/README.md#running-from-the-command-line
		super(
			Arrays.asList(new String[] { "java",
					"-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,quiet=y,address=1044", // to enable debug
					"-Declipse.application=org.eclipse.jdt.ls.core.id1", "-Declipse.product=org.eclipse.jdt.ls.core.product",
					"-Dlog.protocol=true", "-Dlog.level=ALL", "-noverify", "-Xmx1G",
					"-jar", findLauncherPath(),
					"-configuration", JDTLS_PATH + "/config_" + toConfigDir(Platform.getOS()),
					"-data", Files.createTempDirectory("jdtls").toFile().getAbsolutePath()
			}), System.getProperty("user.dir"));
	}

	private static String findLauncherPath() {
		File pluginsDir = new File(JDTLS_PATH, "plugins");
		return pluginsDir.listFiles((dir, name) -> name.startsWith("org.eclipse.equinox.launcher_"))[0].getAbsolutePath();
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

	@Override public Object getInitializationOptions(URI rootUri) {
		return Collections.singletonMap("workspaceFolders", Collections.singletonList(rootUri.toString()));
	}

}
