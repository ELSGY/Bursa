package rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Consumer {

	// Logger
	ConnectionFactory factory = new ConnectionFactory();
	private static final String QUEUE = "stock-transactions";
	private final List<String> MESSAGE = new ArrayList<>();

	public synchronized void consumeMessage(int nrMessages) throws IOException, TimeoutException {

		try (Connection connection = factory.newConnection()) {
			Channel channel = connection.createChannel();
			int nr = 1;

			channel.queueDeclare(QUEUE, false, false, false, null);

			while (nr < nrMessages) {
				DeliverCallback deliverCallback = (consumerTag, delivery) -> {
					MESSAGE.add(new String(delivery.getBody(), StandardCharsets.UTF_8));
				};

				CancelCallback cancelCallback = System.out::println;

				channel.basicConsume(QUEUE, true, deliverCallback, cancelCallback);
				nr += 1;
			}

			for (int i = 0; i < MESSAGE.size(); i++) {
				System.out.println("MESSAGE [" + (i + 1) + "]: '" + MESSAGE.get(i) + "'");
			}

		}
	}
}
