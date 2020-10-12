# Spring JPA - Sleuth deadlock

## Run the project

```
mvn -P localdev clean spring-boot:run
```

## Issue
With sleuth dependency enabled, the application blocks at startup, a thread dump shows:

```
"scheduling-1" #24 prio=5 os_prio=0 cpu=1188,63ms elapsed=14,39s tid=0x00007fb12d5e4800 nid=0x7ffd waiting for monitor entry  [0x00007fb11e5f8000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.getSingletonFactoryBeanForTypeCheck(AbstractAutowireCapableBeanFactory.java:986)
        - waiting to lock <0x0000000708c8d250> (a java.util.concurrent.ConcurrentHashMap)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.getTypeForFactoryBean(AbstractAutowireCapableBeanFactory.java:884)
        at org.springframework.beans.factory.support.AbstractBeanFactory.isTypeMatch(AbstractBeanFactory.java:619)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.doGetBeanNamesForType(DefaultListableBeanFactory.java:536)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForType(DefaultListableBeanFactory.java:503)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForType(DefaultListableBeanFactory.java:480)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForType(DefaultListableBeanFactory.java:473)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveNamedBean(DefaultListableBeanFactory.java:1159)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveBean(DefaultListableBeanFactory.java:420)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:349)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:342)
        at org.springframework.cloud.sleuth.instrument.async.LazyTraceThreadPoolTaskScheduler.spanNamer(LazyTraceThreadPoolTaskScheduler.java:428)
        at org.springframework.cloud.sleuth.instrument.async.LazyTraceThreadPoolTaskScheduler.execute(LazyTraceThreadPoolTaskScheduler.java:187)
        at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(java.base@11.0.2/Native Method)
        at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(java.base@11.0.2/NativeMethodAccessorImpl.java:62)
        at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(java.base@11.0.2/DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(java.base@11.0.2/Method.java:566)
        at org.springframework.cloud.sleuth.instrument.async.ExecutorMethodInterceptor.invoke(ExecutorBeanPostProcessor.java:356)
        at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
        at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)
        at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)
        at org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler$$EnhancerBySpringCGLIB$$61bef91f.execute(<generated>)
        at org.springframework.boot.autoconfigure.orm.jpa.DataSourceInitializedPublisher$DataSourceSchemaCreatedPublisher.postProcessEntityManagerFactory(DataSourceInitializedPublisher.java:199)
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:412)
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean$$Lambda$841/0x00000008006cf840.call(Unknown Source)
        at org.springframework.cloud.sleuth.instrument.async.TraceCallable.call(TraceCallable.java:71)
        at java.util.concurrent.FutureTask.run(java.base@11.0.2/FutureTask.java:264)
        at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(java.base@11.0.2/ScheduledThreadPoolExecutor.java:304)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(java.base@11.0.2/ThreadPoolExecutor.java:1128)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(java.base@11.0.2/ThreadPoolExecutor.java:628)
        at java.lang.Thread.run(java.base@11.0.2/Thread.java:834)

   Locked ownable synchronizers:
        - <0x00000007172f2120> (a java.util.concurrent.ThreadPoolExecutor$Worker)
```