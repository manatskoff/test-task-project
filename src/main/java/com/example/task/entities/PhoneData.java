package com.example.task.entities;

import javax.persistence.*;import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "PHONE_DATA")
public class PhoneData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String phone;

    public PhoneData() {}

}