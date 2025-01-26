package com.luanr.agregadorinvestimentos.mapper;


import com.luanr.agregadorinvestimentos.dto.requests.CreateAccountDto;
import com.luanr.agregadorinvestimentos.dto.responses.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.responses.BillingAddressResponseDto;
import com.luanr.agregadorinvestimentos.entity.Account;
import com.luanr.agregadorinvestimentos.entity.BillingAddress;
import com.luanr.agregadorinvestimentos.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AccountMapper {
    public AccountResponseDto toResponseDto(Account account) {
        return new AccountResponseDto(
                account.getAccount_id().toString(),
                account.getDescription(),
                toBillingAddressDto(account.getBillingAddress())
        );
    }

    private BillingAddressResponseDto toBillingAddressDto(BillingAddress address) {
        return new BillingAddressResponseDto(
                address.getStreet(),
                address.getNumber()
        );
    }

    public Account toEntity(CreateAccountDto createAccountDto, User user) {
        var account = new Account(
                null,
                createAccountDto.description(),
                user,
                null,
                new ArrayList<>()
        );
        var billingAddressEntity = new BillingAddress(
                account.getAccount_id(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );
        account.setBillingAddress(billingAddressEntity);
        return account;
    }
}
