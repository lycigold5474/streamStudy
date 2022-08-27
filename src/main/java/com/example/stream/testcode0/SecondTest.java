package com.example.stream.testcode0;

import com.example.stream.testcode0.model.Person;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SecondTest {
    @PostConstruct
    public void testStream(){
        long beforeTime = System.currentTimeMillis();
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("짱구", 23, "010-1234-1234"));
        personList.add(new Person("유리", 24, "010-2341-2341"));
        personList.add(new Person("철수", 29, "010-3412-3412"));
        personList.add(new Person("맹구", 25, null));


        // Function.identity는 t -> t, 항상 입력된 인자(자신)를 반환합니다.
        Map<String, Person> personMap = personList.stream()
                .collect(Collectors.toMap(Person::getName, Function.identity()));

        // 동일한 기능
        Map<String, Person> personMap1 = personList.stream()
                        .collect(Collectors.toMap(new Function<Person, String>() {
                            @Override
                            public String apply(Person person) {
                                return person.getName();
                            }
                        }, new Function<Person, Person>() {
                            @Override
                            public Person apply(Person person) {
                                return person;
                            }
                        }));
        
        printPerson(personMap, "For문");
        printPerson(personMap1, "For문1");

        Map<String, Person> personMap2 = personList.stream()
                .filter(person -> person.getAge() > 24) // 25살이상만 골라내기
                .collect(Collectors.toMap(Person::getName, Function.identity()));
        printPerson(personMap2, "Filtered 25살이상");

        // 키가 2개이상인 경우 IllegallStateException에러남, BinaryOperator
        personList.add(new Person("맹구", 25, "xxxx"));
        Map<Integer, Person> personMap3 = personList.stream()
                .collect(Collectors.toMap(
                        o -> o.getAge(),
                        Function.identity(),
                        (oldValue, newValue) -> newValue)); // 중복되는 경우 새 값으로 넣는다.
        printPerson(personMap2, "맹구추가");

        //중복키허용
        Map<Integer, List<Person>> duplicatedMap = personList.stream()
                .collect(Collectors.groupingBy(Person::getAge));
        printPerson1(duplicatedMap, "중복키허용");
        
        //스트림내에서 null제외하기
        Stream<String> stream = Stream.of("철수", "훈이", null, "유리", null);
        List<String> filteredList = stream.filter(Objects::nonNull)
                .collect(Collectors.toList());
        printPerson2(filteredList,"null제거");

        /**
         * 조건에 일치한 요소 찾기
         */
        // 가장 첫요소 첫기
        Person person = personList.stream()
                .filter(p -> p.getAge() == 100)
                .findFirst()
//                .get();  // 일치하는 값이 없는 경우 NoSuchElementException 발생함
                .orElse(null);  //참고 : http://www.tcpschool.com/java/java_stream_optional
        System.out.println("person 100 = " + (person != null ? person.toString() : null));
        Person person1 = personList.stream()
                .filter(p -> p.getAge() == 25)
                .findAny()
                .orElse(null);  //참고 : http://www.tcpschool.com/java/java_stream_optional
        System.out.println("person 25 = " + person1.toString());
        List<Person> test1 = personList.stream()
                .filter(p -> p.getAge() == 25)
                .collect(Collectors.toList());
        printPerson3(test1, "필터");

        //스트림 정렬
        System.out.println("** 정렬 **");
        personList.stream()
                .sorted((Comparator.comparing(Person::getAge)))
                .forEach(p -> System.out.println("p = " + p));
        //스트림 역순으로정렬
        System.out.println("** 역순 정렬 **");
        personList.stream()
                .sorted(Comparator.comparing(Person::getAge).reversed())
                .forEach(p -> System.out.println("p = " + p));
        
        // reduce로 결과 구하기
        List<Integer> list = Arrays.asList(5,4,2,1,6,7,8,3);
        Integer result = list.stream()
                .reduce(0, (value1, value2) -> value1 + value2);
        System.out.println("result = " + result);
        int intResult = list.stream()
                // .mapToInt(x -> x).sum();
                .mapToInt(Integer::intValue).sum();
        System.out.println("intResult = " + intResult);
        
        // "Swift보다 문자일이 길고 리스트중에 가장 긴 문자열
        List<String> list1 = Arrays.asList("Java", "C++", "Python", "Ruby");
        String result1 = list1.stream()
                .reduce("Swift", (val1, val2) ->
                        val1.length() >= val2.length() ? val1 : val2);
        System.out.println("result1 = " + result1);

        //단일 컬렉션 만들기
        String[][] names = new String[][]{
                {"짱구", "철수"}, {"훈이", "맹구"}
        };

        // 리스트로
        List<String> list0 = Arrays.stream(names)
                .flatMap(Stream::of)
                .collect(Collectors.toList());

        // 1차원 배열로
        String[] flattedNames = Arrays.stream(names)
                .flatMap(Stream::of).toArray(String[]::new);
    }

    public void printPerson (Map<String, Person> map, String msg) {
       /* System.out.println("****iterator문****");
        beforeTime = System.currentTimeMillis();
        Iterator<String> iter = personMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Person person = personMap.get(key);
            System.out.println(person.toString());
        }
        System.out.println("time takes:"+(System.currentTimeMillis() - beforeTime));*/
        System.out.println("****"+msg+"****");
        long beforeTime = System.currentTimeMillis();
        for(String key : map.keySet()){
            Person person = map.get(key);
            System.out.println(key+" : "+person.toString());
        }
        System.out.println("time takes:"+(System.currentTimeMillis() - beforeTime));
    }
    public void printPerson1 (Map<Integer, List<Person>> map, String msg) {

        System.out.println("****"+msg+"****");
        long beforeTime = System.currentTimeMillis();
        for(Integer key : map.keySet()){
            List<Person> persons = map.get(key);
            for (Person person : persons) {
                System.out.println("key: " +key+ " person = " + person);
            }
        }
        System.out.println("time takes:"+(System.currentTimeMillis() - beforeTime));
    }
    public void printPerson2 (List<String> list, String msg) {
        System.out.println("****"+msg+"****");
        long beforeTime = System.currentTimeMillis();
        for(String s : list){
            System.out.println("s = " + s);
        }
        System.out.println("time takes:"+(System.currentTimeMillis() - beforeTime));
    }
    public void printPerson3 (List<Person> list, String msg) {
        System.out.println("****"+msg+"****");
        long beforeTime = System.currentTimeMillis();
        for(Person s : list){
            System.out.println("s = " + s);
        }
        System.out.println("time takes:"+(System.currentTimeMillis() - beforeTime));
    }
}
