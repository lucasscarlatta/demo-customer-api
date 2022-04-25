package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.Transaction;
import com.assignment.demo.domain.enums.TransactionType;
import com.assignment.demo.exception.OverdrawException;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExecuteTransaction {

    private final SessionFactory sessionFactory;

    public synchronized void deposit(Account account, BigDecimal deposit) {
        var newBalance = account.getBalance().add(deposit);
        account.setBalance(newBalance);
        createTransaction(account, TransactionType.DEPOSIT, deposit);
    }

    public synchronized void withdraw(Account account, BigDecimal withdraw) {
        var newBalance = account.getBalance().subtract(withdraw);
        account.setBalance(newBalance);
        if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
            // TODO for this example you can not overdraw
            throw new OverdrawException("Overdraw not allowed");
        }
        createTransaction(account, TransactionType.WITHDRAW, withdraw);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public synchronized void createTransaction(Account account, TransactionType type, BigDecimal amount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);

        var session = sessionFactory.openSession();
        session.beginTransaction();

        session.saveOrUpdate(account);
        session.save(transaction);

        session.getTransaction().commit();

        session.close();
    }
}
