## 前言

千呼万唤始出来，在经过前面lambda表达式和函数式接口的学习。终于可以学习Stream了。如果你还不熟悉lambda表达式和几个常见的函数式接口，建议先从这两个学习，不然下面看的你肯定是一头雾水。



## Stream概述

Java 8是一个非常成功的版本，这个版本新增的Stream，配合同版本出现的Lambda，给我们操作集合提供了极大的便利。

>**Stream**将要处理的元素集合看作一种流，在流的过程中，借助Stream API对流中的元素进行操作,比如：筛选，排序，聚合等。



`Stream`可以由数组或集合创建，对流的操作分为两种：

1. 中间操作，每次返回一个新的流，可以有多个。
2. 终端操作，每个流只能进行一次终端操作，终端操作结束后流无法再次使用。终端操作会产生一个新的集合或值。

另外，**`Stream`**有几个特性：

1. stream不存储数据，而是按照特定的规则对数据进行计算，一般会输出结果。
2. stream不会改变数据源，通常情况下会产生一个新的集合或一个值
3. stream具有延迟执行的特性，只有调用终端操作时，中间操作才会执行。

## Stream创建

`stream`可以通过集合数组创建。

1. 通过`java.util.Collection.stream()`方法用集合创建流。

~~~java
List<String> list = Arrays.asList("a", "b", "c");
// 创建一个顺序流
Stream<String> stream = list.stream();
// 创建一个并行流
Stream<String> parallelStream = list.parallelStream();
~~~

2. 通过`java.util.Arrays.stream(T[] array)`方法用数组创建流。

~~~java
int[] array={1,3,5,6,8};
IntStream stream = Arrays.stream();
~~~

3. 使用`Stream`的静态方法：`of()、iterate()、generate()`。

~~~java
Stream<Integer> stream = Stream.of(1,2,3,4,5);

Stream<Integer> stream2 = Stream.Iterate(0,x -> x+1).limit(4);
stream2.forEach(System.out::println);

Stream<Double> stream3 = Stream.generate(Math::random).limit(3);
stream3.forEach(System.out::println);
~~~



`stream`和`parallelStream`的简单区分：`stream`是顺序流，由主线程按顺序对流执行操作，而`parallelStream`是并行流，内部以多线程并行执行的方式对流进行操作，但前提是流中的数据处理没有顺序要求。例如筛选集合中的奇数，两者的处理不同之处：

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-1)

如果流中的数据量足够大，并行流可以加快处速度。

除了直接创建并行流，还可以通过`parallel()`把顺序流转换成并行流：

~~~java
Optional<Integer> findFirst = list.stream().parallel().filter(x->x>7).findFirst();
~~~



## Stream的使用

首先编写一个员工类：

~~~java
package main.java.stream;

public class Person {
    private String name;  // 姓名
    private int salary; // 薪资
    private int age; // 年龄
    private String sex; //性别
    private String area;  // 地区

    public Person(String name, int salary, String sex, String area,int age) {
        this.name = name;
        this.salary = salary;
        this.sex = sex;
        this.area = area;
        this.age = age;
    }
  // 省略set、get方法
}

~~~

新建一个测试类：

~~~java
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
}
~~~



### 遍历/匹配（foreach/find/match）

`Stream`也是支持类似集合中的遍历和匹配元素的，只是Stream中的元素是以Optional类型存在的。

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-2)

~~~java
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
~~~

> 输出结果：

![image-20210219113027542](C:\Users\80007102\Desktop\blogs\Stream-3)



### 筛选(filter)

筛选，是按照一定的规则校验流中的元素，将符合条件的元素提取到新的流中的操作。

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-4)

~~~java
    @Test
    void Test2(){
        // 筛选员工中工资高于8000的人，并形成新的集合
        List<String> filterList = personList.stream().filter(x -> x.getSalary() >  8000).map(Person::getName).collect(Collectors.toList());
        System.out.print("高于8000的员工姓名：" + filterList);

    }
~~~



### 聚合（max/min/count)

`max`、`min`、`count`这些字眼你一定不陌生，没错，在mysql中我们常用它们进行数据统计。Java stream中也引入了这些概念和用法，极大地方便了我们对集合、数组的数据统计工作。

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-5)

**获取`String`集合中最长的元素。**

~~~java
   void Test2(){
        List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
        // 获取String集合中最长的元素
        Optional<String> max =list.stream().max(Comparator.comparing(String::length));
        System.out.println("最长的字符串：" + max.get());
    }
~~~

> 输出结果：最长的字符串：weoujgsd



**获取`Integer`集合中的最大值和找到员工工资最大值。**

