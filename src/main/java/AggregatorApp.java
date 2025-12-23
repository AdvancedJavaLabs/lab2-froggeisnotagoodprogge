import com.rabbitmq.client.*;
import entity.Counters;
import entity.ResultMessage;
import utils.JsonUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AggregatorApp {

    private static final Map<String, JobState> jobs = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitConfig.HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(RabbitConfig.RESULTS_EXCHANGE, BuiltinExchangeType.DIRECT, true);
        channel.queueDeclare(RabbitConfig.RESULTS_QUEUE, true, false, false, null);
        channel.queueBind(
                RabbitConfig.RESULTS_QUEUE,
                RabbitConfig.RESULTS_EXCHANGE,
                RabbitConfig.RESULTS_ROUTING_KEY
        );


        DeliverCallback callback = (tag, delivery) -> {
            ResultMessage result =
                    JsonUtils.fromJsonBytes(delivery.getBody(), ResultMessage.class);

            JobState state = jobs.computeIfAbsent(result.jobId, id -> {
                JobState js = new JobState(result.totalSections);
                return js;
            });

            Counters counters = result.counters;

            state.totalWordCount.addAndGet(counters.wordCount);

            counters.wordFreq.forEach((word, count) ->
                                              state.globalWordFreq.merge(word, count, Integer::sum));

            state.totalSentiment.addAndGet(counters.sentimentScore);

            state.anonymizedText.append(counters.anonymizedText).append("\n");

            state.sortedSentences.addAll(counters.sortedSentences);

            int received = state.received.incrementAndGet();
            if (received == state.totalSections) {
                System.out.println("==== JOB FINISHED ====");
                System.out.println("Total words: " + state.totalWordCount.get());
                System.out.println("Top words: " + JobState.getTopN(state.globalWordFreq, 10));
                System.out.println("Total sentiment: " + state.totalSentiment.get());


                JsonUtils.toFile(state, "job_" + result.jobId + "_report.json");
                System.out.println("Results saved to job_" + result.jobId + "_report.json");
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(RabbitConfig.RESULTS_QUEUE, false, callback, tag -> {});
    }
}