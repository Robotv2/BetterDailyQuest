package fr.robotv2.betterdailyquest.storage.dto;

import fr.robotv2.anchor.api.annotation.Column;
import fr.robotv2.anchor.api.annotation.Entity;
import fr.robotv2.anchor.api.annotation.Id;
import fr.robotv2.anchor.api.repository.Identifiable;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity("quest_players")
public class QuestPlayerDto implements Identifiable<UUID> {

    @Id
    @Column("player_id")
    private UUID playerUniqueId;

    @Column("player_name")
    private String playerName;

    @Column(value = "quest_done", blob = true)
    private Map<String, Integer> questDone;

    @ApiStatus.Internal
    public QuestPlayerDto() {

    }

    public QuestPlayerDto(QuestPlayer questPlayer) {
        this.playerUniqueId = questPlayer.getId();
        this.playerName = questPlayer.getPlayerName();
        this.questDone = new HashMap<>(questPlayer.getQuestDone());
    }

    public QuestPlayerDto(UUID playerUniqueId, String playerName, Map<String, Integer> questDone) {
        this.playerUniqueId = playerUniqueId;
        this.playerName = playerName;
        this.questDone = new HashMap<>(questDone);
    }

    @Override
    public UUID getId() {
        return playerUniqueId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Map<String, Integer> getQuestDone() {
        return questDone;
    }
}
