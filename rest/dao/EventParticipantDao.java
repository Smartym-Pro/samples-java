package pro.smartum.reptracker.gateway.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pro.smartum.reptracker.gateway.dao.entities.EventParticipantEntity;

import java.util.List;

/**
 * @author Sergey Valuy
 * 
 */
public interface EventParticipantDao extends BaseDao<EventParticipantEntity, Long> {

    @Nullable
    EventParticipantEntity findEventParticipant(Long trackingAccountId, Long externalUserId);

    @Nullable
    EventParticipantEntity findEventParticipantByExternalUser(long externalUserId);

    @NotNull
    List<EventParticipantEntity> findEventParticipantByTrackingAccount(long trackingAccountId);
}
