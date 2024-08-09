package org.carl.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.time.Duration;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class TestReactive {
  /* Uni 创建 */
  @Test
  public void testUni() {
    Uni<String> hello = Uni.createFrom().item("hello").onItem().transform(item -> item);
    hello.subscribe().with(System.out::println);
  }

  /* item 数据进行转换 */
  @Test
  public void testTransform() {
    Uni<String> hello =
        Uni.createFrom().item("hello").onItem().transform(item -> item + " " + "word");
    hello.subscribe().with(System.out::println);
    Uni<ArrayList<String>> transform =
        hello
            .onItem()
            .transform(
                item ->
                    new ArrayList<String>() {
                      {
                        add("hello");
                        add("word");
                      }
                    });
    transform.subscribe().with(System.out::println);
  }

  /* 创建异常抛出异常*/
  @Test
  public void testExceptionHandling() {
    Uni<String> hello = Uni.createFrom().failure(new Throwable("err"));
    hello.subscribe().with(System.out::println);
  }

  /* invoke 会在数据处理完执行，注意次方法为阻塞式的*/
  @Test
  public void testInvoke() {
    Uni<String> hello =
        Uni.createFrom()
            .item("hello")
            .invoke(
                o -> {
                  System.out.println(o);
                });
    hello.subscribe().with(System.out::println);
  }

  /* call 数据处理完执行，异步执行*/
  @Test
  public void testCall() {
    Uni<String> hello =
        Uni.createFrom()
            .item("hello")
            .call(
                item -> {
                  System.out.println(item);
                  return Uni.createFrom().item(item + " word");
                });
    hello.subscribe().with(System.out::println);
  }

  /*item 覆盖*/
  @Test
  public void testTransformToUni() {
    Uni<String> hello = Uni.createFrom().item("hello");
    Uni<String> toUni = hello.onItem().transformToUni(item -> remoteService(item));
    toUni.subscribe().with(System.out::println);
    // or
    Uni<String> chain = hello.chain(item -> remoteService(item));
    chain.subscribe().with(System.out::println);

    // NOTE: 返回结果没有顺序
    Multi<String> multi = Multi.createFrom().items("a", "b", "c");

    Multi<String> merged = multi.onItem().transformToUniAndMerge(name -> remoteService(name));
    merged.subscribe().with(System.out::println);

    // NOTE: 有序返回
    Multi<String> concat = multi.onItem().transformToUniAndConcatenate(name -> remoteService(name));
    concat.subscribe().with(System.out::println);
  }

  /*uni to multi*/
  @Test
  public void testUniToMulti() {
    Uni<String> hello = Uni.createFrom().item("a");
    Multi<String> ofMulti =
        hello.onItem().transformToMulti(item -> Multi.createFrom().items(item, item));
    ofMulti.subscribe().with(System.out::println);
  }

  /*异常处理*/
  @Test
  public void testHandleException() {
    Uni<String> hello = Uni.createFrom().item("hello");
    hello =
        hello
            .onItem()
            .transformToUni(
                item -> {
                  if (Math.random() == 0.9) {
                    return Uni.createFrom().item("skip");
                  }
                  return Uni.createFrom().failure(new Throwable("err"));
                });
    Uni<String> handle1 = hello.onItem().transform(item -> item);
    handle1.subscribe().with(item -> System.out.println("异常默认是不进行处理直接抛到下一个流程"));

    Uni<String> handle2 =
        hello.onFailure().transform(e -> new RuntimeException("transform " + e.getMessage()));
    handle2.subscribe().with(item -> System.out.println("可以将异常转换为更详细的异常: " + item));

    Uni<String> handle3 = hello.onFailure().recoverWithItem("hay");
    handle3.subscribe().with(item -> System.out.println("直接覆盖异常: " + item));

    Uni<String> handle4 = hello.onFailure().recoverWithItem(e -> getFallback(e));
    handle4.subscribe().with(item -> System.out.println("根据抛出的异常进行覆盖: " + item));

    Uni<String> handle5 = hello.onFailure().retry().indefinitely();
    handle5.subscribe().with(item -> System.out.println("会不断重试，编码不当会进入死循环: " + item));

    Uni<String> handle6 = hello.onFailure().retry().atMost(5);
    handle6.subscribe().with(item -> System.out.println("最多重试5次: " + item));

    Uni<String> handle7 = hello.onFailure().retry().withBackOff(Duration.ofMillis(100)).atMost(3);
    handle7.subscribe().with(item -> System.out.println("每100毫秒进行一次重试直到重试三次: " + item));

    Uni<String> handle8 =
        hello
            .onFailure()
            .retry()
            .withBackOff(Duration.ofMillis(100), Duration.ofSeconds(1))
            .atMost(3);
    handle8
        .subscribe()
        .with(item -> System.out.println("第一次重试为100毫秒，后面重试时间会进行翻倍，最大重试时间为3次，直到重试三次: " + item));

    Uni<String> handle9 =
        hello.onFailure().retry().withBackOff(Duration.ofSeconds(1)).withJitter(0.2).atMost(10);
    handle9
        .subscribe()
        .with(item -> System.out.println("进行随机抖动时间进行重试，越接进1抖动幅度越大，重试10次结束: " + item));

    Uni<String> handle10 =
        hello
            .onFailure()
            .retry()
            .withBackOff(Duration.ofSeconds(1))
            .withJitter(0.2)
            .expireIn(1000L);
    handle10.subscribe().with(item -> System.out.println("每次进行随机抖动的重试，1000毫秒后结束重试: " + item));

    Uni<String> handle11 = hello.onFailure().retry().until(f -> "skip".equals(f.getMessage()));
    handle11.subscribe().with(item -> System.out.println("满足条件结束重试，编码不规范会死循环: " + item));

    // NOTE: 需要注意的是, 如果是数据流Multi, 那么当对数据流中的数据进行操作时引起异常, 然后使用另一个事件产生的数据流item来覆盖失败时,
    // 下游事件会在收到这个覆盖的item之后收到一个结束流信号, 而不会继续处理异常之后的item

    Multi<String> apples = Multi.createFrom().items("apple1", "apple2", "apple3");
    apples
        .onItem()
        .transformToUniAndConcatenate(
            apple -> {
              if (apple.equals("apple2")) {
                return Uni.createFrom().failure(new Exception("bad apple"));
              }
              return Uni.createFrom().item("hello-" + apple);
            })
        .onFailure()
        .recoverWithItem("backup apple")
        .subscribe()
        .with(System.out::println);

    // NOTE: 收到异常的时候直接通过.recoverWithCompletion()宣布数据流处理完毕, 此时下游事件不会收到异常信息, 只会接收到异常之前的item

    apples
        .onItem()
        .transformToUniAndConcatenate(
            apple -> {
              if (apple.equals("apple2")) {
                return Uni.createFrom().failure(new Exception("bad apple"));
              }
              return Uni.createFrom().item("hello-" + apple);
            })
        .onFailure()
        .recoverWithCompletion()
        .subscribe()
        .with(System.out::println);

    // NOTE: 使用.recoverWithMulti()会在遇到异常时直接切换到新的Multi
    Multi<String> m =
        apples.onFailure().recoverWithMulti(() -> Multi.createFrom().items("a", "b", "c"));
  }

  String getFallback(Throwable e) {
    return e.getMessage();
  }

  Uni<String> remoteService(String item) {
    return Uni.createFrom().item("hay " + item);
  }
}