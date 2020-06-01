package com.epam.order.entity;

import com.epam.order.dto.ItemDto;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_entity_id", nullable = false)
  private OrderEntity orderEntity;
  private long itemId;
  private String itemName;
  private int amount;

  public ItemEntity(ItemDto dto) {
    this.itemName = dto.getItemName();
    this.amount = dto.getAmount();
  }

  public static List<ItemEntity> convert(List<ItemDto> itemDtos) {
    return itemDtos.stream().map(ItemEntity::new).collect(Collectors.toList());
  }
}
