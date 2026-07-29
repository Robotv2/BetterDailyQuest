package fr.robotv2.betterdailyquest.configurations;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultResourcesTest {

    private static final Map<String, GroupExpectation> GROUPS = Map.of(
            "daily", new GroupExpectation("0 0 * * *", 5, 2, 11, List.of(10, 11, 12, 13, 14)),
            "weekly", new GroupExpectation("0 0 * * mon", 3, 1, 11, List.of(28, 29, 30)),
            "monthly", new GroupExpectation("0 0 1 * *", 1, 1, 10, List.of(40))
    );

    private static final Map<String, List<TaskExpectation>> QUESTS = Map.ofEntries(
            quest("daily-stonebreaker", task("BREAK", 32, "STONE", "COBBLESTONE")),
            quest("daily-woodcutter", task("BREAK", 24, "OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG")),
            quest("daily-builder", task("PLACE", 32, "COBBLESTONE", "OAK_PLANKS")),
            quest("daily-baker", task("CRAFT", 8, "BREAD")),
            quest("daily-cook", task("COOK", 16, "COOKED_BEEF", "COOKED_CHICKEN", "BAKED_POTATO")),
            quest("daily-monster-hunter", task("KILL", 12, "ZOMBIE", "SKELETON", "SPIDER")),
            quest("daily-archer", task("LAUNCH", 32, "ARROW")),
            quest("daily-angler", task("FISH_ITEM", 5, "*")),
            quest("daily-shepherd", task("SHEAR", 8)),
            quest("daily-dairy-farmer", task("MILK", 4)),
            quest("daily-traveler", task("WALK", 500)),
            quest("weekly-quarry-worker", task("BREAK", 512, "STONE", "COBBLESTONE")),
            quest("weekly-ore-prospector", task("BREAK", 96, "COAL_ORE", "IRON_ORE", "GOLD_ORE")),
            quest("weekly-site-builder", task("PLACE", 256, "COBBLESTONE", "OAK_PLANKS", "BRICKS")),
            quest("weekly-monster-slayer", task("KILL", 75, "ZOMBIE", "SKELETON", "SPIDER", "CREEPER")),
            quest("weekly-master-angler", task("FISH_ITEM", 32, "*")),
            quest("weekly-head-chef", task("COOK", 128, "COOKED_BEEF", "COOKED_CHICKEN", "BAKED_POTATO")),
            quest("weekly-artisan", task("CRAFT", 24, "CHEST", "FURNACE", "TORCH")),
            quest("weekly-enchanter", task("ENCHANT", 5, "*")),
            quest("weekly-animal-caretaker", task("SHEAR", 32), task("MILK", 16)),
            quest("weekly-wolf-tamer", task("TAME", 3, "WOLF")),
            quest("weekly-swimmer", task("SWIM", 1000)),
            quest("monthly-master-miner",
                    task("BREAK", 2000, "STONE", "COBBLESTONE"),
                    task("BREAK", 256, "COAL_ORE", "IRON_ORE", "GOLD_ORE"),
                    task("BREAK", 16, "DIAMOND_ORE")),
            quest("monthly-grand-builder",
                    task("PLACE", 1024, "STONE", "COBBLESTONE", "BRICKS"),
                    task("PLACE", 512, "OAK_PLANKS", "BIRCH_PLANKS", "SPRUCE_PLANKS", "JUNGLE_PLANKS"),
                    task("CRAFT", 64, "TORCH")),
            quest("monthly-legendary-hunter",
                    task("KILL", 250, "ZOMBIE", "SKELETON", "SPIDER"),
                    task("KILL", 100, "CREEPER"),
                    task("KILL", 25, "ENDERMAN")),
            quest("monthly-seasoned-angler", task("FISH_ITEM", 256, "*")),
            quest("monthly-master-chef",
                    task("COOK", 256, "*"),
                    task("CRAFT", 64, "BREAD"),
                    task("CONSUME", 64, "*")),
            quest("monthly-ranch-master",
                    task("SHEAR", 128),
                    task("MILK", 64),
                    task("TAME", 5, "WOLF")),
            quest("monthly-master-smith",
                    task("CRAFT", 16, "IRON_PICKAXE", "IRON_SWORD"),
                    task("ENCHANT", 10, "*")),
            quest("monthly-master-archer",
                    task("CRAFT", 16, "BOW"),
                    task("LAUNCH", 512, "ARROW")),
            quest("monthly-provisioner",
                    task("CRAFT", 64, "CHEST"),
                    task("CRAFT", 64, "FURNACE"),
                    task("CRAFT", 128, "TORCH")),
            quest("monthly-all-rounder",
                    task("BREAK", 512, "OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG"),
                    task("PLACE", 512, "OAK_PLANKS", "BIRCH_PLANKS", "SPRUCE_PLANKS", "JUNGLE_PLANKS"),
                    task("KILL", 100, "ZOMBIE", "SKELETON", "SPIDER", "CREEPER"))
    );

    private static final Set<String> SEQUENTIAL_QUESTS = Set.of(
            "monthly-master-miner",
            "monthly-grand-builder",
            "monthly-master-smith",
            "monthly-master-archer"
    );

    @Test
    void bundledBoardProvidesCapacityForAllThreeCadences() {
        YamlConfiguration config = resource("config.yml");

        assertEquals("&8Quest Board", config.getString("quest-board.title"));
        assertEquals(6, config.getInt("quest-board.rows"));
        assertEquals("GRAY_STAINED_GLASS_PANE", config.getString("quest-board.filler-item.material"));
        assertEquals(GROUPS.keySet(), section(config, "quest-board.groups").getKeys(false));
        GROUPS.forEach((groupId, expected) ->
                assertEquals(expected.slots(), config.getIntegerList("quest-board.groups." + groupId + ".slots")));
    }

    @Test
    void bundledGroupsUseRecurringAutomaticAssignments() {
        GROUPS.forEach((groupId, expected) -> {
            YamlConfiguration group = resource("groups/" + groupId + ".yml");

            assertTrue(group.getBoolean("options.repeatable"), groupId);
            assertFalse(group.getBoolean("options.sequential-tasks"), groupId);
            assertFalse(group.getBoolean("options.need-starting"), groupId);
            assertTrue(group.getBoolean("options.automatically-given"), groupId);
            assertEquals(expected.schedule(), group.getString("automatic-reset"), groupId);
            assertEquals(expected.assignmentLimit(), group.getInt("global-assignment-limit"), groupId);
            assertEquals(expected.maxRerolls(), group.getInt("max-rerolls"), groupId);
            assertFalse(group.isConfigurationSection("assignment-limits"), groupId);
        });
    }

    @Test
    void bundledQuestPoolsMatchTheApprovedCatalog() {
        Map<String, ConfigurationSection> actualQuests = new HashMap<>();
        GROUPS.keySet().forEach(groupId -> {
            ConfigurationSection quests = section(resource("quests/" + groupId + "-quests.yml"), "quests");
            assertEquals(GROUPS.get(groupId).questCount(), quests.getKeys(false).size(), groupId);
            quests.getKeys(false).forEach(questId -> {
                ConfigurationSection previous = actualQuests.put(
                        questId.toLowerCase(),
                        section(quests, questId)
                );
                assertNull(previous, "duplicate quest ID " + questId);
            });
        });

        assertEquals(QUESTS.keySet(), actualQuests.keySet());
        QUESTS.forEach((questId, expectedTasks) -> {
            ConfigurationSection quest = actualQuests.get(questId);
            assertEquals(questId.substring(0, questId.indexOf('-')), quest.getString("group"), questId);
            assertFalse(quest.getString("name", "").isBlank(), questId);
            assertEquals(1, quest.getStringList("description").size(), questId);
            assertEquals(
                    List.of("[message] &aQuest complete: &f%quest_name%"),
                    quest.getStringList("rewards"),
                    questId
            );
            assertEquals(SEQUENTIAL_QUESTS.contains(questId), quest.getBoolean("options.sequential-tasks"), questId);
            assertFalse(quest.isConfigurationSection("conditions"), questId);

            ConfigurationSection tasks = section(quest, "tasks");
            assertEquals(expectedTasks.size(), tasks.getKeys(false).size(), questId);
            for(int index = 0; index < expectedTasks.size(); index++) {
                TaskExpectation expected = expectedTasks.get(index);
                ConfigurationSection task = section(tasks, Integer.toString(index + 1));
                assertEquals(expected.type(), task.getString("task_type"), questId);
                assertEquals(expected.amount(), task.getInt("required_amount"), questId);
                assertEquals(expected.targets(), targets(task), questId);
                assertFalse(task.getString("task_description", "").isBlank(), questId);
                assertTrue(task.getStringList("task_rewards").isEmpty(), questId);
                assertFalse(task.isConfigurationSection("conditions"), questId);
            }
        });
    }

    private static Map.Entry<String, List<TaskExpectation>> quest(String id, TaskExpectation... tasks) {
        return Map.entry(id, List.of(tasks));
    }

    private static TaskExpectation task(String type, int amount, String... targets) {
        return new TaskExpectation(type, amount, List.of(targets));
    }

    private static List<String> targets(ConfigurationSection task) {
        if(task.isString("required_target")) {
            return List.of(task.getString("required_target"));
        }
        return new ArrayList<>(task.getStringList("required_targets"));
    }

    private static ConfigurationSection section(ConfigurationSection parent, String path) {
        ConfigurationSection section = parent.getConfigurationSection(path);
        assertNotNull(section, path);
        return section;
    }

    private static YamlConfiguration resource(String path) {
        InputStream stream = DefaultResourcesTest.class.getClassLoader().getResourceAsStream(path);
        assertNotNull(stream, path);
        return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    private record GroupExpectation(String schedule, int assignmentLimit, int maxRerolls, int questCount, List<Integer> slots) {
    }

    private record TaskExpectation(String type, int amount, List<String> targets) {
    }
}
