package com.mscommunication.productapi.modules.product.sales.dto;

import com.mscommunication.productapi.modules.product.sales.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesConfirmationDTO {
    private String salesId;
    private SalesStatus status;
    private String transactionid;
}