~~~java
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
~~~

> 输出结果：

![image-20210219135151868](C:\Users\80007102\Desktop\blogs\stream-6.png)



**计算`Integer`集合中大于6的元素的个数。**

~~~java
   @Test
    void Test4(){
        List<Integer> list = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
        long count = list.stream().filter(x -> x > 6).count();
        System.out.println("集合中大于6的元素的个数"+count);
    }
~~~



> 输出结果：集合中大于6的元素的个数4



### 映射(map/flatMap)

映射，可以将一个流的元素按照一定的映射规则映射到另一个流中。分为`map`,`flatMap`:

- **map**：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
- **flatMap**：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-7.png)

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-8.png)

 

**英文字符串数组的元素全部改为大写。整数数组每个元素+3。**

~~~ java
 @Test
    void Test5(){
        String[] strArr = { "abcd", "bcdd", "defde", "fTr" };
        List<Integer> list = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
        List<String> collect = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());
        List<Integer> num = list.stream().map(x -> x + 3).collect(Collectors.toList());
        System.out.println("元素全部改为大写后"+collect);
        System.out.println("整数数组每个元素+3"+num);
    }			
~~~

输出结果：

>元素全部改为大写后[ABCD, BCDD, DEFDE, FTR]
>整数数组每个元素+3[10, 9, 7, 11, 5, 14, 12]



**将员工的薪资全部增加1000。**

~~~java
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
~~~

输出结果

> 一次改动前：Tom-->9900
> 一次改动后：Tom-->9900
> 一次改动前：Tom-->9900
> 一次改动后：Tom-->19900



**将两个字符数组合并成一个新的字符数组。**

~~~java
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
~~~

输出结果

> 处理前的集合：[m,k,l,a, 1,3,5,7]
> 处理后的集合：[m, k, l, a, 1, 3, 5, 7]

### 归约(reduce)

归约，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和，求乘积和求最值操作。

**求`Integer`集合的元素之和、乘积和最大值。**

~~~java
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
~~~

输出结果

> list求和：29,29,29
> list求积：2112
> list求和：11,11



**求所有员工的工资之和和最高工资。**

~~~java
   @Test
    void Test9(){
        //求所有员工的工资之和和最高工资。
        Optional<Integer> sum = personList.stream().map(Person::getSalary).reduce((x,y) ->x+y);

        System.out.println("所有员工的工资之和为:"+sum.get());

        Optional<Integer> max = personList.stream().map(Person::getSalary).reduce((x, y) -> x > y ? x : y);
        System.out.println("所有员工的最高工资为："+max.get());
    }
~~~

输出结果

> 所有员工的工资之和为:49300
> 所有员工的最高工资为：9500



### 收集(collect)

`collect`，收集，可以说是内容最繁多、功能最丰富的部分了。从字面上去理解，就是把一个流收集起来，最终可以是收集成一个值也可以收集成一个新的集合。

>`collect`主要依赖`java.util.stream.Collectors`类内置的静态方法。



#### 归集(toList/toSet/toMap)

因为流不存储数据，那么在流中的数据完成处理后，需要将流中的数据重新归集到新的集合里。`toList`、`toSet`和`toMap`比较常用，另外还有`toCollection`、`toConcurrentMap`等复杂一些的用法。

~~~java
@Test
    void Test10(){
        
        List<Integer> listNew = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
		Set<Integer> set = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());
      
        Map<String, Person> map = personList.stream().filter(p -> p.getSalary() > 8000).collect(Collectors.toMap(Person::getName, p -> p));
        System.out.println("toMap:" + map);
    }
~~~

运行结果

> toList：[6, 4, 6, 6, 20]
> toSet：[4, 20, 6]
> toMap：{Tom=mutest.Person@5fd0d5ae, Anni=mutest.Person@2d98a335}

#### 统计(count/averaging)

`Collectors`提供了一系列用于数据统计的静态方法：

- 计数：`count`
- 平均值：`averagingInt`、`averagingLong`、`averagingDouble`
- 最值：`maxBy`、`minBy`
- 求和：`summingInt`、`summingLong`、`summingDouble`
- 统计以上所有：`summarizingInt`、`summarizingLong`、`summarizingDouble`



**统计员工人数、平均工资、工资总额、最高工资。**

