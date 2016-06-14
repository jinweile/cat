package com.dianping.cat;

import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.message.internal.MilliSecondTimer;
import org.unidal.helper.Splitters;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jinweile on 2016/6/12.
 */
public class MessageIdCreater {
    private static volatile long m_timestamp = getCurrentTimestamp();

    private static volatile Map<String,AtomicInteger> m_index_map = new HashMap<String,AtomicInteger>();

    private static volatile Object lock = new Object();

    private static final long HOUR = 3600 * 1000L;

    public static String createMessageId(String domain, String ipAddress, long current_timestamp) {
        String ip = getIp(ipAddress);

        String key = domain + "-" + ip;
        if (!m_index_map.containsKey(key)){
            synchronized (lock) {
                if (!m_index_map.containsKey(key)){
                    m_index_map.put(key, new AtomicInteger(0));
                }
            }
        }

        long timestamp = getTimestamp(current_timestamp);

        if (timestamp != m_timestamp) {
            synchronized (lock) {
                if (timestamp != m_timestamp){
                    m_index_map.put(key, new AtomicInteger(0));
                    m_timestamp = timestamp;
                }
            }
        }

        int index = m_index_map.get(key).getAndIncrement();

        StringBuilder sb = new StringBuilder(domain.length() + 32);

        sb.append(domain);
        sb.append('-');
        sb.append(ip);
        sb.append('-');
        sb.append(timestamp);
        sb.append('-');
        sb.append(index);

        return sb.toString();
    }

    protected static String getIp(String ipAddress){
        List<String> items = Splitters.by(".").noEmptyItem().split(ipAddress);
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) Integer.parseInt(items.get(i));
        }

        StringBuilder sb = new StringBuilder(bytes.length / 2);

        for (byte b : bytes) {
            sb.append(Integer.toHexString((b >> 4) & 0x0F));
            sb.append(Integer.toHexString(b & 0x0F));
        }

        return sb.toString();
    }

    protected static long getCurrentTimestamp() {
        long timestamp = MilliSecondTimer.currentTimeMillis();

        return timestamp / HOUR; // version 2
    }

    protected static long getTimestamp(long current_timestamp) {
        return current_timestamp / HOUR; // version 2
    }
}
