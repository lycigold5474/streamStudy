package com.example.stream.testcode0;

import com.example.stream.testcode0.model.Product;
import com.example.stream.testcode0.model.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;

public class FisrtTest {

    public void streamTest1(){
        // 배열 스트림생성
        String[] arr = new String[]{"a", "b", "c"};
        System.out.println(" = " + Arrays.toString(arr));

        Stream<String> streamArr = Arrays.stream(arr);
        Stream<String> streamOfArrayPart = Arrays.stream(arr, 1, 3);

        //출력비교
        System.out.println("=====스트림을 사용하지 않을때 배열출력======");
        for(int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
        for(String s : arr){
            System.out.println(s);
        }
        //streamd을 사용하여 출력
        streamArr.forEach(s -> System.out.println("s = " + s));




        //컬렉션 스트림 생성 예)
        // Iterfable을 상속한 Collection 인타페이스생성 (java.util에 인터페이스가 있으므로 사용하면됨)
        List<String> list = Arrays.asList("a","b","c");

        System.out.println("===== list 스트림 사용하지 않고 출력 =====");
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
            System.out.println("s = " + s);
        }
        for (String s : list) {
            System.out.println("s = " + s);
        }

        Stream<String> streamList = list.stream();
//        Stream<String> parallelStream = list.parallelStream();
        System.out.println("===== list 스트림 사용하여 출력 =====");
        streamList.forEach(s -> System.out.println("s = " + s));


        //비어있는 스트림
        Stream<String> emptyStream = streamOf(null);
        System.out.println("emptyStream.count() = " + emptyStream.count());

        //빌더로 원하는 값 추가
        Stream<String> builderStream = Stream.<String>builder()
                .add("Eric").add("Elena").add("Java")
                .build();
        //스트림 출력
        builderStream.forEach(a -> System.out.println("s = " + a));

        //generate로 생성하기
        Stream<String> generatedStream = Stream.generate(() -> "gen").limit(5); //  스트림크기는무한대이므로 최대크기제한
        System.out.println("===== generatedStream ====");
        generatedStream.forEach(s -> System.out.println("s = " + s));

        //iterate() 반복되는 요소 만들기
        Stream<Integer> iteratedStream = Stream.iterate(30, n -> n+2).limit(5);
        System.out.println("iteratedStream = " + iteratedStream);
        iteratedStream.forEach(s -> System.out.println("s = " + s));

        //기본타입형 스트림 (제네릭을 사용하지 않음)
        IntStream intStream = IntStream.range(1,5);
        intStream.forEach(s -> System.out.println("IntStream = " + s));
        LongStream longStream = LongStream.rangeClosed(1,5);
        longStream.forEach(s -> System.out.println("LongStream = " + s));

        //박싱
        Stream<Integer> boxedIntStream = IntStream.range(1,5).boxed();
        //난수생성
        DoubleStream doubles = new Random().doubles(3); // 난수 3개생성
        doubles.forEach(s -> System.out.println("doubles = " + s));

        //문자열 스트링
        // 문자를 IntStream으로 변환
        IntStream charStream = "Stream".chars();
        charStream.forEach(s -> System.out.println("charStream = " + s));

        // 문자열을 정규표현식을 이용해서 스트림을 만든 예제
        Stream<String> stringStream = Pattern.compile(", ").splitAsStream("Eric, Elena, Java");
        stringStream.forEach(s -> System.out.println("stringStream = " + s));

        // 파일 스트림
        try {
            //파일생성
            String filePath = "/Users/andy/test.txt";
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // BufferedWriter생성
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            //파일에 쓰기
            writer.write("test1");
            writer.newLine();
            writer.write("test2");
            writer.newLine();
            //버퍼및 스트림 정리
            writer.flush();
            writer.close();

            //파일의 라인데이터로 스트림 생성
            Stream<String> lineStream = Files.lines(Paths.get("/Users/andy/test.txt"), Charset.forName("UTF-8"));
            lineStream.forEach(s -> System.out.println("lineStream = " + s));
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("tv",10));
        productList.add(new Product("radio",5));
        for (Product p : productList) {
            System.out.println(p.getName() +" : "+ p.getAmount());
        }

        /**
         * 병렬 스트림
         */
        //병렬 스트림생성
        Stream<Product> parallelStream = productList.parallelStream();

        //병렬여부 확인
        boolean isParallel = parallelStream.isParallel();
        System.out.println("isParallel = " + isParallel);

        boolean isMany = parallelStream
                .map(product -> product.getAmount() * 10)
                .anyMatch(amount -> amount >  200);

        System.out.println("isMany = " + isMany);

        //배열을 이용하여 병렬스트림 생성
        Stream<String> arrStream = Arrays.stream(arr).parallel();

        //컬렉션과 배열이 아닌 경우 다음과 같이 parallel 메소드를 이용하여 처리
        IntStream intStream1 = IntStream.range(1, 150).parallel();
        boolean isParalllel1 = intStream1.isParallel();
        System.out.println("isParalllel1 = " + isParalllel1);

        // 병렬을 시퀀셜로 돌릴때
        IntStream intStream2 = intStream1.sequential();
        boolean isParallel2 = intStream2.isParallel();
        System.out.println("isParallel2 = " + isParallel2);

        // 스트림 연결하기

        Stream<String> stream1 = Stream.of("Java", "Scala", "Groovy");
        Stream<String> stream2 = Stream.of("Python", "Go", "Swift");
        Stream<String> concat = Stream.concat(stream1, stream2);
        concat.forEach(s -> System.out.println("concat = " + s));

        /**
         * 가공하기
         */

        List<String> names = Arrays.asList("Eric", "Elena", "Java");
        // 필터링
        // Stream<T> filter(Predicate<? super T> predicate);

        // 예제1) "a"가 포함된 단어를 찾아라
        Stream<String> stream = names.stream()
                .filter(name -> name.contains("a"));
        stream.forEach(s -> System.out.println("name = " + s));

        /**
         * Mapping
         * map은 스트림 내요소들을 하나씩 특정 값으로 변환함
         * <R> Stream</R> map(Function<? spuer T, ? extends R> mapper);
         */

        Stream<String> nameStream = names.stream()
                .map(String::toUpperCase);
        nameStream.forEach(s -> System.out.println("nameStrea = " + s));

        // ProductList의 개체들의 amount를 반환
        Stream<Integer> productStream = productList.stream()
                .map(Product::getAmount);
        productStream.forEach(s -> System.out.println("productStream = " + s));

        /**
         * 중첩제거 flatMap
         * <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
         */

        //flatmap은 중첩구조를 한단계 제거하고 단일 컬렉션으로 만들어주는 역할
        List<List<String>> list1 = Arrays.asList(Arrays.asList("a"), Arrays.asList("b"));
        List<String> flatList = list1.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        for (String s : flatList) {
            System.out.println("flatlist item = " + s);
        }

        //객체에 적용
        List<Student> students = Arrays.asList(new Student(50,100,90), new Student(30, 40, 50));
        //전체학생들의 평균점수 첫번째학생 평균 80 두번째 학생 40 두학생의 평균 60
        //학생객체를 가진 스트림에서 국영수점수를 뽑아 새로운 스트림을 만들어 평균을 구하는 코드
        students.stream()
                .flatMapToInt(student ->
                        IntStream.of(student.getKor(),
                                student.getEng(),
                                student.getMath()))
                .average().ifPresent(avg -> System.out.println("All average = " + Math.round(avg * 10)/10.0));
        //전체학생들의 국어 점수 평균
        students.stream()
                .flatMapToInt(student ->
                        IntStream.of(student.getKor()))
                .average().ifPresent(avg -> System.out.println("kor average = " + Math.round(avg * 10)/10.0));

        /******************
         * 정렬
         *****************/
        List<Integer> sortedList = IntStream.of(14, 11,20, 39, 23)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
        for (Integer i : sortedList) {
            System.out.println("sortedList i = " + i);
        }

        List<String> lang  = Arrays.asList("Java", "Scala", "Groovy", "Python", "Go", "Swift");

        List<String> sortedLang = lang.stream()
                .sorted()
                .collect(Collectors.toList());
        for (String s : sortedLang) {
            System.out.println("sortedLang s = " + s);
        }
        lang.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()).forEach(s -> System.out.println("sortedLang reverse = " + s));

