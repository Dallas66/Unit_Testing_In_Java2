package edu.epam.izhevsk.junit;


import org.junit.Before;

import static org.mockito.AdditionalMatchers.*;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;




public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;
    @Mock
    private DepositService depositService = mock(DepositService.class);
    @Mock
    private AccountService accountService = mock(AccountService.class);
    private long id = 100L;
    private long wrongId = 51L;
    private long amount = 50L;
    private long wrongAmount = 101L;


    @Before
    public void init() throws InsufficientFundsException {
        MockitoAnnotations.initMocks(this);

        when(accountService.isUserAuthenticated(id)).thenReturn(true);
        when(depositService.deposit(amount,id)).thenReturn("successful");
        when(depositService.deposit(gt(100L), eq(100L))).thenThrow(new InsufficientFundsException());

    }

    @Test
    public void validUser(){
        assertTrue(accountService.isUserAuthenticated(id));
    }

    @Test
    public void notValidUser(){
        assertFalse(accountService.isUserAuthenticated(wrongId));
    }

    @Test(expected = InsufficientFundsException.class)
    public void paymentControllerDepositTest() throws InsufficientFundsException {
        paymentController.deposit(wrongAmount,id);
    }

    @Test(expected = SecurityException.class)
    public void paymentControllerWrongIdTest() throws InsufficientFundsException {
        paymentController.deposit(amount, wrongId);
    }


}
