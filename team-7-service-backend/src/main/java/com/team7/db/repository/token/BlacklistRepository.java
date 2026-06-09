package com.team7.db.repository.token;

import com.team7.db.model.token.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {
    public Optional<Blacklist> findBlacklistByToken(String token);

}
