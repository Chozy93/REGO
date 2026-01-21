package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_notification_settings")
@Getter
@Setter
@NoArgsConstructor
public class NotificationSettings {

    @Id
    @Column(name = "user_id")
    private Long userId; 

    @OneToOne
    @MapsId 
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "chat_noti")
    private boolean chatNoti = true;

    @Column(name = "activity_noti")
    private boolean activityNoti = true;

    @Column(name = "marketing_noti")
    private boolean marketingNoti = false;
}