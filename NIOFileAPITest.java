package com.bridgelabz.javaIO;
import com.bridgelabz.javaIO.Java8WatchServiceExample;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class NIOFileAPITest {

	private static String HOME = System.getProperty("user.home");
	private static String PLAY_WITH_NIO = "TempPlayGround";

	@Test
	public void givenPathWhenCheckedThenConfirm() throws IOException {
		Path homePath = Paths.get(HOME);
		assertTrue(Files.exists(homePath));

		Path playPath = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		if (Files.exists(playPath)) {
			FileUtils.forceDelete(playPath.toFile());
		}
		assertTrue(Files.notExists(playPath));

		Files.createDirectories(playPath);
		assertTrue(Files.exists(playPath));

		IntStream.range(1, 10).forEach(counter -> {
			Path tempFile = Paths.get(playPath + "/tempFile" + counter);
			assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			} catch (IOException e) {
			}
			assertTrue(Files.exists(tempFile));
		});

		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		Files.newDirectoryStream(playPath).forEach(System.out::println);
		Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp"))
				.forEach(System.out::println);
	}
	
	@Test
	public void givenADirectoryWhenWatchedListAllTheActivities() throws IOException {
		Path dir = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
		new Java8WatchServiceExample(dir).processEvent();
	}
}