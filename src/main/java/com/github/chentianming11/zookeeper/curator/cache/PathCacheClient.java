package com.github.chentianming11.zookeeper.curator.cache;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author 陈添明
 * @date 2018/12/14
 */
public class PathCacheClient {


    private final static String connectionInfo = "127.0.0.1:2181";

    private static final String PATH = "/example/pathCache";

    public static void main(String[] args) throws Exception {
        // 创建curator客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectionInfo)
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(50000)
                .retryPolicy(retryPolicy)
                .namespace("base")
                .build();
        client.start();

        // 增删改查
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(PATH + "/a", "a".getBytes());

        Thread.sleep(3_000);

        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(PATH + "/b", "b".getBytes());

        Thread.sleep(3_000);

        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(PATH + "/c", "c".getBytes());

        Thread.sleep(3_000);

        client.setData().forPath(PATH + "/c", "my".getBytes());

        Thread.sleep(3_000);

        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(PATH+"/b");

    }
}
