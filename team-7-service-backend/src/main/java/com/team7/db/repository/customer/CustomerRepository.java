package com.team7.db.repository.customer;

import com.team7.db.model.entity.User;
import com.team7.db.model.entity.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByUid(Long uid);

    public Optional<User> findUserByPhone(String phone);

    public Optional<User> findUserByEmail(String email);


    public Optional<User> findUserByAccountIdAndAccountPassword(String accountId, String accountPassword);

    public ArrayList<User> findAllByAccountPassword(String accountPassword);

    public ArrayList<User> findAllByMbti(Mbti mbti);

    public ArrayList<User> findAllByMbtiValue(String mbtiValue);

    public Optional<User> findUserByAccountId(String accountId);

    public ArrayList<User> findAllByRole(String role);



    public void deleteUserByAccountId(String accountId);
    public void deleteUserByUid(Long uid);
}
