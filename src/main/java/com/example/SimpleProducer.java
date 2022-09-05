package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleProducer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
    /**
     * 1. 프로듀서는 생성한 레코드를 전송하기 위해 전송하고자 하는 토픽을 알고 있어야 한다.
     * 토픽을 지정하지 않고서는 데이터를 전송할 수 없기 때문
     * 토픽 이름은 Producer Record 인스턴스를 생성할 때 사용됨
     * */
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092"; // 전송하고자 하는 카프카 클러스터 서버의 host와 IP를 지정

    public static void main(String[] args) {
        /* *
         * 2. Properties에는 KafkaProducer 인스턴스를 생성하기 위한 프로듀서 옵션들을 key/value 값으로 선언함
         * 필수 옵션을 반드시 선언해야 하며, 선택 옵션은 선언하지 않아도 됨
         * */
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /* *
         * 3. 메시지 키, 메시지 값을 직렬하기 위한 직렬화 클래스를 선언
         * String을 직렬화하기 위한 카프카 라이브러리의 StringSerializer 사용
         * */
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /* *
         * 4. Properties를 KafkaProducer의 생성 파라미터로 추가하여 인스턴스를 생성
         *  producer 인스턴스는 ProducerRecord를 전송할 떄 사용
         * */
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        /* *
         * 5. 메시지 값 선언
         * * */
        String messageValue = "testMessage";
        /* *
         * 6. 카프카 브로커로 데이터를 보내기 위해 ProducerRecord를 생성
         * - 토픽 이름과 메시지 값만 선언함
         * - 메시지 키는 따로 선언하지 않았으므로 null로 설정되어 전송됨
         * - ProducerRecord를 생성할 떄 생성자에 2개의 제네릭 값이 들어가는데, 이 값은 메시지 키와 메시지 값의 타입을 뜻함
         * */
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messageValue);

        /* *
        * 7. 생성한 ProducerRecord를 전송하기 위해 record를 파라미터로 가지는 send() 메서드 호출
        * 파라미터로 들어간 record를 프로듀서 내부에 가지고 있다가 배치 형태로 묶어서 브로커에 전송 -> 배치 전송
        * * */
        producer.send(record);
        logger.info("{}", record);

        // 8. flush()를 통해 프로듀서 내부 버퍼에 가지고 있던 레코드 배치를 브로커로 전송
        producer.flush();

        // 9. 애플리케이션을 종료하기 전에 close() 메서드를 호출하여 producer 인스턴스의 리소스를 안전하게 종료료
        producer.close();
    }
}
