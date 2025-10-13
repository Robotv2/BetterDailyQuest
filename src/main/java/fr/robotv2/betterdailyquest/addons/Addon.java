package fr.robotv2.betterdailyquest.addons;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Abstract class representing an addon for the BetterDailyQuest plugin.
 * Addons can extend this class and override lifecycle methods to implement custom behavior.
 */
public abstract class Addon {

    private FileConfiguration config = null;
    private File configFile = null;
    private ClassLoader loader;

    public void onLoad() {}

    public void onPreEnable() {}

    public void onPostEnable() {}

    public void onDisable() {}

    public void onReload() {}

    /**
     * Get the name of the addon.
     * This method should be overridden by the addon implementation to return the correct name.
     *
     * @return The name of the addon.
     */
    public abstract String getName();

    /**
     * Get the version of the addon.
     * This method should be overridden by the addon implementation to return the correct version.
     *
     * @return The version string of the addon.
     */
    public abstract String getVersion();

    public BetterDailyQuest getPlugin() {
        return BetterDailyQuest.instance();
    }

    /**
     * Register a command to the plugin's command handler.
     * The command object should have methods annotated with @Command.
     *
     * @param command The command object to register.
     */
    protected void registerCommand(Object command) {
        getPlugin().getCommandHandler().register(command);
    }

    /**
     * Register a Bukkit event listener.
     *
     * @param listener The listener to register.
     */
    protected void registerListener(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
    }

    /**
     * Get the folder where the addon files are stored.
     * The folder is located at "plugins/BetterDailyQuest/addons/{addonName}".
     *
     * @return The addon folder as a File object.
     */
    public File getFolder() {
        return getPlugin().getRelativeFile("addons/" + getName());
    }

    /**
     * Get the name of the configuration file for the addon.
     * The file is named "{addonName}-config.yml".
     *
     * @return The configuration file name.
     */
    public String getConfigFileName() {
        return getName().toLowerCase() + "-config.yml";
    }

    /**
     * Get the configuration file for the addon.
     *
     * @return The configuration file as a File object.
     */
    public File getConfigFile() {
        return new File(getFolder(), getConfigFileName());
    }

    /**
     * Save the default configuration file for the addon if it does not already exist.
     * The default configuration is loaded from the addon JAR resources.
     */
    public void saveDefaultConfig() {
        if(getConfigFile().exists()) {
            return;
        }

        try (InputStream inputStream = this.getClass().getResourceAsStream("/" + getConfigFileName())) {
            if (inputStream != null) {
                Files.copy(inputStream, getConfigFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                getPlugin().getLogger().warning("Could not save default config for addon " + getName() + ": config.yml not found in resources.");
            }
        } catch (IOException exception) {
            getPlugin().getLogger().severe("Could not save default config for addon " + getName() + ": " + exception.getMessage());
        }
    }

    /**
     * Reload the configuration file for the addon.
     * If the configuration file does not exist, it will be created with default values.
     * Default values are loaded from the addon JAR resources.
     */
    public void reloadConfig() {
        if (configFile == null) {
            configFile = getConfigFile();
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = this.getClass().getResourceAsStream("/" + getConfigFileName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    /**
     * Get the configuration for the addon.
     * If the configuration has not been loaded yet, it will be loaded from the file.
     *
     * @return The FileConfiguration object representing the addon's configuration.
     */
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /** Get the class loader used by the addon.
     *
     * @return The class loader.
     */
    @ApiStatus.Internal
    public ClassLoader getLoader() {
        return loader;
    }

    /** Set the class loader for the addon.
     *
     * @param loader The class loader to set.
     */
    @ApiStatus.Internal
    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }
}
