package br.com.fiap.payment;

import java.util.Date;

public class Card {
    private final String holder_name;
    private final String holder_document;
    private final String card_number;
    private final String cvv;
    private final Date valid_date;

    public Card(String holder_name, String holder_document, String card_number, String cvv, Date valid_date) {
        this.holder_name = holder_name;
        this.holder_document = holder_document;
        this.card_number = card_number;
        this.cvv = cvv;
        this.valid_date = valid_date;
    }

    public String getHolderName() {
        return holder_name;
    }

    public String getHolderDocument() {
        return holder_document;
    }

    public String getCardNumber() {
        return card_number;
    }

    public String getCvv() {
        return cvv;
    }

    public Date getValidDate() {
        return valid_date;
    }
}
