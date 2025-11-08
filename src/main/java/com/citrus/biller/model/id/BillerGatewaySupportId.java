package com.citrus.biller.model.id;

import java.io.Serializable;

import lombok.Data;

@Data
public class BillerGatewaySupportId implements Serializable {
    private String billerId;
    private String gatewayId;
} 