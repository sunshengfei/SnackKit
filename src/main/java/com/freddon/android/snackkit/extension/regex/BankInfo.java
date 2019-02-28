package com.freddon.android.snackkit.extension.regex;

import java.util.List;

/**
 * Created by fred on 2018/5/17.
 */

public class BankInfo {

    private String   bankName;//中国邮政储蓄银行",
    private String   bankCode;// "PSBC",
    private List<BankPattern> patterns;

    public BankInfo() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public List<BankPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<BankPattern> patterns) {
        this.patterns = patterns;
    }
}
