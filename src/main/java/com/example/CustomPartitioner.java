package com.example;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {

    // 리컨값 -> 주어진 레코드가 들어갈 파티션 번호
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 메시지 키를 지정하지 앟ㄴ은 경우
        if (keyBytes == null) {
            throw new InvalidRecordException("Need message key");
        }

        // 메시지 키가 Pangyo일 경우 파티션 0번으로 지정되도록 0을 리턴
        if (((String)key).equals("Pangyo")) {
            return 0;
        }

        // 메시지 키가 Pangyo가 아닐 경우 해시값을 지정하여 특정 파티션에 매칭되도록 설정
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
