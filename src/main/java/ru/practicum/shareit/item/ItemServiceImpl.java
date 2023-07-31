package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepository.getUserById(userId);
        Item item = ItemMapper.itemDtoToItem(itemDto);
        item.setOwner(user);
        log.info("Позиция с id = {} была создана", item.getId());
        return ItemMapper.itemToItemDto(itemRepository.createItem(item, user));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.getAllItems(userId)) {
            items.add(ItemMapper.itemToItemDto(item));
        }
        log.info("Получены все позиции");
        return items;
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.getItemById(itemId, userId).orElseThrow(() -> {
            log.info("Позиция с id = {} не существует", itemId);
            throw new NotFoundException("Позиция с id = " + itemId + "не существует");
        });
        log.info("Позиция с id = {} загружена", itemId);
        return ItemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long itemId, Long userId) {
        Item updatedItem = itemRepository.getItemById(itemId, userId).orElseThrow(() -> {
            log.info("Позиция с id = {} не существует", itemId);
            throw new NotFoundException("Позиция с id = " + itemId + "не существует");
        });
        if (!updatedItem.getOwner().getId().equals(userId)) {
            log.info("Позиция с id = {} не найдена", itemId);
            throw new NotFoundException("Позиция с id " + itemId + " не найдена");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        log.info("Позиция с id = {} успешно обновлена", itemId);
        return ItemMapper.itemToItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItemByText(String text, Long userId) {
        if (text.isBlank() || userId == null) {
            return List.of();
        }
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.searchItemByText(text, userId)) {
            items.add(ItemMapper.itemToItemDto(item));
        }
        log.info("Позиция найдена");
        return items;
    }

    @Override
    public ItemDto removeItemById(Long itemId, Long userId) {
        userRepository.getUserById(userId);
        Optional<Item> optionalItem = itemRepository.getItemById(itemId, userId);

        if (!optionalItem.get().getOwner().getId().equals(userId)) {
            log.info("У пользователя с id ={} не найдена позиция с id = {}", userId, itemId);
            throw new NotFoundException("У пользователя с id = " + userId + " не найдена позиция с id = " + itemId);
        }
        if (optionalItem.isEmpty()) {
            log.info("Позиция с id = {} не найдена", itemId);
            throw new NotFoundException("Позиция с id " + itemId + " не найдена");
        }
        log.info("Позиция с id = {} удалена", itemId);
        optionalItem = itemRepository.removeItemById(itemId, userId);
        return ItemMapper.itemToItemDto(optionalItem.get());
    }
}
