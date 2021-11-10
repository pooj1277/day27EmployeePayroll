package com.bridgelabz.javaIO;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Java8WatchServiceExample {

	private static final Kind<?> ENTRY_CREATE = null;
	private static final Kind<?> ENTRY_DELETE = null;
	private static final Kind<?> ENTRY_MODIFY = null;
	private final WatchService watcher;
	private final Map<WatchKey, Path> dirWatcher;

	public Java8WatchServiceExample(Path dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.dirWatcher = new HashMap<>();
		scanAndRegisterDirectories(dir);
	}

	private void registerDirWatcher(Path dir) throws IOException {

		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

		dirWatcher.put(key, dir);
	}

	private void scanAndRegisterDirectories(final Path start) throws IOException {
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirWatcher(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public void processEvent() {
		while (true) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException e) {
				return;
			}
			Path dir = dirWatcher.get(key);
			if (dir == null) {
				continue;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);
				System.out.format("%s: %s\n", event.kind().name(), child);

				if (kind == ENTRY_CREATE) {
					try {
						if (Files.isDirectory(child)) {
							scanAndRegisterDirectories(child);
						}
					} catch (IOException e) {
					}
				} else if (kind.equals(ENTRY_DELETE)) {
					if (Files.isDirectory(child)) {
						dirWatcher.remove(key);
					}
				}
			}

			boolean valid = key.reset();
			if (!valid) {
				dirWatcher.remove(key);
				if (dirWatcher.isEmpty()) {
					break;
				}
			}
		}
	}
}