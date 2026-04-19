package com.doctorapointment.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvLoader {
	private static final Logger log  = LoggerFactory.getLogger(EnvLoader.class);
	private static final Map<String, String> env = new HashMap<>();

	static {
		try {
			// load .env file
			Files.lines(Paths.get(".env"))
				.filter(line -> line.contains("="))
				.forEach(line -> {
					String[] parts = line.split("=", 2);
					env.put(parts[0].trim(), parts[1].trim());
				});

			log.info(".env file loaded successfully");
		}
		catch(IOException e) {
			log.error("Failed to load .env file",e);
		}
	}

	// get env variable
	public static String get(String key) {
		String value = env.get(key);
		if(value == null) {
			log.warn("Key '{}' is missing in the .env file", key);
		}
		return value;
	}
}
