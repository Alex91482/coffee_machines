package org.example;

import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.concurrent.Flow.*;
import org.example.entity.SavedEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CoffeeSubscription implements Subscription{

    private static CoffeeSubscription instance;

    private CoffeeSubscription() {}

    public static CoffeeSubscription getInstance() {
        if(instance == null){
            instance = new CoffeeSubscription();
        }
        return instance;
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private final AtomicBoolean countFlag = new AtomicBoolean(false);
    private final AtomicBoolean flag = new AtomicBoolean(false);
    private final AtomicReference<SavedEvent> finishedCoffee = new AtomicReference<>();
    private final ArrayBlockingQueue<SavedEvent> myBlockingQueue = new ArrayBlockingQueue<>(1000, true);
    private final ConcurrentMap<String, CoffeeSubscriber> subscribers = new ConcurrentHashMap<>();

    public void subscribeToReceive(CoffeeSubscriber subscriber, SavedEvent savedEvent){
        System.out.println("Subscribe: " + Thread.currentThread().getName() + " " + savedEvent.coffeeEvent().getTypesCoffeeEvent());
        subscribers.put(Thread.currentThread().getName() ,subscriber);
        myBlockingQueue.add(savedEvent);
    }

    @Override
    public void request(long n) {
        try {
            if(!countFlag.getAndSet(true)) {        //если барьер был сломан
                countDownLatch = new CountDownLatch(1);     //устанавливаем новый барьер
            }
            createExtractInLoop();  //запуск переопроса очереди

            countDownLatch.await(); //потоки будут ожидать пока событие станет доступным

            if (!subscribers.containsKey(Thread.currentThread().getName())) {
                return;
            }
            if (subscribers.get(Thread.currentThread().getName()).getSaveEventId() == finishedCoffee.get().id()) {
                //как флаг используется id последнего события
                complete();
            } else {
                next(finishedCoffee.get());
            }

        }catch (Exception e){
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
            error(e);
        }
    }

    /**
     * При старте метода проверяется флаг того что цикл переопроса очереди запущен
     * если флаг false, перезаписываем его на true и запускаем цикл переопроса
     * в цикле сперва проверяем есть ли в очереди события если события есть заснуть на 3 секунды (эмитация приготовления кофе)
     * далее извлекаем из очереди событие и записываем в переменную finishedCoffee, сбрасываем барьер для ждущих потоков
     * устанавливаем флаг что барьер сброшен.
     * Барьер будет востановлен потоком который начнет запрашивать элемент из метода request.
     */
    private void createExtractInLoop() {
        if (!flag.getAndSet(true)) {
            executor.submit(() -> {
                try {
                    while (true) {
                        if (myBlockingQueue.isEmpty()) {            //остались ли еще события
                            flag.getAndSet(false);          //событий больше нет сбрасываем флаг
                            countDownLatch.countDown();             //сбрасывем барьер для ожидающих потоков
                            countFlag.getAndSet(false);
                            break;
                        }
                        Thread.sleep(3000);                    //эмитация готовки кофе
                        finishedCoffee.set(myBlockingQueue.poll());

                        System.out.println("Current event: " + finishedCoffee.get().coffeeEvent().getTypesCoffeeEvent());

                        countDownLatch.countDown();                 //сбрасываем барьер
                        countFlag.getAndSet(false);         //устанавливаем флаг что барьер сломан
                    }
                } catch (Exception e) {
                    System.err.println(LocalTime.now() + " Exception in loop!");
                    flag.getAndSet(false);
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public void cancel() {
        String name = Thread.currentThread().getName();
        var subscriber = subscribers.get(name);
        subscribers.remove(name);
        subscriber.onComplete();
    }

    private void error(Exception e){
        subscribers.get(Thread.currentThread().getName()).onError(e);
    }

    private void next(SavedEvent finishedCoffee){
        subscribers.get(Thread.currentThread().getName()).onNext(finishedCoffee);
    }

    private void complete(){
        String name = Thread.currentThread().getName();
        var subscriber = subscribers.get(name);
        subscribers.remove(name);
        subscriber.onComplete();
    }

    public void stopExecutor(){
        executor.shutdown();
    }
}
