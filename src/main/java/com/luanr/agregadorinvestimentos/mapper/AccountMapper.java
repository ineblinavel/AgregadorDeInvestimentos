package com.luanr.agregadorinvestimentos.mapper;


import com.luanr.agregadorinvestimentos.dto.responses.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.responses.BillingAddressResponseDto;
import com.luanr.agregadorinvestimentos.entity.Account;
import com.luanr.agregadorinvestimentos.entity.BillingAddress;
import org.springframework.stereotype.Component;

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
}
