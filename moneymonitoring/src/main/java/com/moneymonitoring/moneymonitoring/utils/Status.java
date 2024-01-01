package com.moneymonitoring.moneymonitoring.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private int responseCode;
    private String responseDesc;

    public static Status success() {
        return new Status(200, "SUCCESS");
    }
}

