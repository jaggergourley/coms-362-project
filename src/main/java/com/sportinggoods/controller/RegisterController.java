package com.sportinggoods.controller;

import com.sportinggoods.model.Register;

public class RegisterController {
    private Register register;

    public RegisterController(Register register) {
        this.register = register;
    }

    public boolean processPayment(double amount, String paymentMethod) {
        return register.processPayment(amount, paymentMethod);
    }

    public void issueRefund(double amount) {
        register.issueRefund(amount);
    }
}
