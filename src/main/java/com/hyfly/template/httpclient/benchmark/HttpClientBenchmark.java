package com.hyfly.template.httpclient.benchmark;

import com.hyfly.template.httpclient.HttpClient;
import com.hyfly.template.httpclient.factory.HttpClientFactory;
import com.hyfly.template.httpclient.model.HttpRestResult;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP客户端性能基准测试
 * 比较 Apache HttpClient 和 OkHttp 的性能差异
 */
@Slf4j
public class HttpClientBenchmark {

    private static final String TEST_URL = "https://httpbin.org/get";
    private static final int TOTAL_REQUESTS = 1000;
    private static final int CONCURRENT_THREADS = 50;

    public static void main(String[] args) throws Exception {
        log.info("开始 HTTP 客户端性能基准测试");
        log.info("测试URL: {}", TEST_URL);
        log.info("总请求数: {}", TOTAL_REQUESTS);
        log.info("并发线程数: {}", CONCURRENT_THREADS);
        log.info("========================================");

        // 测试 Apache HttpClient
        testApacheHttpClient();

        // 等待一段时间避免测试相互影响
        Thread.sleep(2000);

        // 测试 OkHttp（如果实现了的话）
        // testOkHttpClient();

        log.info("基准测试完成");
    }

    /**
     * 测试 Apache HttpClient 性能
     */
    private static void testApacheHttpClient() throws Exception {
        log.info("测试 Apache HttpClient...");

        HttpClient client = new HttpClient(TEST_URL, HttpClientFactory.getDefaultRestTemplate());

        BenchmarkResult result = runBenchmark("Apache HttpClient", () -> {
            try {
                HttpRestResult<String> response = client.get("", String.class);
                return response.isSuccess();
            } catch (Exception e) {
                log.error("请求失败", e);
                return false;
            }
        });

        printBenchmarkResult(result);
    }

    /**
     * 测试 OkHttp 性能（示例 - 需要实现 OkHttp 客户端）
     */
    private static void testOkHttpClient() throws Exception {
        log.info("测试 OkHttp...");

        // 这里需要实现 OkHttp 版本的客户端
        // HttpClient client = new HttpClient(TEST_URL, HttpClientFactory.getOkHttpRestTemplate());

        log.info("OkHttp 实现待完成...");
    }

    /**
     * 运行基准测试
     */
    private static BenchmarkResult runBenchmark(String name, Callable<Boolean> task) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        // 提交所有任务
        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            executor.submit(() -> {
                try {
                    if (task.call()) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有任务完成
        latch.await();
        long endTime = System.currentTimeMillis();

        executor.shutdown();

        return new BenchmarkResult(
                name,
                TOTAL_REQUESTS,
                successCount.get(),
                failureCount.get(),
                endTime - startTime
        );
    }

    /**
     * 打印基准测试结果
     */
    private static void printBenchmarkResult(BenchmarkResult result) {
        log.info("========== {} 测试结果 ==========", result.name);
        log.info("总请求数: {}", result.totalRequests);
        log.info("成功请求数: {}", result.successCount);
        log.info("失败请求数: {}", result.failureCount);
        log.info("总耗时: {} ms", result.totalTime);
        log.info("平均响应时间: {} ms", (double) result.totalTime / result.totalRequests);
        log.info("QPS: {}", (double) result.totalRequests / result.totalTime * 1000);
        log.info("成功率: {}%", (double) result.successCount / result.totalRequests * 100);
        log.info("=====================================");
    }

    /**
     * 基准测试结果
     */
    static class BenchmarkResult {
        final String name;
        final int totalRequests;
        final int successCount;
        final int failureCount;
        final long totalTime;

        BenchmarkResult(String name, int totalRequests, int successCount, int failureCount, long totalTime) {
            this.name = name;
            this.totalRequests = totalRequests;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.totalTime = totalTime;
        }
    }
}
