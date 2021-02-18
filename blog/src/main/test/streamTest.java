package main.test;

import main.java.stream.Person;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class streamTest {

    static List<Person> personList = new ArrayList<Person>();

    static {
        personList.add(new Person("Tom", 8900, "male", "New York",19));
        personList.add(new Person("Jack", 7000, "male", "Washington",19));
        personList.add(new Person("Lily", 7800, "female", "Washington",18));
        personList.add(new Person("Anni", 8200, "female", "New York",20));
        personList.add(new Person("Owen", 9500, "male", "New York",21));
        personList.add(new Person("Alisa", 7900, "female", "New York",22));
    }

    @Test
    void Test1(){
        List<Integer> list = Arrays.asList(7,8,4,3,23,11,4,1,2,0,245);

        //遍历符合条件的元素
        list.stream().filter(x-> x>5).forEach(System.out::println);
        //匹配第一个
        Optional<Integer> firstNum = list.stream().filter(x -> x > 5).findFirst();
        //匹配任意一个（适用于并行流）
        Optional<Integer> anyNum = list.parallelStream().filter(x -> x > 5).findAny();
        //是否包含任意条件
        boolean b = list.stream().anyMatch(x -> x > 7 && x < 7);
        System.out.println("第一个数据为"+firstNum.get());
        System.out.println("匹配任意一个"+anyNum.get());
        System.out.println("是否包含符合条件的数据"+b);

    }

    @Test
    void Test2(){
        // 筛选员工中工资高于8000的人，并形成新的集合
        List<String> filterList = personList.stream().filter(x -> x.getSalary() > 8000).map(Person::getName).collect(Collectors.toList());
        System.out.print("高于8000的员工姓名：" + filterList);

        List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
        // 获取String集合中最长的元素
        Optional<String> max = list.stream().max(Comparator.comparing(String::length));
        System.out.println("最长的字符串：" + max.get());


    }

    @Test
    void Test3(){
        List<Integer> list1 = Arrays.asList(7, 6, 9, 4, 11, 6);

        // 自然排序
        Optional<Integer> max1 = list1.stream().max(Integer::compareTo);
        // 自定义排序
        Optional<Integer> max2 = list1.stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println("自然排序的最大值：" + max1.get());
        System.out.println("自定义排序的最大值：" + max2.get());

        // 案例三：获取员工工资最高的人。
        Optional<Person> max = personList.stream().max(Comparator.comparingInt(Person::getSalary));
        System.out.println("员工工资最大值：" + max.get().getSalary());
    }

    @Test
    void Test4(){
        //计算Integer集合中大于6的元素的个数。
        List<Integer> list = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
        long count = list.stream().filter(x -> x > 6).count();
        System.out.println("集合中大于6的元素的个数"+count);

    }

    @Test
    void Test5(){
        //英文字符串数组的元素全部改为大写。整数数组每个元素+3。
        String[] strArr = { "abcd", "bcdd", "defde", "fTr" };
        List<Integer> list = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
        List<String> collect = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());
        List<Integer> num = list.stream().map(x -> x + 3).collect(Collectors.toList());
        System.out.println("元素全部改为大写后"+collect);
        System.out.println("整数数组每个元素+3"+num);
    }
    @Test
    void Test6(){
        //将员工的薪资全部增加1000。
        // 改变原来员工集合方式
        List<Person> list = personList.stream().map(person -> {
            person.setSalary(person.getSalary() + 1000);
            return person;
        }).collect(Collectors.toList());
        System.out.println("一次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
        System.out.println("一次改动后：" + list.get(0).getName() + "-->" + list.get(0).getSalary());

        // 不改变原来员工集合方式
        List<Person> personListNew = personList.stream().map(person -> {
            Person personNew = new Person(person.getName(), 0, person.getSex(), person.getArea(),person.getAge());
            personNew.setSalary(person.getSalary() + 10000);
            return personNew;
        }).collect(Collectors.toList());
        System.out.println("一次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
        System.out.println("一次改动后：" + personListNew.get(0).getName() + "-->" + personListNew.get(0).getSalary());
    }

    @Test
    void Test7(){
        //将两个字符数组合并成一个新的字符数组。
        List<String> list = Arrays.asList("m,k,l,a", "1,3,5,7");
        List<String> collect = list.stream().flatMap(s -> {
            String[] split = s.split(",");
            Stream<String> stream = Arrays.stream(split);
            return stream;
        }).collect(Collectors.toList());
        System.out.println("处理前的集合：" + list);
        System.out.println("处理后的集合：" + collect);
    }

    @Test
    void Test8(){
        List<Integer> list = Arrays.asList(1, 3, 2, 8, 11, 4);
        // 求和方式1
        Optional<Integer> sum = list.stream().reduce((x, y) -> x + y);
        // 求和方式2
        Optional<Integer> sum2 = list.stream().reduce(Integer::sum);
        // 求和方式3
        Integer sum3 = list.stream().reduce(0, Integer::sum);
        // 求乘积
        Optional<Integer> product = list.stream().reduce((x, y) -> x * y);

        // 求最大值方式一
        Optional<Integer> max = list.stream().reduce((x, y) -> x > y ? x : y);

        // 求最大值方式二
        Integer max2 = list.stream().reduce(1, Integer::max);


        System.out.println("list求和：" + sum.get() + "," + sum2.get() + "," + sum3);
        System.out.println("list求积：" + product.get());
        System.out.println("list求和：" + max.get() + "," + max2);
    }

    @Test
    void Test9(){
        //求所有员工的工资之和和最高工资。
        Optional<Integer> sum = personList.stream().map(Person::getSalary).reduce((x,y) ->x+y);

        System.out.println("所有员工的工资之和为:"+sum.get());

        Optional<Integer> max = personList.stream().map(Person::getSalary).reduce((x, y) -> x > y ? x : y);
        System.out.println("所有员工的最高工资为："+max.get());
    }

    @Test
    void Test10(){
        //因为流不存储数据，那么在流中的数据完成处理后，需要将流中的数据重新归集到新的集合里。
        //toList、toSet和toMap比较常用，另外还有toCollection、toConcurrentMap等复杂一些的用法。
        Map<String, Person> map = personList.stream().filter(p -> p.getSalary() > 8000).collect(Collectors.toMap(Person::getName, p -> p));
        System.out.println("toMap:" + map);

        // 求总数
        Long count = personList.stream().collect(Collectors.counting());

        // 求平均工资
        Double ave = personList.stream().collect(Collectors.averagingDouble(Person::getSalary));

        //求最高工资
        Optional<Integer> max = personList.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare));

        // 求工资之和
        Integer sum = personList.stream().collect(Collectors.summingInt(Person::getSalary));

        // 一次性统计所有信息

        DoubleSummaryStatistics collect1 = personList.stream().collect(Collectors.summarizingDouble(Person::getSalary));

        System.out.println("员工总数：" + count);
        System.out.println("员工平均工资：" + ave);
        System.out.println("员工工资总和：" + sum);
        System.out.println("员工工资所有统计：" + collect1);
    }

    @Test
    void Test11(){
        // 将员工按薪资是否高于8000分组
        Map<Boolean, List<Person>> collect = personList.stream().collect(Collectors.partitioningBy(person -> person.getSalary() > 8000));
        // 将员工按性别分组
        Map<String, List<Person>> collect1 = personList.stream().collect(Collectors.groupingBy(Person::getSex));
        // 将员工先按性别分组，再按地区分组
        Map<String, Map<String, List<Person>>> collect2 = personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));

        System.out.println("员工按薪资是否大于8000分组情况：" + collect);
        System.out.println("员工按性别分组情况：" + collect1);
        System.out.println("员工按性别、地区：" + collect2);
    }

    @Test
    void Test12(){
        //joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
        String name = personList.stream().map(Person::getName).collect(Collectors.joining(","));
        System.out.println("所有员工的姓名"+name);
        List<String> list = Arrays.asList("A", "B", "C");
        String collect = list.stream().collect(Collectors.joining("-"));
        System.out.println("拼接后的字符串：" + collect);
    }

    @Test
    void Test13(){
        // 按工资升序排序（自然排序）
        List<String> newList = personList.stream().sorted(Comparator.comparing(Person::getSalary)).map(Person::getName)
                .collect(Collectors.toList());
        // 按工资倒序排序
        List<String> newList2 = personList.stream().sorted(Comparator.comparing(Person::getSalary).reversed())
                .map(Person::getName).collect(Collectors.toList());
        // 先按工资再按年龄升序排序
        List<String> newList3 = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge)).map(Person::getName)
                .collect(Collectors.toList());
        // 先按工资再按年龄自定义排序（降序）
        List<String> newList4 = personList.stream().sorted((p1, p2) -> {
            if (p1.getSalary() == p2.getSalary()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getSalary() - p1.getSalary();
            }
        }).map(Person::getName).collect(Collectors.toList());

        System.out.println("按工资升序排序：" + newList);
        System.out.println("按工资降序排序：" + newList2);
        System.out.println("先按工资再按年龄升序排序：" + newList3);
        System.out.println("先按工资再按年龄自定义降序排序：" + newList4);
    }

    @Test
    void Test14(){
        String[] arr1 = { "a", "b", "c", "d" };
        String[] arr2 = { "d", "e", "f", "g" };

        Stream<String> stream = Arrays.stream(arr1);
        Stream<String> stream1 = Arrays.stream(arr2);

        // concat:合并两个流 distinct：去重
        List<String> collect = Stream.concat(stream, stream1).distinct().collect(Collectors.toList());
        // limit：限制从流中获得前n个数据
        List<Integer> collect1 = Stream.iterate(1, x -> x + 2).limit(10).collect(Collectors.toList());
        // skip：跳过前n个数据
        List<Integer> collect2 = Stream.iterate(1, x -> x + 1).skip(2).limit(10).collect(Collectors.toList());

        System.out.println("流合并：" + collect);
        System.out.println("limit：" + collect1);
        System.out.println("skip：" + collect2);
    }

}
