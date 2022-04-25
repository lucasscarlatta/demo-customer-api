package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.exception.OverdrawException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExecuteTransactionTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private ExecuteTransaction executeTransaction;

    @Test
    public void deposit() {
        var account = new Account();
        account.setBalance(BigDecimal.TEN);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doNothing().when(session).saveOrUpdate(any());
        when(session.save(any())).thenReturn(null);
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        executeTransaction.deposit(account, new BigDecimal("100"));

        verify(sessionFactory, times(1)).openSession();
        verify(session, times(1)).beginTransaction();
        verify(session, times(1)).saveOrUpdate(any());
        verify(session, times(1)).save(any());
        verify(session, times(1)).getTransaction();
        verify(transaction, times(1)).commit();
    }

    @Test
    public void withdraw() {
        var account = new Account();
        account.setBalance(BigDecimal.TEN);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doNothing().when(session).saveOrUpdate(any());
        when(session.save(any())).thenReturn(null);
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        executeTransaction.withdraw(account, BigDecimal.ONE);

        verify(sessionFactory, times(1)).openSession();
        verify(session, times(1)).beginTransaction();
        verify(session, times(1)).saveOrUpdate(any());
        verify(session, times(1)).save(any());
        verify(session, times(1)).getTransaction();
        verify(transaction, times(1)).commit();
    }

    @Test
    public void withdrawError() {
        var account = new Account();
        account.setBalance(BigDecimal.TEN);

        var thrown = assertThrows(OverdrawException.class, () -> executeTransaction.withdraw(account, new BigDecimal("100")));

        assertEquals("Overdraw not allowed", thrown.getMessage());

        verify(sessionFactory, never()).openSession();
        verify(session, never()).beginTransaction();
        verify(session, never()).saveOrUpdate(any());
        verify(session, never()).save(any());
        verify(session, never()).getTransaction();
        verify(transaction, never()).commit();
    }
}