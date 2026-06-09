package com.team7.db.model.log;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name="request_log")
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "ip")
    String ip;

    @Column(name = "username")
    String username;

    @Column(name = "url")
    String url;

    @Column(name = "method")
    String method;

    @Column(name = "status")
    String status;

}
