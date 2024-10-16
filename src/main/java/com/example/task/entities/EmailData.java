package com.example.task.entities;

import javax.persistence.*;import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "EMAIL_DATA")
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String email;

    public EmailData() {}

}