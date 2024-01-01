package com.moneymonitoring.moneymonitoring.utils;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

	protected Status status;

    public ApplicationException(Status status) {
        super(status.getResponseDesc());
        this.status = status;
    }

    public ApplicationException(Status status, Throwable cause) {
        super(status.getResponseDesc(), cause);
        this.status = status;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    
}
