package com.zren.platform.bout.common.util;

/**  

 * Copyright © 2019pact. All rights reserved.

 *

 * @Title: PermutationUtil.java

 * @Prject: game-logic

 * @Package: com.zren.platform.logic.zjh.common.util

 * @Description: TODO

 * @author: gavin.lyu  

 * @date: 2019年9月21日 下午4:06:20

 * @version: V1.0  

 */

import java.math.BigInteger;
import java.util.List;
import org.raistlic.common.permutation.Combination;

import net.nullschool.util.DigitalRandom;

/**
 * @author gavin.lyu
 * 排列组合工具
 */
public class PermutationUtil {

	/*
	 * 排列组合，随机获取其中一个
	 */
	public static <T> List<T> getRandomList(List<T> list,int n){
		Combination<T> combination = Combination.of(list,n);
		int count = combination.getCombinationCount().intValue();
		DigitalRandom random = new DigitalRandom();
		int ran = random.nextInt(count);
		int i = 0;
		for(List<T> l:combination) {
			if(i==ran) {
				return l;
			}
			i++;
		}
		return combination.getCombination(new BigInteger(String.valueOf(i-1)));
	}
	
	
	
	
	
	
	
	
	
	
}
