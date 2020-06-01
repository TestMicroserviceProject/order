package com.epam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

  private long itemId;

  private String itemName;

  private int amount;

  private double price;
}
