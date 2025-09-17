package com.seon.agora.chat.entity;

import com.seon.agora.chat.dto.SenderType;
import com.seon.agora.room.entity.Room;
import com.seon.agora.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 8.
 */
@Entity
@Table(name = "chat")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat {
    @Id
    @Column(name= "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false, length = 2000)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User sender;

    @Column(name = "send_dttm", nullable = false)
    private LocalDateTime sendDttm;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType;

    @PrePersist
    public void prePersist() {
        this.sendDttm = LocalDateTime.now();
    }
}
