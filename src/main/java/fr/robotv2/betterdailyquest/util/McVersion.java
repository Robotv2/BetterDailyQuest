package fr.robotv2.betterdailyquest.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class McVersion implements Comparable<McVersion> {

    private static final Pattern VERSION_PATTERN = Pattern.compile(
            "^(\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:[.-].*)?$"
    );
    private static final McVersion v1_17 = new McVersion(1, 17);

    private static class CurrentVersionHolder {
        private static final McVersion INSTANCE = parse(Bukkit.getBukkitVersion());
    }

    static McVersion parse(final String version) {
        final Matcher matcher = VERSION_PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Unsupported Minecraft version: " + version);
        }

        final int major = Integer.parseInt(matcher.group(1));
        final int minor = Integer.parseInt(matcher.group(2));
        final int patch = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
        return new McVersion(major, minor, patch);
    }

    private final int major;
    private final int minor;
    private final int patch;

    public McVersion(final int major, final int minor) {
        this(major, minor, 0);
    }

    public McVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Gets the currently running McVersion
     */
    public static McVersion current() {
        return CurrentVersionHolder.INSTANCE;
    }

    public boolean hasVersionInNmsPackageName() {
        return !this.isAtLeast(v1_17);
    }

    public boolean isAtLeast(final McVersion other) {
        return this.compareTo(other) >= 0;
    }

    @Override
    public int compareTo(@NotNull final McVersion other) {
        if (this.major > other.major) return 3;
        if (other.major > this.major) return -3;
        if (this.minor > other.minor) return 2;
        if (other.minor > this.minor) return -2;
        return Integer.compare(this.patch, other.patch);
    }

    /**
     * Gets the "major" part of this McVersion. For 1.16.5, this would be 1
     */
    public int getMajor() {
        return major;
    }

    /**
     * Gets the "minor" part of this McVersion. For 1.16.5, this would be 16
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets the "patch" part of this McVersion. For 1.16.5, this would be 5.
     */
    public int getPatch() {
        return patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final McVersion mcVersion = (McVersion) o;
        return major == mcVersion.major && minor == mcVersion.minor && patch == mcVersion.patch;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        if (patch == 0) {
            return major + "." + minor;
        } else {
            return major + "." + minor + "." + patch;
        }
    }

    public boolean isAtLeast(final int major, final int minor, final int patch) {
        return this.isAtLeast(new McVersion(major, minor, patch));
    }

    public boolean isAtLeast(final int major, final int minor) {
        return this.isAtLeast(new McVersion(major, minor));
    }

}
