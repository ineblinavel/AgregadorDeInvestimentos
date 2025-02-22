package com.luanr.agregadorinvestimentos.repository;

import com.luanr.agregadorinvestimentos.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, String> {
}
