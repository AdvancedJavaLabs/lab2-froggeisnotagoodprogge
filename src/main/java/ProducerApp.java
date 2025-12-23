import com.rabbitmq.client.*;
import entity.TaskMessage;
import utils.JsonUtils;
import utils.TextSplitter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProducerApp {

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("input.txt");
        byte[] encoded = Files.readAllBytes(path);
        String text = new String(encoded, StandardCharsets.UTF_8);

        System.out.println(text);
        TextSplitter splitter = new TextSplitter(50);
        List<String> parts = splitter.split(text);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitConfig.HOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(RabbitConfig.TASKS_EXCHANGE, BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(RabbitConfig.TASKS_QUEUE, true, false, false, null);
            channel.queueBind(
                    RabbitConfig.TASKS_QUEUE,
                    RabbitConfig.TASKS_EXCHANGE,
                    RabbitConfig.TASKS_ROUTING_KEY
            );

            String jobId = "job-1";
            int total = parts.size();

            for (int i = 0; i < parts.size(); i++) {
                TaskMessage msg = new TaskMessage(jobId, i, total, parts.get(i));

                channel.basicPublish(
                        RabbitConfig.TASKS_EXCHANGE,
                        RabbitConfig.TASKS_ROUTING_KEY,
                        null,
                        JsonUtils.toJsonBytes(msg)
                );

                System.out.println("Sent section " + i);
            }
        }
    }
}