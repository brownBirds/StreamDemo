package main.test;

import main.java.demo.functionInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class functionInterfaceTest {

    static  List l1 = new ArrayList();
    static {
        l1.add(3);l1.add(4);l1.add(10);l1.add(12);l1.add(13);l1.add(19);
    }

    public String strHandler(String str, functionInterface f){
        return (String) f.getValue(str);
    }

    public Integer strHandler1(Integer str,functionInterface f){
        return (Integer) f.getValue(str);
    }

    public void strHandler2(List<Integer> str, Consumer<List<Integer>> c){ c.accept(str);}
    @Test
    void getValue1() {
        //作为参数传递Lambda表达式：
        String upperStr = strHandler("touppercase", (str) -> ( (String)str).toUpperCase());
        String newStr = strHandler("馒头片快点好不好！", (str) -> ((String) str).substring(0, 3));
        System.out.println(upperStr);
        System.out.println(newStr);
    }
    @Test
    void getValue2(){
        Integer result = strHandler1(2,str ->{int num = (Integer) str;return num=num*num;});
        System.out.println(result);
    }
    // 如果集合中数字超过10就打印
    @Test
    void getValue3(){
        List l2= new ArrayList();
        l2.add(3);l2.add(4);l2.add(10);l2.add(12);l2.add(13);l2.add(19);
        strHandler2(l2,l3 ->{
            System.out.println("大于10的有:");
            for (int i = 0; i < l3.size(); i++) {
                if (l3.get(i)>10){
                    System.out.println(l3.get(i));
                }
            }
        });
    }
    @Test
    void getValue4(){

        Consumer<List<Integer>> c = l1 ->{
            System.out.println("大于10的有:");
            for (int i = 0; i < l1.size(); i++) {
                if (l1.get(i)>10){
                    System.out.println(l1.get(i));
                }
            }
        };
        Consumer<List<Integer>> listConsumer = c.andThen(l1 -> {
            System.out.println("大于15的有:");
            for (int i = 0; i < l1.size(); i++) {
                if (l1.get(i) > 15) {
                    System.out.println( l1.get(i));
                }
            }
        });
        listConsumer.accept(l1);
    }

    @Test
    void getValue5(){
        Supplier<Double> sup = ()->Math.random()*1000;
        System.out.println(sup.get());
    }

    @Test
    void getvalue6(){
        // 找到数组中最大的值
        Function<Integer[],Integer> fun = num->{
            int Max = Integer.MIN_VALUE;
            for (int i = 0; i < num.length ;i++) {
                if (num[i] > Max)
                    Max = num[i];
            }
            return Max;
        };
        Integer[] num = new Integer[]{-133,34,-3425,433,3234,-3467,90043};

        System.out.println(fun.apply(num));
    }

    @Test
    void getvalue7(){
        //判断数组里是都所有值都大于0
        Predicate<Integer[]> predicate = num ->{
            for (int i = 0; i < num.length; i++) {
                if (num[i] < 0)
                    return false;
            }
            return true;
        };
        Integer[] num = new Integer[]{-133,34,-3425,433,3234,-3467,90043};
        System.out.println(predicate.test(num));
    }

    @Test
    void getvalue8(){
        //判断score数组里是都所有值都大于0,并且是否有大于200的
        Predicate<Integer[]> predicate = num ->{
            for (int i = 0; i < num.length; i++) {
                if (num[i] < 0)
                    return false;
            }
            return true;
        };
        Predicate<Integer[]> addPredicate = age ->{
           for (Integer str:age){
               if (str > 200)
                   return false;
           }
            return true;
        };
        Integer[] score = new Integer[]{133,34,100,89,98,100,101};
        Predicate<Integer[]> and = predicate.and(addPredicate);
        System.out.println(and.test(score));
    }




}