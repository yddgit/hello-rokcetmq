package com.my.project;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * Pull消费者示例
 * @author yang
 *
 */
public class PullConsumer {

	private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<MessageQueue, Long>();

	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("consumerGroup");
		consumer.setNamesrvAddr("192.168.19.128:9876");
		consumer.start();

		//拉取指定Topic的消息
		Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("orders");
		System.out.println(mqs.size());

		int i = 0;
		for (MessageQueue mq : mqs) {
			System.out.println("Consume from queue: " + i++ + " " + mq);

			//PullResult pullResult = consumer.pull(mq, null, getMessageQueueOffset(mq), 32);
			PullResult pullResult = consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
			pullResult.getMsgFoundList().forEach((m) -> {
				System.out.println(new String(m.getBody()));
			});

			System.out.println(mq);

			putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
		}

		consumer.shutdown();
	}

	private static long getMessageQueueOffset(MessageQueue mq) {
		Long offset = OFFSE_TABLE.get(mq);
		if (offset != null)
			return offset;

		return 0;
	}

	private static void putMessageQueueOffset(MessageQueue mq, long offset) {
		OFFSE_TABLE.put(mq, offset);
	}
}
