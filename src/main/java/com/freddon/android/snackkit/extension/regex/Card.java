package com.freddon.android.snackkit.extension.regex;

/**
 * Created by fred on 2018/5/17.
 */

public class Card {

    private BankInfo bankInfo;
    private String cardNO;
    private String cardType;

    public Card() {
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
