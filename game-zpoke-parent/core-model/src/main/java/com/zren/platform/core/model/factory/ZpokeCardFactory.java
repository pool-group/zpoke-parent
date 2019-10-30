package com.zren.platform.core.model.factory;

import com.assembly.common.util.DataUtil;
import com.assembly.common.util.LogUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zren.platform.core.model.context.CardContext;
import com.zren.platform.core.model.enums.CardFirstEnum;
import com.zren.platform.core.model.enums.CardMouldEnum;
import com.zren.platform.core.model.tuple.CardTuple;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * Card Factory
 *
 * @author k.y
 * @version Id: ZpokeCardFactory.java, v 0.1 2019年10月15日 下午17:41 k.y Exp $
 */
public class ZpokeCardFactory {

    private static final Integer ONE_ROUND = 13;

    public static void create(int minIndex, int maxIndex, int sheets, CardContext cardContext){

        List<Integer> list= Lists.newArrayList(CardTuple.CHESS_CARD);
        list.removeAll(CardTuple.KING_CARD);
        Integer[] commonIndex= DataUtil.randomNumber(minIndex, maxIndex-1, 5);
        List<Integer> pList= Lists.newArrayList(list.get(commonIndex[0]),list.get(commonIndex[1]),list.get(commonIndex[2]),list.get(commonIndex[3]),list.get(commonIndex[4]));
        list.removeAll(pList);
        Integer[] pIndex= DataUtil.randomNumber(0, 5, 3);
        Integer c=pList.get(pIndex[0]),e=pList.get(pIndex[1]),f=pList.get(pIndex[2]);
        cardContext.setCommonOneCard(new int[]{c,e,f});
        pList.remove(c);
        pList.remove(e);
        pList.remove(f);
        Integer[] sIndex= DataUtil.randomNumber(0, 2, 1);
        Integer g=pList.get(sIndex[0].intValue());
        cardContext.setCommonTwoCard(new int[]{g});
        pList.remove(g);
        cardContext.setCommonThreeCard(new int[]{pList.get(0)});
        pList.remove(pList.get(0));

        List<int[]> rList=Lists.newArrayList();
        for(int i=0;i<sheets;i++){
            Integer[] totalIndex= DataUtil.randomNumber(0, list.size(), 2);
            Integer a=list.get(totalIndex[0].intValue()),b=list.get(totalIndex[1].intValue());
            rList.add(new int[]{a,b});
            list.remove(a);
            list.remove(b);
        }
        int[] montageCard=cardContext.getMontageCard();
        List<CardBO> resultList=Lists.newArrayList();
        findAllWeight(rList,montageCard,resultList);

        sort(resultList);
        List<int[]> cardList=Lists.newArrayList();
        cardList.addAll(resultList.stream().map(CardBO::getCard).collect(Collectors.toList()));
        cardContext.setCardList(cardList);
    }

    private boolean compare(int[] o, int[] v, int[] montageCard){

        return true;
    }

    public static void findAllWeight(List<int[]> original, int[] montageCard, List<CardBO> resultList){
        List<int[]> allValue=Lists.newArrayList();
        DataUtil.exec(Arrays.stream(montageCard).boxed().collect(Collectors.toList()),"",0,3,allValue);
        original.stream().forEach(s->{
            findOneWeight(s,montageCard,resultList,allValue);
        });
    }

