package com.meeting.model;

import javax.persistence.*;

@Entity
@Table(name = "room_organ")
public class RoomOrgan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomOrganGenerator")
    @SequenceGenerator(name = "roomOrganGenerator", sequenceName = "roomOrgan_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer roomId;

    @Column(nullable = false)
    private Integer organId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrganId() {
        return organId;
    }

    public void setOrganId(Integer organId) {
        this.organId = organId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
