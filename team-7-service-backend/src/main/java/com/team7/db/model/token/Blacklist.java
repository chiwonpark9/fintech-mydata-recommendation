package com.team7.db.model.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="blacklist")
@AllArgsConstructor
@NoArgsConstructor
public class Blacklist {
    @Id
    @Column(name="token", unique = true)
    public String token;
}
