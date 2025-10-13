package fr.robotv2.betterdailyquest.addons;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class AddonManager {

    private final BetterDailyQuest plugin;
    private final File addonFolder;
    private final Set<Addon> addons;

    public AddonManager(BetterDailyQuest plugin, File addonFolder) {
        this.plugin = plugin;
        this.addonFolder = addonFolder;
        this.addons = new HashSet<>();
    }

    public File getAddonFolder() {
        return addonFolder;
    }

    public Set<Addon> getAddons() {
        return addons;
    }

    public void load() {
        if(!addonFolder.exists()) {
            return;
        }

        BetterDailyQuest.logger().info("Loading addons from: " + addonFolder.getAbsolutePath());

        final File[] files = addonFolder.listFiles();
        if(files == null) {
            BetterDailyQuest.logger().warning("Failed to list files in addon folder.");
            return;
        }

        for(File file : files) {
            if(!file.getName().endsWith(".jar")) {
                continue;
            }

            try {
                final ClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, plugin.getClass().getClassLoader());
                final ScanResult result = getClassGraph(loader).scan();

                // find addon class
                final List<Class<Addon>> addonClasses = result.getSubclasses(Addon.class.getName()).loadClasses(Addon.class);
                if(addonClasses.isEmpty()) {
                    BetterDailyQuest.logger().warning("Failed to find addon class in file: " + file.getName());
                    continue;
                }

                if(addonClasses.size() > 1) {
                    BetterDailyQuest.logger().warning("Found multiple addon classes in file: " + file.getName());
                    continue;
                }

                final Class<? extends Addon> addonClass = addonClasses.get(0);
                final Addon addon = addonClass.newInstance();
                addon.setLoader(loader);

                addons.add(addon);
                BetterDailyQuest.logger().info("Addon " + addon.getName() + " (" + addon.getVersion() + ") has been loaded successfully.");
                result.close();

            } catch (IOException | InstantiationException | IllegalAccessException exception) {
                BetterDailyQuest.logger().log(Level.SEVERE, "Failed to load addon from file: " + file.getName(), exception);
            }
        }
    }

    private ClassGraph getClassGraph(ClassLoader loader) throws IOException {
        return new ClassGraph().overrideClassLoaders(loader).enableAllInfo();
    }
}
