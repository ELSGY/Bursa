package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {

	ConnectionFactory factory = new ConnectionFactory();
	private static final String QUEUE = "stock-transactions";

	public void sendMessage(int id_client, int nr_actiuni, int id_actiune, int id_vanzator) throws IOException, TimeoutException {

		try (Connection connection = factory.newConnection()) {
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE, false, false, false, null);

			String message = "Client: " + id_client +
							 " purchased: " + nr_actiuni +
							 " stocks, stock: " + id_actiune +
							 " from: " + id_vanzator;

			channel.basicPublish("",
								 QUEUE,
								 false,
								 null,
								 message.getBytes());
		}
	}
}
