package com.g2forge.alexandria.java.io.watch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.concurrent.AThreadActor;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileScanner extends AThreadActor {
	@RequiredArgsConstructor
	@Getter
	protected static class Event {
		protected final Set<Path> paths;

		protected final boolean scan;
	}

	protected final IConsumer1<Set<Path>> handler;

	protected final boolean reportOnScan;

	protected final Set<Path> directories;

	protected final LinkedHashSet<Event> queue = new LinkedHashSet<>();

	/** A map of the directories we have scanned, so that we can watch recursively, to their watcher controls. */
	protected final Map<Path, ICloseable> scanned = new HashMap<>();

	public FileScanner(IConsumer1<Set<Path>> handler, boolean reportOnScan, Path... directories) {
		this(handler, reportOnScan, HCollection.asSet(directories));
	}

	@Override
	public FileScanner open() {
		return (FileScanner) super.open();
	}

	@Override
	protected void run() {
		// A watcher to help us watch for filesystem changes
		try (final FileWatcher watcher = new FileWatcher()) {
			watcher.open();
			for (Path directory : directories) {
				scan(directory, watcher);
			}

			while (isOpen()) {
				final List<Event> events;
				// Grab the next changed file at the source that we should handle
				synchronized (queue) {
					if (queue.isEmpty()) try {
						queue.wait();
					} catch (InterruptedException exception) {
						continue;
					}

					events = new ArrayList<>(queue);
					queue.clear();
					if (events.isEmpty()) continue;
				}

				// Scan anything we found
				final LinkedHashSet<Path> allPaths = events.stream().map(Event::getPaths).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
				for (Path path : allPaths) {
					if (Files.isDirectory(path)) {
						scan(path, watcher);
					}
				}

				// Handle any changes
				final LinkedHashSet<Path> reportPaths = reportOnScan ? allPaths : events.stream().filter(e -> !e.isScan()).map(Event::getPaths).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
				if (!reportPaths.isEmpty()) handler.accept(reportPaths);
			}
		}
	}

	protected void scan(Path directory, FileWatcher watcher) {
		synchronized (scanned) {
			if (scanned.containsKey(directory)) return;

			// Watch the directory
			final ICloseable watch = watcher.watch(directory, (event, path) -> {
				synchronized (queue) {
					// Stop watching a directory if it's deleted
					if (scanned.containsKey(path) && !Files.isDirectory(path)) {
						scanned.remove(path).close();
					}
					queue.add(new Event(HCollection.asSet(path), false));
					queue.notifyAll();
				}
			}, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

			scanned.put(directory, watch);
		}

		// Scan the directory so that we don't miss any changes from before we knew there was directory here
		synchronized (queue) {
			try {
				final Set<Path> children = Files.list(directory).collect(Collectors.toCollection(LinkedHashSet::new));
				queue.add(new Event(children, true));
				queue.notifyAll();
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
		}
	}
}
