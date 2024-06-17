package hello.itemservice.domain.item;

import lombok.Data;

import java.util.List;

@Data
public class Item {

  private Long id;
  private String itemName;
  private Integer price;
  private Integer quantity;

  private Boolean open;         // 판매 여부: single checkbox
  private List<String> regions; // 등록 지역: multi checkbox
  private ItemType itemType;    // 상품 종류: radio button
  private String deliveryCode;  // 배송 방식: select box

  public Item() {}

  public Item(String itemName, Integer price, Integer quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }

}