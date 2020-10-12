package online.pelago.p4p.shipitinerary.config;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.security.SecurityUtil;

@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class AsyncConfiguration extends AsyncConfigurerSupport {

	private static final String ASYNC_EXECUTION_AUTH = "async execution, auth {}";
	private static final String ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS = "error invoking async thread. error details : {}";

	@Override
	@Bean("taskExecutor")
	@Primary
	public TaskExecutor getAsyncExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
			private static final long serialVersionUID = -5384052157204554819L;

			@Override
			public void execute(Runnable task) {
				ExecutorService executor = getThreadPoolExecutor();
				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
				try {
					super.execute(() -> {
						log.trace(ASYNC_EXECUTION_AUTH, a);
						try {
							SecurityUtil.setSecurityContext(a);
							task.run();
						} catch (Exception e) {
							log.error(ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS, e);
						} finally {
							SecurityContextHolder.clearContext();
						}

					});
				} catch (RejectedExecutionException ex) {
					throw new TaskRejectedException(rejectedMessageFor(task, executor), ex);
				}

			}

			@Override
			public void execute(Runnable task, long startTimeout) {
				ExecutorService executor = getThreadPoolExecutor();
				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
				try {
					super.execute(() -> {
						log.trace(ASYNC_EXECUTION_AUTH, a);
						try {
							SecurityUtil.setSecurityContext(a);
							task.run();
						} catch (Exception e) {
							log.error(ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS, e);
						} finally {
							SecurityContextHolder.clearContext();
						}

					}, startTimeout);
				} catch (RejectedExecutionException ex) {
					throw new TaskRejectedException(rejectedMessageFor(task, executor), ex);
				}
			}

//			@Override
//			public ListenableFuture<?> submitListenable(Runnable task) {
//				ExecutorService executor = getThreadPoolExecutor();
//				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
//				try {
//					return super.submitListenable(() -> {
//						log.trace(ASYNC_EXECUTION_AUTH, a);
//						try {
//							setSecurityContext(a);
//							task.run();
//						} catch (Exception e) {
//							log.error(ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS, e);
//						} finally {
//							SecurityContextHolder.clearContext();
//						}
//					});
//				} catch (RejectedExecutionException ex) {
//					throw new TaskRejectedException(rejectedMessageFor(task, executor), ex);
//				}
//			}
//
//			@Override
//			public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
//				ExecutorService executor = getThreadPoolExecutor();
//				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
//				
//				try {
//					return super.submitListenable(() -> {
//						log.trace(ASYNC_EXECUTION_AUTH, a);
//						try {
//							setSecurityContext(a);
//							return task.call();
//						} catch (Exception e) {
//							log.error(ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS, e);
//							return null;
//						} finally {
//							SecurityContextHolder.clearContext();
//						}
//					});
//				} catch (RejectedExecutionException ex) {
//					throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
//				}
//			}

			@Override
			public Future<?> submit(Runnable task) {
				ExecutorService executor = getThreadPoolExecutor();
				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
				try {
					return super.submit(() -> {
						log.trace(ASYNC_EXECUTION_AUTH, a);
						try {
							SecurityUtil.setSecurityContext(a);
							task.run();
						} catch (Exception e) {
							log.error(ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS, e);
						} finally {
							SecurityContextHolder.clearContext();
						}
					});
				} catch (RejectedExecutionException ex) {
					throw new TaskRejectedException(rejectedMessageFor(task, executor), ex);
				}
			}

			@Override
			public <T> Future<T> submit(Callable<T> task) {
				ExecutorService executor = getThreadPoolExecutor();
				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
				try {
					return executor.submit(() -> {
						log.trace(ASYNC_EXECUTION_AUTH, a);
						try {
							SecurityUtil.setSecurityContext(a);
							return task.call();
						} catch (Exception e) {
							log.error(ERROR_INVOKING_ASYNC_THREAD_ERROR_DETAILS, e);
							return null;
						} finally {
							SecurityContextHolder.clearContext();
						}
					});
				} catch (RejectedExecutionException ex) {
					throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
				}
			}
		};
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(16);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Async-");
		executor.initialize();
		return executor;
	}

	private String rejectedMessageFor(Runnable task, ExecutorService executor) {
		return "Executor [" + executor + "] did not accept task: " + task;
	}

}
