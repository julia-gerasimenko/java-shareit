package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

@Transactional
@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplIntegrationTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private ItemDto itemDto;
    private ItemShortDto itemShortDto;
    private ItemDto savedItem;
    private UserDto userOwner;
    private UserDto userRequestor;
    private UserDto savedRequestor;
    private ItemRequestDto savedRequest;

    @BeforeEach
    void beforeEach() {
        userOwner = UserDto.builder()
                .name("User_Owner_Name")
                .email("userowner@test.testz")
                .build();
        userRequestor = UserDto.builder()
                .name("User_Requestor_Name")
                .email("userRequestor@test.testz")
                .build();
        itemDto = ItemDto.builder()
                .name("Item")
                .description("Item description")
                .available(true)
                .owner(new ItemDto.Owner(userOwner.getId(), userOwner.getName(), userOwner.getEmail()))
                .build();
        itemShortDto = ItemShortDto.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .requestId(itemDto.getRequestId())
                .available(itemDto.getAvailable())
                .build();
    }
}