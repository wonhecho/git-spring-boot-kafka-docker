package net.cho.api.quiz.example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import net.cho.api.config.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import scala.Int;

import java.sql.SQLOutput;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FluxAndMonoTest {

    @Mock
    CustomException customExceptionMono;

    @Mock
    CustomException customExceptionFlux;
    @BeforeEach
    void setUp(){
        customExceptionFlux = new CustomException("Flux");
        customExceptionMono = new CustomException("Mono");
    }
    @DisplayName("Flux just() Sample()")
    @Test
    void main(){
        List<String> names = new ArrayList<>();
        Flux<String> flux = Flux.just("김구", "윤봉길", "유관순").log();
        flux.subscribe(names::add); // json코드로 굳혀야 한다
        assertThat(names, is(equalTo(Arrays.asList("김구", "윤봉길", "유관순"))));
    }
    @DisplayName("Flux range() Sample()")
    @Test
    void rangeTest(){
        List<Integer> list = new ArrayList<>();
        Flux<Integer> flux = Flux.range(1,5).log();
        flux.subscribe(list::add);
        assertThat(list.size(), is(5));
        assertThat(list.get(2), is(3));
        assertThat(list.get(4), not(6));
    }
    @DisplayName("Flux fromArray() Sample()")
    @Test
    void fromArrayTest(){
        List<String> list = new ArrayList<>();
        Flux<String> flux = Flux.fromArray(new String[]{"김구", "윤봉길", "유관순"}).log();
        flux.subscribe(list::add); // json코드로 굳혀야 한다
        assertThat(list, is(equalTo(Arrays.asList("김구", "윤봉길", "유관순"))));
    }
    @DisplayName("Flux iterable() Sample()")
    @Test
    void fromIterableTest(){
        List<String> list = new ArrayList<>();
        Flux<String> flux = Flux.fromIterable(Arrays.asList("김구", "윤봉길", "유관순")).log();
        flux.subscribe(list::add); // json코드로 굳혀야 한다
        assertThat(list, is(equalTo(Arrays.asList("김구", "윤봉길", "유관순"))));
    }
    @DisplayName("Flux fromStream() Sample()")
    @Test
    void fromStreamTest(){
        List<String> list = new ArrayList<>();
        Flux<String> flux = Flux.fromStream(Arrays.asList("김구", "윤봉길", "유관순").stream()).log();
        flux.subscribe(list::add); // json코드로 굳혀야 한다
        assertThat(list, is(equalTo(Arrays.asList("김구", "윤봉길", "유관순"))));
    }
    @DisplayName("Flux generator() Sample()")
    @Test
    void generatorTest(){
        Flux<String> flux = Flux.generate(
                () -> {
                    return 0;
                },
                (state,sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10){
                        sink.complete();
                    }
                    return state + 1;
            }
        );
        flux.subscribe(System.out::println);
    }

    @DisplayName("Flux create() Sample()")
    @Test
    void createTest() {
        /**
         * Flux.create()와 배압
         * Subscriber로부터 요청이 왔을 때(FluxSink#onRequest) 데이터를 전송하거나(pull 방식)
         * Subscriber의 요청에 상관없이 데이터를 전송하거나(push 방식)
         * 두 방식 모두 Subscriber가 요청한 개수보다 더 많은 데이터를 발생할 수 있다.
         * 이 코드는 Subscriber가 요청한 개수보다 3개 데이터를 더 발생한다. 이 경우 어떻게 될까?
         * 기본적으로 Flux.create()로 생성한 Flux는 초과로 발생한 데이터를 버퍼에 보관한다.
         * 버퍼에 보관된 데이터는 다음에 Subscriber가 데이터를 요청할 때 전달된다.
         * 요청보다 발생한 데이터가 많을 때 선택할 수 있는 처리 방식은 다음과 같다.
         * IGNORE : Subscriber의 요청 무시하고 발생(Subscriber의 큐가 다 차면 IllegalStateException 발생)
         * ERROR : 익셉션(IllegalStateException) 발생
         * DROP : Subscriber가 데이터를 받을 준비가 안 되어 있으면 데이터 발생 누락
         * LATEST : 마지막 신호만 Subscriber에 전달
         * BUFFER : 버퍼에 저장했다가 Subscriber 요청시 전달. 버퍼 제한이 없으므로 OutOfMemoryError 발생 가능
         * Flux.create()의 두 번째 인자로 처리 방식을 전달하면 된다.
         * */
        Flux<Integer> flux = Flux.create((FluxSink<Integer> sink)->{
            sink.onRequest(request -> {
                for( int i=1; i<request + 3; i++){  //subscriber가 요청한 것보다 3개 더 발생
                    sink.next(i);
                }
            });
        });
        flux.subscribe(System.out::println);

    }
    @DisplayName("Flux empty() Sample()")
    @Test
    void emptyTest() {
        List<String> list = new ArrayList<>();
        Flux<String> flux = Flux.empty();
        flux.subscribe(list::add);
        assertThat(list.size(),is(0));
    }

    @DisplayName("Mono just() Sample()")
    @Test
    void monoJustTest() {

        /**
         * Reactive Stream 에서는 Data, Event, Signal 중에서 Signal 을 사용한다.
         * onNext, onComplete, onError
         * */

        List<Signal<Integer>> list = new ArrayList<>(4);
        final Integer[] result = new Integer[1];
        Mono<Integer> mono = Mono.just(1).log()
                        .doOnEach(i -> {
                            list.add(i);
                            System.out.println("Signal : " + i);
                        });
        mono.subscribe(i-> result[0] = i);
        assertThat(list.size(),is(2));
        assertThat(list.get(0).getType().name(),is(equalTo("ON_NEXT")));
        assertThat(list.get(1).getType().name(),is(equalTo("ON_COMPLETE")));
        assertThat(result[0].intValue(),is(1));

    }
    @DisplayName("Mono empty() Sample()")
    @Test
    void MonoemptyTest() {
        Mono<String> result = Mono.empty();
        assertThat(result.block(),is(nullValue()));
    }
    @DisplayName("Mono just() sample")
    @Test
    void MonoJustTest2(){
        System.out.println(" Empty Mono");
        Mono.empty().subscribe(System.out::println);
        System.out.println("--------Mono.just()--------------");
        Mono.just("Java")
                .map(item -> "Mono items :" + item)
                .subscribe(System.out::println);
        System.out.println("------Empty Flux -------");
        Flux.empty().subscribe(System.out::println);
        System.out.println("----------Flux.just()---------");
        Flux.just("Java", "Oracle", "python")
                .map(item -> item.toUpperCase())
                .subscribe(System.out::println);
    }
    @DisplayName("Mono Flux error() sample")
    @Test
    void ErrorTest(){
        Mono.error(customExceptionMono)
                .doOnError(e -> System.out.println("Mono inside doOnError "))
                .subscribe(System.out::println);
        Flux.error(customExceptionFlux)
                .doOnError(e -> System.out.println("Flux inside doOnError "))
                .subscribe(System.out::println);
    }
    @DisplayName("Flux() practice")
    @Test
    void main2(){
        List<String> list = new ArrayList<>();
        Flux<String> flux = Flux.just("김구","윤봉길","유관순");
        flux.subscribe(list::add);
        flux.subscribe(System.out::println);
        assertThat(list,is(equalTo(Arrays.asList("김구","윤봉길","유관순"))));

    }

}