    public static void findOneWeight(int[] o, int[] montageCard, List<CardBO> resultList, List<int[]> allValue){
        if(null==allValue){
            allValue=Lists.newArrayList();
            DataUtil.exec(Arrays.stream(montageCard).boxed().collect(Collectors.toList()),"",0,3,allValue);
        }
        List<CardBO> processList=Lists.newArrayList();
        for(int[] value:allValue){
            CardMouldBO cardMouldBO=new CardMouldBO(o);
            CardBO cardBO=new CardBO(o);
            cardMouldBO.getTarget().addAll(Arrays.stream(value).boxed().collect(Collectors.toList()));
            Collections.sort(cardMouldBO.getTarget());
            int i1 = cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 1) - cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 2);
            int i2 = cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 2) - cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 3);
            int i3 = cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 3) - cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 4);
            int i4 = cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 4) - cardMouldBO.getTarget().get(cardMouldBO.getTarget().size() - 5);
            boolean bool = i1 == i2 && i2 == i3 && i3 == i4 && i1 == 1;
            if(CardTuple.SPADES_CARD.containsAll(cardMouldBO.getTarget())
                    ||CardTuple.RED_PEACH_CARD.containsAll(cardMouldBO.getTarget())
                    ||CardTuple.PLUM_CARD.containsAll(cardMouldBO.getTarget())
                    ||CardTuple.BLOCK_CARD.containsAll(cardMouldBO.getTarget())){
                if(i1==i2&&i2==i3&&i3==i4&&i1==1&&cardMouldBO.getTarget().get(cardMouldBO.getTarget().size()-1)%13==1){
                    cardBO.setWeight(CardMouldEnum.MAX_SAME_COLOR_LINK.getWeight());
                    cardBO.setDesc(CardMouldEnum.MAX_SAME_COLOR_LINK.getDesc());
                    cardBO.setOptimalCardType(CardMouldEnum.MAX_SAME_COLOR_LINK);
                }else if(bool){
                    cardBO.setWeight(CardMouldEnum.SAME_COLOR_LINK.getWeight());
                    cardBO.setDesc(CardMouldEnum.SAME_COLOR_LINK.getDesc());
                    cardBO.setOptimalCardType(CardMouldEnum.SAME_COLOR_LINK);
                }else {
                    cardBO.setWeight(CardMouldEnum.SAME_COLOR_UNLINK.getWeight());
                    cardBO.setDesc(CardMouldEnum.SAME_COLOR_UNLINK.getDesc());
                    cardBO.setOptimalCardType(CardMouldEnum.SAME_COLOR_UNLINK);
                }
                cardBO.mergeIntactCard(value);
                cardBO.setRealCard(findRealCard(transform(cardBO.getIntactCard())));
                cardBO.setSingleTupleValue(transform(cardBO.getIntactCard()));
                cardBO.setVsLevel(cardBO.getSingleTupleValue());
                processList.add(cardBO);
                continue;
            }
            for(Integer target:cardMouldBO.getTarget()){
                cardMouldBO.getCountMap().put(genOriginalValue(target),null==cardMouldBO.getCountMap().get(genOriginalValue(target))?1:cardMouldBO.getCountMap().get(genOriginalValue(target))+1);
                System.out.println();
            }
            for(Integer key:cardMouldBO.getCountMap().keySet()){
                cardMouldBO.getCountTarget().add(cardMouldBO.getCountMap().get(key));
            }
            Collections.sort(cardMouldBO.getCountTarget());
            int max=cardMouldBO.getCountTarget().get(cardMouldBO.getCountTarget().size()-1);
            if(max==4){
                cardBO.setWeight(CardMouldEnum.FOUR_SAME_VALUE.getWeight());
                cardBO.setDesc(CardMouldEnum.FOUR_SAME_VALUE.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.FOUR_SAME_VALUE);
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    int count=cardMouldBO.getCountMap().get(key);
                    if(4==count){
                        cardBO.setOtherTupleValue(key);
                    }else {
                        cardBO.setSingleTupleValue(new int[]{key});
                    }
                }
                cardBO.setVsLevel(new int[]{cardBO.getSingleTupleValue()[0],cardBO.getOtherTupleValue()});
            }else if(max==3&&cardMouldBO.getCountTarget().size()==2){
                cardBO.setWeight(CardMouldEnum.THREE_TWO_SAME_VALUE.getWeight());
                cardBO.setDesc(CardMouldEnum.THREE_TWO_SAME_VALUE.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.THREE_TWO_SAME_VALUE);
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    int count=cardMouldBO.getCountMap().get(key);
                    if(3==count){
                        cardBO.setOtherTupleValue(key);
                    }else {
                        cardBO.setPairTupleValue(new int[]{key});
                    }
                }
                cardBO.setVsLevel(new int[]{cardBO.getPairTupleValue()[0],cardBO.getOtherTupleValue()});
            }else if (max==3&&cardMouldBO.getCountTarget().size()==3){
                cardBO.setWeight(CardMouldEnum.THREE_SAME_VALUE.getWeight());
                cardBO.setDesc(CardMouldEnum.THREE_SAME_VALUE.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.THREE_SAME_VALUE);
                int[] v=new int[cardMouldBO.getCountMap().size()-1];
                int inx=0;
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    int count=cardMouldBO.getCountMap().get(key);
                    if(3==count){
                        cardBO.setOtherTupleValue(key);
                    }else {
                        v[inx]=key;
                        inx++;
                    }
                }
                cardBO.setSingleTupleValue(transform(v));
                cardBO.setVsLevel(new int[]{cardBO.getSingleTupleValue()[0],cardBO.getSingleTupleValue()[1],cardBO.getOtherTupleValue()});
            }else if(max==2&&cardMouldBO.getCountTarget().size()==3){
                cardBO.setWeight(CardMouldEnum.TWO_SAME_VALUE.getWeight());
                cardBO.setDesc(CardMouldEnum.TWO_SAME_VALUE.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.TWO_SAME_VALUE);
                int[] v=new int[cardMouldBO.getCountMap().size()-1];
                int inx=0;
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    int count=cardMouldBO.getCountMap().get(key);
                    if(2==count){
                        v[inx]=key;
                        inx++;
                    }else {
                        cardBO.setSingleTupleValue(new int[]{key});
                    }
                }
                cardBO.setPairTupleValue(v);
                cardBO.setVsLevel(new int[]{cardBO.getSingleTupleValue()[0],cardBO.getPairTupleValue()[0],cardBO.getPairTupleValue()[1]});
            }else if(max==2&&cardMouldBO.getCountTarget().size()==4){
                cardBO.setWeight(CardMouldEnum.ONE_SAME_VALUE.getWeight());
                cardBO.setDesc(CardMouldEnum.ONE_SAME_VALUE.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.ONE_SAME_VALUE);
                int[] v=new int[cardMouldBO.getCountMap().size()-1];
                int inx=0;
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    int count=cardMouldBO.getCountMap().get(key);
                    if(2==count){
                        cardBO.setPairTupleValue(new int[]{key});
                    }else {
                        v[inx]=key;
                        inx++;
                    }
                }
                cardBO.setSingleTupleValue(v);
                cardBO.setVsLevel(new int[]{cardBO.getSingleTupleValue()[0],cardBO.getSingleTupleValue()[1],cardBO.getSingleTupleValue()[2],cardBO.getPairTupleValue()[0]});
            }else if(max==1&&cardMouldBO.getCountTarget().size()==5){
                cardBO.setWeight(CardMouldEnum.SINGLE_VALUE.getWeight());
                cardBO.setDesc(CardMouldEnum.SINGLE_VALUE.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.SINGLE_VALUE);
                int[] v=new int[cardMouldBO.getCountMap().size()];
                int inx=0;
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    v[inx]=key;
                    inx++;
                }
                cardBO.setSingleTupleValue(transform(v));
                cardBO.setVsLevel(cardBO.getSingleTupleValue());
            }else if(bool){
                cardBO.setWeight(CardMouldEnum.DIFFERENT_COLOR_LINK.getWeight());
                cardBO.setDesc(CardMouldEnum.DIFFERENT_COLOR_LINK.getDesc());
                cardBO.setOptimalCardType(CardMouldEnum.DIFFERENT_COLOR_LINK);
                int[] v=new int[cardMouldBO.getCountMap().size()];
                int inx=0;
                for(Integer key:cardMouldBO.getCountMap().keySet()){
                    v[inx]=key;
                    inx++;
                }
                cardBO.setSingleTupleValue(transform(v));
                cardBO.setVsLevel(cardBO.getSingleTupleValue());
            }
            cardBO.mergeIntactCard(value);
            cardBO.setRealCard(findRealCard(transform(cardBO.getIntactCard())));
            processList.add(cardBO);
        }
        optimal(processList,resultList);
    }

    private static void sort(List<CardBO> resultList){
        List<CardBO> result=Lists.newLinkedList();
        result.addAll(resultList);
        resultList.clear();
        int weight=result.stream().mapToInt(CardBO::getWeight).max().getAsInt();
        CardBO resultCardBO=new CardBO(null);
        for(CardBO cardBO:result){
            if(weight==cardBO.getWeight()){
                if(null==resultCardBO.getVsLevel()||resultCardBO.getVsLevel().length==0){
                    resultCardBO.setCard(cardBO.getCard());
                    resultCardBO.setWeight(weight);
                    resultCardBO.setDesc(cardBO.getDesc());
                    resultCardBO.setOptimalCardType(cardBO.getOptimalCardType());
                    resultCardBO.setRealCard(cardBO.getRealCard());
                    resultCardBO.setSingleTupleValue(cardBO.getSingleTupleValue());
                    resultCardBO.setPairTupleValue(cardBO.getPairTupleValue());
                    resultCardBO.setOtherTupleValue(cardBO.getOtherTupleValue());
                    resultCardBO.setVsLevel(cardBO.getVsLevel());
                    resultCardBO.setIntactCard(cardBO.getIntactCard());
                }else {
                    int inx=cardBO.getVsLevel().length-1;
                    while (inx>=0){
                        if(cardBO.getVsLevel()[inx]>resultCardBO.getVsLevel()[inx]){
                            resultCardBO.setWeight(resultCardBO.getWeight()+1);
                            resultCardBO.setCard(cardBO.getCard());
                            resultCardBO.setDesc(cardBO.getDesc());
                            resultCardBO.setOptimalCardType(cardBO.getOptimalCardType());
                            resultCardBO.setRealCard(cardBO.getRealCard());
                            resultCardBO.setSingleTupleValue(cardBO.getSingleTupleValue());
                            resultCardBO.setPairTupleValue(cardBO.getPairTupleValue());
                            resultCardBO.setOtherTupleValue(cardBO.getOtherTupleValue());
                            resultCardBO.setVsLevel(cardBO.getVsLevel());
                            resultCardBO.setIntactCard(cardBO.getIntactCard());

                            cardBO.setWeight(resultCardBO.getWeight());
                            break;
                        }else if(cardBO.getVsLevel()[inx]==resultCardBO.getVsLevel()[inx]){
                            cardBO.setWeight(resultCardBO.getWeight());
                            inx--;
                        }else {
                            cardBO.setWeight(resultCardBO.getWeight()-1);
                            break;
                        }
                    }
                }
            }
            resultList.add(cardBO);
        }
        resultList.sort(comparing(CardBO::getWeight));
    }

    private static void optimal(List<CardBO> processList,List<CardBO> resultList){
        processList.sort(comparing(CardBO::getWeight).reversed());
        int weight=processList.stream().mapToInt(CardBO::getWeight).max().getAsInt();
        CardBO resultCardBO=new CardBO(null);
        for(int i=0;i<processList.size();i++){
            if(weight==processList.get(i).getWeight()){
                if(null==resultCardBO.getVsLevel()||resultCardBO.getVsLevel().length==0){
                    resultCardBO.setCard(processList.get(i).getCard());
                    resultCardBO.setWeight(weight);
                    resultCardBO.setDesc(processList.get(i).getDesc());
                    resultCardBO.setOptimalCardType(processList.get(i).getOptimalCardType());
                    resultCardBO.setRealCard(processList.get(i).getRealCard());
                    resultCardBO.setSingleTupleValue(processList.get(i).getSingleTupleValue());
                    resultCardBO.setPairTupleValue(processList.get(i).getPairTupleValue());
                    resultCardBO.setOtherTupleValue(processList.get(i).getOtherTupleValue());
                    resultCardBO.setVsLevel(processList.get(i).getVsLevel());
                    resultCardBO.setIntactCard(processList.get(i).getIntactCard());
                }else {
                    int inx=processList.get(i).getVsLevel().length-1;
                    while (inx>=0){
                        if(processList.get(i).getVsLevel()[inx]>resultCardBO.getVsLevel()[inx]){
                            resultCardBO.setWeight(resultCardBO.getWeight()+1);
                            resultCardBO.setCard(processList.get(i).getCard());
                            resultCardBO.setDesc(processList.get(i).getDesc());
                            resultCardBO.setOptimalCardType(processList.get(i).getOptimalCardType());
                            resultCardBO.setRealCard(processList.get(i).getRealCard());
                            resultCardBO.setSingleTupleValue(processList.get(i).getSingleTupleValue());
                            resultCardBO.setPairTupleValue(processList.get(i).getPairTupleValue());
                            resultCardBO.setOtherTupleValue(processList.get(i).getOtherTupleValue());
                            resultCardBO.setVsLevel(processList.get(i).getVsLevel());
                            resultCardBO.setIntactCard(processList.get(i).getIntactCard());
                            break;
                        }else if(processList.get(i).getVsLevel()[inx]==resultCardBO.getVsLevel()[inx]){
                            inx--;
                        }else {
                            break;
                        }
                    }
                }
            }
            LogUtil.info(String.format(" the detailed result: processList.get(%s)=[ %s ]",i,processList.get(i)));
        }
        LogUtil.info(String.format(" the best result: resultCardBO=[ %s ]",resultCardBO));
        resultList.add(resultCardBO);
    }

    public static CardFirstEnum findCardFirstEnum(int[] cards){
        if(cards[0]%13==cards[1]%13)
            return CardFirstEnum.PAIR;
        else
            return CardFirstEnum.SINGLE;
    }

    @Data
    private static class CardMouldBO {
        private final List<Integer> target=Lists.newArrayList();
        private final List<Integer> countTarget=Lists.newArrayList();
        private final Map<Integer,Integer> countMap=Maps.newHashMap();

        public CardMouldBO(int[] s) {
            this.target.add(s[0]);
            this.target.add(s[1]);
        }
    }

    @Data
    public static class CardBO{
        private int weight;
        private String desc;
        private CardMouldEnum optimalCardType;
        private String realCard;
        private int[] singleTupleValue;
        private int[] pairTupleValue;
        private int otherTupleValue;
        private int[] vsLevel;
        private int[] card;
        private int[] intactCard;

        public CardBO(int[] o) {
            this.card=o;
        }

        public void mergeIntactCard(int[] intactCard) {
            this.intactCard=new int[this.card.length+intactCard.length];
            System.arraycopy(this.card,0,this.intactCard,0,this.card.length);
            System.arraycopy(intactCard,0,this.intactCard,this.card.length,intactCard.length);
            Arrays.sort(this.intactCard);
        }

        public void setIntactCard(int[] intactCard) {
            this.intactCard=intactCard;
        }

        public int[] findOptimalCards(){
            LogUtil.info(String.format("findOptimalCards() : this=[ %s ]",this));
            switch (this.getOptimalCardType()){
                case MAX_SAME_COLOR_LINK:
                case SAME_COLOR_LINK:
                case SAME_COLOR_UNLINK:
                case DIFFERENT_COLOR_LINK:
                    return this.getSingleTupleValue();
                case FOUR_SAME_VALUE:
                case THREE_SAME_VALUE:
                    List<Integer> var1=Lists.newArrayList();
                    for(int v:this.getIntactCard()){
                        if(v%ONE_ROUND==this.getOtherTupleValue())
                            var1.add(v);
                    }
                    return var1.stream().mapToInt(Integer::valueOf).toArray();
                case THREE_TWO_SAME_VALUE:
                    List<Integer> var2=Lists.newArrayList();
                    for(int v:this.getIntactCard()){
                        if(v%ONE_ROUND==this.getOtherTupleValue())
                            var2.add(v);
                        else if(v%ONE_ROUND==this.getPairTupleValue()[0])
                            var2.add(v);
                    }
                    return var2.stream().mapToInt(Integer::valueOf).toArray();
                case TWO_SAME_VALUE:
                case ONE_SAME_VALUE:
                    List<Integer> var3=Lists.newArrayList();
                    for(int p:this.getPairTupleValue()){
                        for(int v:this.getIntactCard()){
                            if(v%ONE_ROUND==p)
                                var3.add(v);
                        }
                    }
                    return var3.stream().mapToInt(Integer::valueOf).toArray();
            }
            return new int[]{};
        }
    }

    public static String findRealCard(int[] cards){
        StringBuffer sb=new StringBuffer();
        Arrays.stream(cards).forEach(s->{
            s=genOriginalValue(s);
            switch (s){
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    sb.append(s);
                    break;
                case 10:
                    sb.append("T");
                    break;
                case 11:
                    sb.append("J");
                    break;
                case 12:
                    sb.append("Q");
                    break;
                case 13:
                    sb.append("K");
                    break;
                case 14:
                    sb.append("A");
                    break;
            }
        });
        return sb.toString();
    }


    private static int[] transform(int[] cards){
        int[] result=new int[cards.length];
        for(int i=0;i<cards.length;i++){
            result[i]=genOriginalValue(cards[i]);
        }
        Arrays.sort(result);
        return result;
    }

    private static int genOriginalValue(int card) {
        int num = card % ONE_ROUND;
        num = num == 0 ? ONE_ROUND : num;
        num = num == 1 ? ONE_ROUND + 1 : num;
        return num;
    }
}