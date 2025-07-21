package com.luanr.agregadorinvestimentos.repository;

import com.luanr.agregadorinvestimentos.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a " +
           "LEFT JOIN FETCH a.accountStocks ast " +
           "LEFT JOIN FETCH ast.stock " +
           "WHERE a.account_id = :accountId")
    Optional<Account> findByIdWithStocks(@Param("accountId") UUID accountId);

    @Query("SELECT a FROM Account a " +
           "LEFT JOIN FETCH a.billingAddress " +
           "WHERE a.user.user_id = :userId")
    List<Account> findAllByUserIdWithBillingAddress(@Param("userId") UUID userId);
}
