package com.epam.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

  private Long orderId;

  private Long customerId;

  private List<ItemDto> itemDtos;

  private String location;

  private Double total;
}
