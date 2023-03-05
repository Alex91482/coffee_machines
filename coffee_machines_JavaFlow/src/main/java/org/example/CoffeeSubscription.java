package org.example;

import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.concurrent.Flow.*;
import org.example.entity.SavedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class CoffeeSubscription implements Subscription{

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private final ArrayBlockingQueue<SavedEvent> myBlockingQueue = new ArrayBlockingQueue<>(1000, true);
    private SavedEvent finishedCoffee;
    private final AtomicBoolean flag = new AtomicBoolean(false);
    private final ConcurrentMap<String, CoffeeSubscriber> subscribers = new ConcurrentHashMap<>();


    public void subscribeToReceive(CoffeeSubscriber subscriber, SavedEvent savedEvent){
        System.out.println("Subscribe: " + Thread.currentThread().getName());
        subscribers.put(Thread.currentThread().getName() ,subscriber);
        myBlockingQueue.add(savedEvent);
    }

    @Override
    public void request(long n){
        try {
            if(countDownLatch.getCount() <= 0) {            //если барьер был сломан
                countDownLatch = new CountDownLatch(1);     //устанавливаем новый барьер
            }

            if (!flag.getAndSet(true)) {
                executor.submit(() -> {
                    try {
                        while (true) {
                            if (myBlockingQueue.isEmpty()) {            //остались ли еще события
                                flag.getAndSet(false);          //событий больше нет сбрасываем флаг
                                countDownLatch.countDown();             //сбрасывем барьер для ожидающих потоков
                                break;
                            }
                            Thread.sleep(3000);                    //эмитация готовки кофе
                            finishedCoffee = myBlockingQueue.poll();

                            System.out.println("Current event: " + finishedCoffee.coffeeEvent().getTypesCoffeeEvent());

                            countDownLatch.countDown();                 //сбрасываем барьер
                        }
                    } catch (Exception e) {
                        System.err.println(LocalTime.now() + " Exception in loop!");
                        flag.getAndSet(false);
                        throw new RuntimeException(e);
                    }
                });
            }

            countDownLatch.await(); //потоки будут ожидать пока событие станет доступным

            if(subscribers.get(Thread.currentThread().getName()).getHashCodeLastSaveEvent() == finishedCoffee.hashCode()){
                //как флаг используется хеш код последнего события
                complete();
            }else{
                next();
            }

        }catch (Exception e){
            error(e);
        }
    }

    @Override
    public void cancel() {
        String name = Thread.currentThread().getName();
        subscribers.get(name).onComplete();
        CompletableFuture.runAsync(() -> subscribers.remove(name));
    }

    private void error(Exception e){
        subscribers.get(Thread.currentThread().getName()).onError(e);
    }

    private void next(){
        subscribers.get(Thread.currentThread().getName()).onNext(finishedCoffee);
    }

    private void complete(){
        String name = Thread.currentThread().getName();
        subscribers.get(name).onComplete();
        CompletableFuture.runAsync(() -> subscribers.remove(name));
    }

    public void stopExecutor(){
        executor.shutdown();
    }
}
