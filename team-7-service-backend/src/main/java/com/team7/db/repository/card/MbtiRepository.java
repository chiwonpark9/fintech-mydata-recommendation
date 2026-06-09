package com.team7.db.repository.card;

import com.team7.db.model.entity.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MbtiRepository extends JpaRepository<Mbti, Long> {

    public Mbti findMbtiByValue(String value);

    public Mbti findMbtiByMbtiUid(Long Uid);
}
