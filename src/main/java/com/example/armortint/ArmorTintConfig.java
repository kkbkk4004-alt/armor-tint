package com.example.armortint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/** Saved to .minecraft/config/armortint.json */
public class ArmorTintConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("armortint.json");

	public boolean enabled = true;
	/** Tinting starts when this fraction of the durability is left (0.5 = half). */
	public float startFraction = 0.5f;
	/** Blink when almost broken (10% left or less). */
	public boolean flash = true;
	/** How strong the color is, 0.0 (faint) to 1.0 (solid). */
	public float strength = 0.9f;

	private static ArmorTintConfig instance;

	public static ArmorTintConfig get() {
		if (instance == null) instance = load();
		return instance;
	}

	private static ArmorTintConfig load() {
		try {
			if (Files.exists(FILE)) {
				ArmorTintConfig c = GSON.fromJson(Files.readString(FILE), ArmorTintConfig.class);
				if (c != null) return c;
			}
		} catch (Exception e) {
			ArmorTintClient.LOGGER.warn("Could not read config, using defaults", e);
		}
		return new ArmorTintConfig();
	}

	public void save() {
		try {
			Files.createDirectories(FILE.getParent());
			Files.writeString(FILE, GSON.toJson(this));
		} catch (Exception e) {
			ArmorTintClient.LOGGER.warn("Could not save config", e);
		}
	}
}
