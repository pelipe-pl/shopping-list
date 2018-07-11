package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.user.UserRepository;

import java.util.List;

@Service
public class ItemListService {
    private final ItemListRepository itemListRepository;
    private final UserRepository userRepository;

    public ItemListService(ItemListRepository itemListRepository, UserRepository userRepository) {
        this.itemListRepository = itemListRepository;
        this.userRepository = userRepository;
    }

    public ItemListEntity getById(Long id) {
        return itemListRepository.getOne(id);
    }

    public void add(ItemListDto itemListDto){
        ItemListEntity itemListEntity = new ItemListEntity();
        itemListEntity.setName(itemListDto.getName());
        itemListEntity.setUser(userRepository.getById(itemListDto.getUserId()));
        itemListEntity.setRemoved(false);
        itemListRepository.save(itemListEntity);
    }

    public void remove(Long id){
        ItemListEntity itemListEntity = itemListRepository.getOne(id);
        itemListEntity.setRemoved(true);
        itemListRepository.save(itemListEntity);
    }

    public List<ItemListEntity> findAllByUserIdAndRemovedFalse(Long id) {
        return itemListRepository.findAllByUser_IdAndRemovedFalse(id);
    }
}
