package com.seon.agora.room.repository;

import com.seon.agora.room.dto.RoomStatus;
import com.seon.agora.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByStatusAndDelYn(RoomStatus status, boolean delYn);
}
