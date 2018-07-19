package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Component;
import pl.pelipe.shoppinglist.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
class ItemListFactory {

    List<ItemListEntity> createSampleLists(UserEntity userEntity) {
        ItemListEntity sampleList1 = new ItemListEntity("Grocery", userEntity);
        ItemListEntity sampleList2 = new ItemListEntity("Cosmetics", userEntity);
        ItemListEntity sampleList3 = new ItemListEntity("Household chemicals", userEntity);
        ItemListEntity sampleList4 = new ItemListEntity("Wish list", userEntity);
        List<ItemListEntity> itemListEntityList = new ArrayList<>();
        itemListEntityList.add(sampleList1);
        itemListEntityList.add(sampleList2);
        itemListEntityList.add(sampleList3);
        itemListEntityList.add(sampleList4);
        return itemListEntityList;
    }
}
