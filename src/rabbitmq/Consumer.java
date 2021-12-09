package rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {

	// Logger
	ConnectionFactory factory = new ConnectionFactory();
	private static final String QUEUE = "stock-transactions";
	private String MESSAGE = "";

	public void consumeMessage(int nrMessages) throws IOException, TimeoutException {

		try (Connection connection = factory.newConnection()) {
			Channel channel = connection.createChannel();
			int nr = 1;

			channel.queueDeclare(QUEUE, false, false, false, null);

			while (nr < nrMessages) {
				DeliverCallback deliverCallback = (consumerTag, delivery) -> {
					this.MESSAGE = new String(delivery.getBody(), StandardCharsets.UTF_8);
					System.out.println("MESSAGE [" + delivery.hashCode() + "]: '" + MESSAGE + "'");
				};

				CancelCallback cancelCallback = System.out::println;

				channel.basicConsume(QUEUE, true, deliverCallback, cancelCallback);
				nr += 1;
			}
		}
	}
}