~~~java
 @Test
    void Test10(){

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
~~~

运行结果

> 员工总数：6
> 员工平均工资：8216.666666666666
> 员工工资总和：49300
> 员工工资所有统计：DoubleSummaryStatistics{count=6, sum=49300.000000, min=7000.000000, average=8216.666667, max=9500.000000}



#### 分组(partitioningBy/groupingBy)

- 分区：将stream按条件分为两个Map，比如员工按薪水是否高于8000分为两部分。
- 分组：将集合分为多个Map，比如员工按照性别分组。有单级分组和多级分组。

![](C:\Users\80007102\Desktop\blogs\Stream-9.png)



**将员工按薪资是否高于8000分为两部分；将员工按性别和地区分组**

```java
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
```

输出结果

> 员工按薪资是否大于8000分组情况：{false=[main.java.stream.Person@18eed359, main.java.stream.Person@3e9b1010, main.java.stream.Person@6c3708b3], true=[main.java.stream.Person@6f1fba17, main.java.stream.Person@185d8b6, main.java.stream.Person@67784306]}
> 员工按性别分组情况：{female=[main.java.stream.Person@3e9b1010, main.java.stream.Person@185d8b6, main.java.stream.Person@6c3708b3], male=[main.java.stream.Person@6f1fba17, main.java.stream.Person@18eed359, main.java.stream.Person@67784306]}
> 员工按性别、地区：{female={New York=[main.java.stream.Person@185d8b6, main.java.stream.Person@6c3708b3], Washington=[main.java.stream.Person@3e9b1010]}, male={New York=[main.java.stream.Person@6f1fba17, main.java.stream.Person@67784306], Washington=[main.java.stream.Person@18eed359]}}



#### 接合(joining)

`joining`可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。

~~~java
  @Test
    void Test12(){
        String name = personList.stream().map(Person::getName).collect(Collectors.joining(","));
        System.out.println("所有员工的姓名"+name);
        List<String> list = Arrays.asList("A", "B", "C");
        String collect = list.stream().collect(Collectors.joining("-"));
        System.out.println("拼接后的字符串：" + collect);
    }
~~~

输出结果

>所有员工的姓名Tom,Jack,Lily,Anni,Owen,Alisa
>拼接后的字符串：A-B-C



### 排序(sorted)

sorted，中间操作。有两种排序：

- sorted()：自然排序，流中元素需实现Comparable接口
- sorted(Comparator com)：Comparator排序器自定义排序



 **将员工按工资由高到低（工资一样则按年龄由大到小）排序**

~~~java
  @Test
void Test13(){
    // 按工资升序排序（自然排序）
       List<String> newList = personList.stream().sorted(Comparator.comparing(Person::getSalary)).map(Person::getName).collect(Collectors.toList());
    // 按工资倒序排列
       List<String> newList2 = personList.stream().sorted(Comparator.comparing(Person::getSalary).reversed()).map(Person::getName).collect(Collectors.toList());
     // 先按工资再按年龄升序排序
       List<String> newList3 = personList.stream().sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge)).map(Person::getName).collect(Collectors.toList());
     // 先按工资再按年龄自定义排序（降序）一
        List<String> newList4 = personList.stream().sorted((p1, p2) -> {
            if (p1.getSalary() == p2.getSalary()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getSalary() - p1.getSalary();
            }
        }).map(Person::getName).collect(Collectors.toList());
     // 先按工资再按年龄自定义排序（降序）二
           List<String> newList5 = personList.stream().sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge).reversed()).map(Person::getName).collect(Collectors.toList());
    
        System.out.println("按工资升序排序：" + newList);
        System.out.println("按工资降序排序：" + newList2);
        System.out.println("先按工资再按年龄升序排序：" + newList3);
        System.out.println("先按工资再按年龄自定义降序排序：" + newList4);
        System.out.println("先按工资再按年龄自定义降序排序：" + newList5);

}
~~~

输出结果

> 按工资升序排序：[Jack, Lily, Alisa, Anni, Tom, Owen]
> 按工资降序排序：[Owen, Tom, Anni, Alisa, Lily, Jack]
> 先按工资再按年龄升序排序：[Jack, Lily, Alisa, Anni, Tom, Owen]
> 先按工资再按年龄自定义降序排序1：[Owen, Tom, Anni, Alisa, Lily, Jack]
> 先按工资再按年龄自定义降序排序2：[Owen, Tom, Anni, Alisa, Lily, Jack]



###  提取/组合

流也可以进行合并、去重、限制、跳过等操作。

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-10.png)

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-11.png)

![在这里插入图片描述](C:\Users\80007102\Desktop\blogs\Stream-12.png)

~~~JAVA
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
~~~

运行结果:

>流合并：[a, b, c, d, e, f, g]
>limit：[1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
>skip：[3, 4, 5, 6, 7, 8, 9, 10, 11, 12]