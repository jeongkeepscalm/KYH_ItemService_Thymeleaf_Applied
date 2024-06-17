package hello.itemservice.domain.item.basic;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }

  @PostConstruct
  public void init() {
    /**
     * 테스트용 데이터 추가
     */
    itemRepository.save(new Item("testA", 10000, 10));
    itemRepository.save(new Item("testB", 20000, 20));
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "basic/addForm";
  }

  // @PostMapping("/add")
  public String addItemV1(
          @RequestParam String itemName
          , @RequestParam int price
          , @RequestParam Integer quantity
          , Model model) {
    Item item = new Item();
    item.setItemName(itemName);
    item.setPrice(price);
    item.setQuantity(quantity);
    itemRepository.save(item);
    model.addAttribute("item", item);
    return "basic/item";
  }

  // @PostMapping("/add")
  public String addItemV2(@ModelAttribute("item") Item item, Model model) {
    itemRepository.save(item);
    //model.addAttribute("item", item); //자동 추가, 생략 가능
    return "basic/item";
    /**
     * @ModelAttribute("hello") Item item:
     *    model 에 hello 라는 이름으로 item 을 set 해준다.( == model.addAttribute("hello", item) )
     */
  }

  // @PostMapping("/add")
  public String addItemV3(@ModelAttribute Item item) {
    itemRepository.save(item);
    return "basic/item";
  }

  // @PostMapping("/add")
  public String addItemV5(Item item) {
    itemRepository.save(item);
    return "redirect:/basic/items/" + item.getId();
  }

  @PostMapping("/add")
  public String addItemV6(Item item, RedirectAttributes redirectAttributes) {

    log.info("item open = {}", item.getOpen());
    /*
     * checked: true, unchecked: false
     * 체크박스 체크시 html form 에서 open=on 이라는 값으로 넘어오고
     * 스프링은 on 이라는 문자를 true 타입으로 변환(스프링 타입 컨버터가 수행)
     * 체크박스 선택하지 않고 폼 전송시, open 이라는 피드 자체가 서버로 전송되지 않는다.
     */

    log.info("item.regions={}", item.getRegions());

    log.info("item.itemType={}", item.getItemType());

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/basic/items/{itemId}";
    /**
     * RedirectAttributes
     *    URL 인코딩, PathVariable, 쿼리 파라미터까지 처리해준다.
     */

  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/basic/items/{itemId}";
  }

  @ModelAttribute("regionsA")
  public Map<String, String> regions() {
    LinkedHashMap<String, String> regionsB = new LinkedHashMap<>();
    regionsB.put("SEOUL", "서울");
    regionsB.put("BUSAN", "부산");
    regionsB.put("JEJU", "제주");
    return regionsB;
  }
  /*
   * 메소드 단위의 @ModelAttribute("regions")
   *   해당 컨트롤러 내 메소드 호출 시 해당 데이터가 담겨져 있다.
   *   model.addAttribute("regions", regionsB);
   * */

  @ModelAttribute("itemTypes")
  public ItemType[] itemTypes() {
    return ItemType.values(); // [BOOK, FOOD, ETC]
  }

  @ModelAttribute("deliveryCodes")
  public List<DeliveryCode> deliveryCodes() {
    List<DeliveryCode> deliveryCodes = new ArrayList<>();
    deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
    deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
    deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
    return deliveryCodes;
  }

}
