package com.team7.service.entitiy;

import com.team7.db.model.entity.Mbti;
import com.team7.db.repository.card.MbtiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbtiService {
    private final MbtiRepository mbtiRepository;

    public Mbti findMbtiByMbtiValue(String mbtiValue){
        return mbtiRepository.findMbtiByValue(mbtiValue);
    }
}
