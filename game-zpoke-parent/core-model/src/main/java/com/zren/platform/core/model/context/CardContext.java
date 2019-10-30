package com.zren.platform.core.model.context;

import com.zren.platform.core.model.enums.CardFirstEnum;
import lombok.Data;

import java.util.List;

/**
 * Card Context
 *
 * @author k.y
 * @version Id: CardContext.java, v 0.1 2019年10月15日 下午18:46 k.y Exp $
 */
@Data
public class CardContext {

    private List<int[]> cardList;
    private int[] commonOneCard;
    private int[] commonTwoCard;
    private int[] commonThreeCard;
    private int[] montageCard;

    public int[] getMontageCard() {
        montageCard=new int[this.commonOneCard.length+this.commonTwoCard.length+this.commonThreeCard.length];
        System.arraycopy(this.commonOneCard,0,this.montageCard,0,this.commonOneCard.length);
        System.arraycopy(this.commonTwoCard,0,this.montageCard,this.commonOneCard.length,this.commonTwoCard.length);
        System.arraycopy(this.commonThreeCard,0,this.montageCard,this.commonOneCard.length+this.commonTwoCard.length,this.commonThreeCard.length);
        return this.montageCard;
    }

    @Data
    public class CardEntity {

        private CardFirstEnum cardFirstEnum;
        private int[] firstCard;
    }
}