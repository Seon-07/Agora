package com.seon.moca.room.entity;

import com.seon.moca.room.dto.RoomStatus;
import com.seon.moca.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-17
 */
@Entity
@Table(name = "rooms")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Room {
    @Id
    @Column(name= "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "topic")
    private String topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
    private User pro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
    private User con;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Column(name = "create_dttm", nullable = false)
    private LocalDateTime createDttm;

    @Column(name = "start_dttm", nullable = false)
    private LocalDateTime startDttm;

    @Column(name = "end_dttm", nullable = false)
    private LocalDateTime endDttm;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @PrePersist
    protected void onCreate() {
        this.createDttm = LocalDateTime.now();
        this.status = RoomStatus.WAITING;
    }

    public void assignPro(User user) {
        this.pro = user;
    }

    public void assignCon(User user) {
        this.con = user;
    }
}
