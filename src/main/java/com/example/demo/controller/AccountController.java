package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Account;
import com.example.demo.domain.AccountRepository;
import com.example.demo.rabbitmq.Receiver;
import com.example.demo.rabbitmq.Sender;

@RestController
@RequestMapping("/api")
public class AccountController {
	
	private final AccountRepository repository;
	
	public AccountController(AccountRepository repository){
		this.repository = repository;
	}
	
	@GetMapping("/account")
	public Iterable<Account> getAccounts() {
		Iterable<Account> account = repository.findAll();
		
		System.out.println("---- : sending AMQP messages");
		try { 
			Sender.send();
		}catch(Exception e) {
			System.err.println("*** :" +e);
		}
		
		return account;
	}
	
	@GetMapping("/savings")
	public String getSavings() {
		
		System.out.println("---- : receiver AMQP messages");
		try { 
			Receiver.recv();
		}catch(Exception e) {
			System.err.println("*** :" +e);
		}
		
		return "waiting for messages" ;
	}

}
