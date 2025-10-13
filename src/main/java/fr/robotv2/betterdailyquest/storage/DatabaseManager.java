package fr.robotv2.betterdailyquest.storage;

import fr.robotv2.anchor.api.repository.async.AsyncQueryableRepository;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.storage.dto.ActiveQuestDto;
import fr.robotv2.betterdailyquest.storage.dto.ActiveTaskDto;
import fr.robotv2.betterdailyquest.storage.dto.QuestPlayerDto;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DatabaseManager {

    void init();

    void close();

    CompletableFuture<QuestPlayer> composePlayer(Player player, boolean shouldCache);

    CompletableFuture<Void> savePlayer(Player player, boolean removeFromCache);

    CompletableFuture<Void> savePlayers(Player player, boolean removeFromCache);

    @NotNull
    AsyncQueryableRepository<UUID, QuestPlayerDto> getQuestPlayerRepository();

    @NotNull
    AsyncQueryableRepository<UUID, ActiveQuestDto> getActiveQuestRepository();

    @NotNull
    AsyncQueryableRepository<UUID, ActiveTaskDto> getActiveTaskRepository();

    @Nullable
    QuestPlayer getCachedQuestPlayer(Player player);

    @Nullable
    QuestPlayer getCachedQuestPlayer(UUID playerId);

    @UnmodifiableView
    Collection<QuestPlayer> getCachedPlayers();

    CompletableFuture<Void> removeQuests(QuestGroup group);

    CompletableFuture<Void> removeQuests(QuestPlayer player);

    CompletableFuture<Void> removeQuests(QuestPlayer player, QuestGroup group);

    CompletableFuture<Void> removeQuestsIfEnded(QuestPlayer player);

    CompletableFuture<Void> removeQuestsAndTasks(ActiveQuest quest);
}
