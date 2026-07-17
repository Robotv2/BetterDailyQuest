package fr.robotv2.betterdailyquest.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class McVersionTest {

    @Test
    void parsesLegacyBukkitVersion() {
        assertEquals(new McVersion(1, 8, 8), McVersion.parse("1.8.8-R0.1-SNAPSHOT"));
    }

    @Test
    void parsesModernPaperApiVersion() {
        assertEquals(new McVersion(26, 2), McVersion.parse("26.2.build.60-beta"));
    }
}
