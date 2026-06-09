package com.team7.db.repository.card;

import com.team7.db.model.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface BenefitRepository extends JpaRepository<Benefit, Long> {


    public ArrayList<Benefit> findBenefitByBenefitUid(Long benefituid);

    public ArrayList<Benefit> findBenefitsByBenefitOn(String benefitOn);

    public ArrayList<Benefit> findBenefitsByBenefitOnIn(ArrayList<String> benefitOns);

    public ArrayList<Benefit> findBenefitsByType(String type);

    public ArrayList<Benefit> findBenefitsByUnit(String unit);


}
