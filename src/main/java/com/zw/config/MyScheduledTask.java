package com.zw.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

//@Component
@Slf4j
public class MyScheduledTask implements ApplicationListener<ApplicationReadyEvent> {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread workerThread;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 确保只启动一次
        if (running.compareAndSet(false, true)) {
            startLoop();
        }
    }

    private void startLoop() {
        workerThread = new Thread(() -> {
            log.info("✅ todo while 循环线程已启动（Spring Boot 已就绪）");
            while (running.get()) {
                try {
                    log.info("【开始执行】todo 操作");

                    // ========== 你的 todo 逻辑放这里 ==========
                    // 例如：处理数据、调用 API、打印信息等
                    // =======================================

                    log.info("【执行完成】todo 操作");

                    // 每次执行完暂停 3 秒
                    Thread.sleep(300000);

                } catch (InterruptedException e) {
                    log.warn("todo 线程被中断，准备退出");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("❌ todo 操作异常", e);
                    // 可选择继续循环 or break
                }
            }
            log.info("⏹️ todo while 循环线程已停止");
        }, "todo-loop-thread");

        workerThread.setDaemon(true); // 守护线程，主程序退出时自动结束
        workerThread.start();
    }

    // 提供一个方法用于外部（如 Actuator 或测试）手动停止
    public void stop() {
        if (running.compareAndSet(true, false)) {
            if (workerThread != null) {
                workerThread.interrupt();
            }
        }
    }
}