        //문자열 길이로 정렬
        lang.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList()).forEach(s -> System.out.println("length = " + s));
        lang.stream()
                .sorted((s1,s2) -> s2.length() - s1.length())
                .collect(Collectors.toList()).forEach(s -> System.out.println("length reverse = " + s));

        /**
         * Iterating
         */
        // 따라서 스트림 내 요소들 각각에 특정 작업을 수행할 뿐 결과에 영향을 미치지 않습니다. 다음처럼 작업을 처리하는 중간에 결과를 확인해볼 때 사용할 수 있습니다.
        int sum = IntStream.of(1,3,5,7,9)
                .peek(System.out::println)
                .sum();


        /***
         * 결과만들기
         */
        long count = IntStream.of(1,3,5,7,9).count();
        long sum1 = LongStream.of(1,3,5,7,9).sum();
        //스트림이 비어있는 경우 OptionalInt사용
        OptionalInt min = IntStream.of(1, 3, 5, 7, 9).min();
        OptionalInt max = IntStream.of(1, 3, 5, 7, 9).max();

        DoubleStream.of(1.1,2.2,3.3,4.4,5.5)
                .average()
                .ifPresent(System.out::println);
    }


    //비어있는 스트림 생성예시
    // 요소가 없을때 null대신 사용가능
    public static Stream<String> streamOf(List<String> list) {
        return list == null || list.isEmpty() ? Stream.empty() : list.stream();
    }
}
