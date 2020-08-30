package app.web.pavelk.microservices1.сapture1.wikipedia;

import app.web.pavelk.microservices1.сapture1.exception.GrabberException;
import app.web.pavelk.microservices1.сapture1.model.Article;
import app.web.pavelk.microservices1.сapture1.service.WikipediaDAO;
import app.web.pavelk.microservices1.сapture1.util.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.*;

//            Callable подобен Runnable, но с возвратом значения. Интерфейс Callable является параметризованным типом
//Future хранит результат асинхронного вычисления
//Метод isDone() возвращает false, если вычисление продолжается, и true - если оно завершено.
//Вызов первого метода get() устанавливает блокировку до тех пор, пока не завершится вычисление.
//Вы можете прервать вычисление,вызвав метод Cancel().
//isCancelled();
//            LockSupport             park()
//            Отключает текущий поток в целях планирования потоков, если разрешение не доступно.!!!
//            FutureTask task = new FutureTask(myComputation);
//            Thread t = ne Thread(task);  // это Runnable
//            t.start();
//            Класс-оболочка FutureTask представляет собой удобный механизм для превращения
//            Callable одновременно в Future и Runnable, реализуя оба интерфейса.
//            Каждый вызов get() устанавливает блокировку до тех пор, пока не будет готов результат.
//            Конечно, потоки работают параллельно, так что есть шанс, что результаты будут готовы почти одновременно.
//            CompletableFuture<String> completed;
// То есть при создании стрима в Java стрим не запускается сразу, а ждёт, когда из него захотят значение.
// А вот CompletableFuture запускает цепочку на выполнение сразу, не дожидаясь того,
//            CompletableFuture.runAsync(task)
//                    .thenApply((v) -> longValue.get())
//                    .thenApply(dateConverter)
//                    .thenAccept(printer);


@Slf4j
@Data
public class WikipediaGrabber1 {
    private ExecutorService executorService;
    private int maxLevel = 1;
    private int maxDelay = 10;
    private int countThreads = 4;
    private WikipediaDAO wikipediaDAO = new WikipediaDAO();

    private void init() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("grabber-%d")
                .setDaemon(true)
                .build();
        executorService = Executors.newFixedThreadPool(countThreads, threadFactory);
        log.debug("countThreads: {} maxDelay: {}", this.countThreads, this.maxDelay + "  this.maxLevel = " + this.maxLevel);
    }


    public void start(String url) throws GrabberException {
        init();

        try {
            Future<Article> future = executorService.submit(new ArticleWorker2(this, url));
            //start recruit
            grab(future.get(), 0);
        } catch (InterruptedException | ExecutionException ie) {
            log.error("", ie);
        }
        executorService.shutdown();//выключение
    }

    //рекрусивные метод загрузки в пул потоков
    private void grab(Article parent, int currentLevel) throws InterruptedException, ExecutionException {
        if (currentLevel < getMaxLevel()) {//проверка левала вложености
            currentLevel++;
            Collection<Future<Article>> results = new LinkedList<>();

            int z = 1;
            for (Article r : parent.getRelations()) { // all url
                results.add(executorService.submit(new ArticleWorker2(this, r.getUrl())));
                System.out.println("grab results ----- " + results.size());
                z++;
            }
            //System.out.println("отправлено " + z + " задачь с результаом футура теперь гет поставит парковый лок");
            log.info("отправлено " + z + " задачь с результаом футура теперь гет поставит парковый лок");

            int t = 1;
            for (Future<Article> f : results) {
                long time = System.currentTimeMillis();
                //System.out.println(f.isDone() + " поток выполняеться !!!!!!! ");
                log.info("Future: {}", f.isDone() + " если false то еще не сделано !!!!!!! get park lock ");
                grab(f.get(), currentLevel); // get to method start or not
                //System.out.println(t++ + " thread is === " + (System.currentTimeMillis() - time) + " ms");
                log.info(t++ + " thread ПОЛУЧИЛ стаью за : {}", +(System.currentTimeMillis() - time) + " ms");
            }
            //System.out.println("получено " + t + " статей вроде по сылкам с первой странице включительно ");
            log.info("получено: {}", t + " статей вроде по сылкам с первой рекрусивно без первой ");

        } else {
            System.out.println("max level = " + getMaxLevel() + " get call level " + currentLevel + " not get!");
        }

    }


}
