package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);}

	@Bean
	public CommandLineRunner initData(ClientRepository ClienteNuevo, AccountRepository NuevaCuenta, TransactionRepository NuevaTransaccion, LoanRepository NuevoPrestamo, ClientLoanRepository Nuevoclientloan,CardRepository newcard) {
		return (args) -> {
			Client cliente1 = new Client("Melba", "Lorenzo" , "TuTurritaloka@gmail.com",passwordEncoder.encode("123"));
			Client cliente2 = new Client("Roberto", "Nuñez" ,"RobertoNuñez@gmail.com",passwordEncoder.encode("123"));

			Account cuenta1 = new Account("VIN001", LocalDate.now(),5000.00,AccountType.CURRENT);
			Account cuenta2 = new Account("VIN002", LocalDate.now(), 7500.00,AccountType.SAVING);
			Account cuenta3 = new Account("VIN003", LocalDate.now(),200.00,AccountType.CURRENT);
			Account cuenta4 = new Account("VIN004", LocalDate.now(),5000.00,AccountType.SAVING);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT,200.00,"Transferencia de Roberto Nuñez",LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,5500.00,"Pagaste EDENOR",LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,27600.00,"Transferencia de Francisco Alvarez",LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,276000.00, "Transferencia de Ernesto Flores",LocalDateTime.now());

			Loan payment1 = new Loan("Mortgage", 500000.00 , Arrays.asList(12,24,36,48,60));
			Loan payment2 = new Loan("Personal", 100000.00 , Arrays.asList(6,12,24));
			Loan payment3 = new Loan("Car", 300000.00 , Arrays.asList(6,12,24,36));

			ClientLoan clientLoan1 = new ClientLoan(400000.00,payment1.getPayments().get(4),cliente1,payment1);
			ClientLoan clientLoan2 = new ClientLoan(50000.00, payment2.getPayments().get(1),cliente1,payment2);
			ClientLoan clientLoan3 = new ClientLoan(100000.00, payment2.getPayments().get(2),cliente2,payment2);
			ClientLoan clientLoan4 = new ClientLoan(200000.00, payment3.getPayments().get(3),cliente2,payment3);

			Card card1 = new Card(cliente1,CardType.DEBIT,CardColor.GOLD,"5489 4264 2412 0703",656,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card2 = new Card(cliente1,CardType.CREDIT,CardColor.TITANIUM,"5509 8437 3455 3371",711,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card3 = new Card(cliente2,CardType.CREDIT,CardColor.SILVER,"5169 4288 1581 4628",357,LocalDateTime.now(),LocalDateTime.now().plusYears(5));

			cliente1.addAccount(cuenta1);
			cliente1.addAccount(cuenta2);
			cliente1.addAccount(cuenta3);
			cliente2.addAccount(cuenta4);

			cuenta1.addTransaction(transaction1);
			cuenta2.addTransaction(transaction2);
			cuenta3.addTransaction(transaction3);
			cuenta4.addTransaction(transaction4);

			cliente1.addCard(card1);
			cliente1.addCard(card2);
			cliente2.addCard(card3);

			ClienteNuevo.save(cliente1);
			ClienteNuevo.save(cliente2);

			NuevaCuenta.save(cuenta1);
			NuevaCuenta.save(cuenta2);
			NuevaCuenta.save(cuenta3);
			NuevaCuenta.save(cuenta4);

			NuevaTransaccion.save(transaction1);
			NuevaTransaccion.save(transaction2);
			NuevaTransaccion.save(transaction3);
			NuevaTransaccion.save(transaction4);

			NuevoPrestamo.save(payment1);
			NuevoPrestamo.save(payment2);
			NuevoPrestamo.save(payment3);
			Nuevoclientloan.save(clientLoan1);
			Nuevoclientloan.save(clientLoan2);
			Nuevoclientloan.save(clientLoan3);
			Nuevoclientloan.save(clientLoan4);

			newcard.save(card1);
			newcard.save(card2);
			newcard.save(card3);


		};
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
}
