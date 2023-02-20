package com.example.awsshop.repository;

import com.example.awsshop.model.UserVirtualPaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserVirtualPaymentAccountRepository extends JpaRepository<UserVirtualPaymentAccount, Long> {

}
