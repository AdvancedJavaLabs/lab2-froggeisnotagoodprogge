import analyzers.NameAnonymizer;
import analyzers.SentenceSorter;
import analyzers.SentimentAnalyzer;
import analyzers.WordCounter;
import analyzers.WordFrequency;
import com.rabbitmq.client.*;
import entity.Counters;
import entity.ResultMessage;
import entity.TaskMessage;
import utils.JsonUtils;

import java.util.List;
import java.util.Map;


public class WorkerApp {

    public static void start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RabbitConfig.HOST);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(RabbitConfig.TASKS_QUEUE, true, false, false, null);
            channel.exchangeDeclare(RabbitConfig.RESULTS_EXCHANGE, BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(RabbitConfig.RESULTS_QUEUE, true, false, false, null);
            channel.queueBind(
                    RabbitConfig.RESULTS_QUEUE,
                    RabbitConfig.RESULTS_EXCHANGE,
                    RabbitConfig.RESULTS_ROUTING_KEY
            );
            channel.basicQos(1);

            WordCounter wc = new WordCounter();
            WordFrequency wf = new WordFrequency();
            SentimentAnalyzer sa = new SentimentAnalyzer();
            NameAnonymizer na = new NameAnonymizer();
            SentenceSorter ss = new SentenceSorter();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                TaskMessage task = JsonUtils.fromJsonBytes(delivery.getBody(), TaskMessage.class);

                long wordCount = wc.count(task.text);
                Map<String, Integer> wordFreq = wf.countFrequency(task.text);
                int sentiment = sa.analyze(task.text);
                String anonymized = na.anonymize(task.text);
                List<String> sortedSentences = ss.sortByLength(task.text);

                Counters counters = new Counters(wordCount, wordFreq, sentiment, anonymized, sortedSentences);

                ResultMessage result = new ResultMessage(
                        task.jobId,
                        task.sectionId,
                        task.totalSections,
                        counters
                );

                channel.basicPublish(
                        RabbitConfig.RESULTS_EXCHANGE,
                        RabbitConfig.RESULTS_ROUTING_KEY,
                        null,
                        JsonUtils.toJsonBytes(result)
                );

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                System.out.println(Thread.currentThread().getName() + " processed section " + task.sectionId);
            };

            channel.basicConsume(
                    RabbitConfig.TASKS_QUEUE,
                    false,  // autoAck = false
                    deliverCallback,
                    consumerTag -> {}
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}