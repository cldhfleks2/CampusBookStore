package com.campusbookstore.app.purchase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PurchaseDTO {
    private Long wishId;
    private Long quantity;
}
