package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService service;
    public static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Позиция создана пользователем с id = {} ", userId);
        return service.createItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Позиция с id = {} {} {} ", itemId, " создана пользователем с id = ", userId);
        return service.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("получены все позиции пользователя с id = {}", userId);
        return service.getAllItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestBody ItemDto itemDto,
                                  @PathVariable Long itemId,
                                  @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("позиция с id = {} {} {} ", itemId, " обновлена пользователем с id = ", userId);
        return service.updateItemById(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text,
                                          @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Найдена позиция по тексту = {} {} {} ", text, " пользователем с id = ", userId);
        return service.searchItemByText(text.toLowerCase(), userId);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItemById(@PathVariable Long itemId,
                                  @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("удалена позиция с id = {} {} {} ", itemId, " пользователем с id = ", userId);
        return service.removeItemById(itemId, userId);
    }
}
