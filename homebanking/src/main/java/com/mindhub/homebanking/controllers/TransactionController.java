package com.mindhub.homebanking.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.FilteredTransactionsDTO;
import com.mindhub.homebanking.dtos.PaymentApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    CardService cardService;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> makeTransactions(
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String numberAccountOrigin, @RequestParam String numberAccountReceive, Authentication authentication
    ) {
        Account accountOrigin = accountService.getClientByNumber(numberAccountOrigin);
        Account accountReceive = accountService.getClientByNumber(numberAccountReceive);
        Client clienteautenticado = clientService.findClientByEmail(authentication.getName());
        if (amount <= 0 || description.isEmpty() || numberAccountOrigin.isEmpty() || numberAccountReceive.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (!accountReceive.isAccountActive() || !accountOrigin.isAccountActive()) {
            return new ResponseEntity<>("The accounts you are trying to transfer to are disabled.", HttpStatus.FORBIDDEN);
        }
        if (numberAccountOrigin.equals(numberAccountReceive)) {
            return new ResponseEntity<>("A transaction cannot be made to the same account.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin == null) {
            return new ResponseEntity<>("The account from which you are trying to make the transaction does not exist.", HttpStatus.FORBIDDEN);
        }
        if (!clienteautenticado.getAccounts().contains(accountOrigin)) {
            return new ResponseEntity<>("This account is not owned by you.", HttpStatus.FORBIDDEN);
        }
        if (accountReceive == null) {
            return new ResponseEntity<>("The account you are trying to transfer to does not exist.", HttpStatus.FORBIDDEN);
        }
        if (amount > accountOrigin.getBalance()) {
            return new ResponseEntity<>("You do not have enough balance to carry out the transaction.", HttpStatus.FORBIDDEN);
        }
        Transaction transactionDebit = new Transaction(accountOrigin, DEBIT, -amount, description, LocalDateTime.now());
        transactionService.saveTransactions(transactionDebit);
        Transaction transactionCredit = new Transaction(accountReceive, CREDIT, amount, description, LocalDateTime.now());
        transactionService.saveTransactions(transactionCredit);

        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountReceive.setBalance(accountReceive.getBalance() + amount);
        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountReceive);
        return new ResponseEntity<>("Transaccion realizada con exito.", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/transactions/payment")
    public ResponseEntity<Object> paymentApp(@RequestBody PaymentApplicationDTO paymentApplicationDTO,
                                             Authentication authentication) {
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.getClientByNumber(paymentApplicationDTO.getNumber());
        Card card = cardService.getCardByNumber(paymentApplicationDTO.getCardNumber());
        if (!card.isCardActive()) {
            return new ResponseEntity<>("you card is disable", HttpStatus.FORBIDDEN);
        }
        if (card.getThruDate().isAfter(LocalDateTime.now())) {
            return new ResponseEntity<>("This card is expired", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("This card is not yours", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() < paymentApplicationDTO.getAmount()) {
            return new ResponseEntity<>("Account without balance", HttpStatus.FORBIDDEN);
        }
        if (!account.isAccountActive()) {
            return new ResponseEntity<>("The account to which the transaction is being made is disabled", HttpStatus.FORBIDDEN);
        }
        if (!card.getCvv().equals(paymentApplicationDTO.getCardCvv())) {
            return new ResponseEntity<>("CVV Invalid", HttpStatus.FORBIDDEN);
        }
        Transaction transactionPayment = new Transaction(account, TransactionType.DEBIT, - paymentApplicationDTO.getAmount(), paymentApplicationDTO.getDescription(), LocalDateTime.now());
        transactionService.saveTransactions(transactionPayment);
        account.setBalance(account.getBalance() - paymentApplicationDTO.getAmount());
        accountService.saveAccount(account);
        return new ResponseEntity<>("Successful payment", HttpStatus.CREATED);
    }

    @PostMapping("/api/transactions/filtered")
    public ResponseEntity<Object> getFilteredTransaction(
            @RequestBody FilteredTransactionsDTO filteredTransactionsDTO, Authentication authentication) throws DocumentException, FileNotFoundException {
        Client client = clientService.findClientByEmail((authentication.getName()));
        Account account = accountService.getClientByNumber(filteredTransactionsDTO.getAccountNumber());
        if (filteredTransactionsDTO.accountNumber.isEmpty() || filteredTransactionsDTO.fromDate == null || filteredTransactionsDTO.toDate == null) {
            return new ResponseEntity<>("Please fill all the form fields.", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("this account is not yours", HttpStatus.FORBIDDEN);
        }
        if (account.getTransactions() == null) {
            return new ResponseEntity<>("You don't have any transactions in this account.", HttpStatus.FORBIDDEN);
        }
        if (!account.isAccountActive()) {
            return new ResponseEntity<>("This account doesn't exist.", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = transactionService.filterTransactionWithDate(filteredTransactionsDTO.fromDate, filteredTransactionsDTO.toDate, account);

        createTable(transactions);

        return new ResponseEntity<>("Done! Check your desktop to find the document with that data.",HttpStatus.CREATED);
    }

    public void createTable(Set<Transaction> transactionArray) throws FileNotFoundException, DocumentException {
        var document = new Document();
        String route = System.getProperty("user.home");
        PdfWriter.getInstance(document, new FileOutputStream(route + "/Desktop/Transaction_Report.pdf"));

        document.open();

        var bold = new Font(Font.FontFamily.HELVETICA,18,Font.BOLD);
        var paragraph = new Paragraph("Transactions that occurred between the selected dates:");

        var table = new PdfPTable(4);
        Stream.of("Amount","Description","Date","Type").forEach(table::addCell);

        transactionArray
                .forEach(transaction -> {
                    table.addCell(("$"+transaction.getAmount()));
                    table.addCell(transaction.getDescription());
                    table.addCell(transaction.getDate().toString());
                    table.addCell(transaction.getType().toString());

                });
        paragraph.add(table);
        document.add(paragraph);
        document.close();
    }
}
