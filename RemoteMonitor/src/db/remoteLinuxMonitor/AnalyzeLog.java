package db.remoteLinuxMonitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyzeLog {
	
	static String filename = null;
	static LocalDateTime startTime = null;
	static LocalDateTime endTime = null;
	static StringBuilder sb;
	
	static Map<Long, Long> cpuMap;
	static Map<Long, Long> physicalMap;
	static Map<Long, Long> swapMap;
	static Map<Long, Long> receiveMap;
	static Map<Long, Long> sendMap;

	public static String analyze(String logFileName) {
		
		sb = new StringBuilder();
		cpuMap = new HashMap<>();
		physicalMap = new HashMap<>();
		swapMap = new HashMap<>();
		receiveMap = new HashMap<>();
		sendMap = new HashMap<>();
		
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);		

		try (Stream<String> stream = Files.lines(Paths.get(logFileName))) {
			
			stream.forEach((line) -> {
				if (!line.startsWith("T")) {
					 StringTokenizer st = new StringTokenizer(line,"\t");
					 
					 LocalDateTime dateTime = null;
					 Long cpu = null;
					 Long physical = null;
					 Long swap = null;
					 Long receive = null;
					 Long send = null;
					 
					 while (st.hasMoreTokens()) {
					     dateTime = LocalDateTime.parse(st.nextToken(), format);
					     cpu = getLong(st.nextToken());
					     physical = getLong(st.nextToken());
					     swap = getLong(st.nextToken());
					     receive = getLong(st.nextToken());
					     send = getLong(st.nextToken());
					 }
					 
					 if (startTime == null) {
						 startTime = dateTime;
					 }
					 
					 endTime = dateTime;
					 
					if (cpuMap.containsKey(cpu)) {
						cpuMap.put(cpu, cpuMap.get(cpu) + 1);
					} else {
						cpuMap.put(cpu, 1L);
					}
					
					if (physicalMap.containsKey(physical)) {
						physicalMap.put(physical, physicalMap.get(physical) + 1);
					} else {
						physicalMap.put(physical, 1L);
					}
					
					if (swapMap.containsKey(swap)) {
						swapMap.put(swap, swapMap.get(swap) + 1);
					} else {
						swapMap.put(swap, 1L);
					}

					if (receiveMap.containsKey(receive)) {
						receiveMap.put(receive, receiveMap.get(receive) + 1);
					} else {
						receiveMap.put(receive, 1L);
					}

					if (sendMap.containsKey(send)) {
						sendMap.put(send, sendMap.get(send) + 1);
					} else {
						sendMap.put(send, 1L);
					}
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Duration duration = Duration.between(startTime, endTime);
		
		long days = duration.toDays();
		long hours = duration.toHours() % 24;
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;
		
		sb.append(System.lineSeparator());
		sb.append("Remote Linux Monitor Statistics" + System.lineSeparator());
		sb.append(String.format("From: %s  To: %s%s", startTime.format(format), endTime.format(format), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append("Analysis Period: " + days + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds" + System.lineSeparator());
		sb.append("=============================================================================" + System.lineSeparator());

		displayCpuStats(getStats(cpuMap));
		displayMemoryStats(getStats(physicalMap), getStats(swapMap));
		displayNetworkStats(getStats(receiveMap), getStats(sendMap));
		
		return sb.toString();
	}
	
	static StatClass getStats(Map<Long,Long> map) {
		
		StatClass statClass = new StatClass();
		
		Map<Long,Long> sortMap = sortMap(map);
		statClass.setMap(sortMap);	
		sumAvg(map,statClass);
		
		return statClass;
	}
	
	static Map<Long,Long> sortMap(Map<Long,Long> map) {
		
		Map<Long, Long> sortedMap = map.entrySet().stream()
		        .sorted(Comparator.comparingLong(e -> -e.getKey()))
		        .limit(10)
		        .collect(Collectors.toMap(
		                Map.Entry::getKey,
		                Map.Entry::getValue,
		                (a, b) -> { throw new AssertionError(); },
		                LinkedHashMap::new
		        ));

        return sortedMap;
    }
	
	static void sumAvg(Map<Long,Long> map, StatClass statClass) {
		
		double sum = 0;
		double avg = 0;
		double secs = 0;
		
		for (Long key : map.keySet()) {
			  sum += key * map.get(key);
			  secs += map.get(key);
		}
		
		avg = sum / secs;
		
		statClass.setSum(sum);
		statClass.setAvg(avg);
	}
	
	static String displaySecounds(Long seconds) {

        int hours = (int) (seconds / 3600);
        int remainingSeconds = (int) (seconds % 3600);
        int minutes = remainingSeconds / 60;
        int finalSeconds = remainingSeconds % 60;

        String display = "";

        if (hours > 0) {
        	display = String.format("%d hrs : %d mins : %d secs", hours, minutes, finalSeconds);
        
        } else if (minutes > 0) {
        	display = String.format("%d mins : %d secs", minutes, finalSeconds);
        
        } else if (seconds == 1) {
        	display = String.format("%d sec", finalSeconds);
        	
        } else if (seconds > 0) {
        	display = String.format("%d secs", finalSeconds);
        }
        
        return display;
    }
	
	static String displayBytes(Double bytes, boolean perSec) {
		
		String display = "";
		
		if (bytes >= 1000000000) {
			display = String.format("%1.2f GBytes", bytes / 1000000000);
			
		} else if (bytes >= 1000000) {
			display = String.format("%1.2f MBytes", bytes / 1000000);
			
		} else if (bytes >= 1000) {
			display = String.format("%1.2f KBytes", bytes / 1000);
			
		} else {
			display = String.format("%.0f bytes", bytes);
		}
		
		if (perSec) {
			return display + "/sec";
			
		} else {
			return display;
		}
	}
	
	static void displayCpuStats(StatClass statClass) {
		
		sb.append(System.lineSeparator());
		sb.append(String.format("Average all CPUs usage across analysis period: %2.4f%%%s", statClass.getAvg(),System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Top %d usage stats:%s", statClass.getMap().size(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Usage\t\tDuration" + System.lineSeparator()));
		sb.append(String.format("-------------------------------------------" + System.lineSeparator()));
		
		for (Long key : statClass.getMap().keySet()) {
			sb.append(String.format("%-4s\t\t%s%s", key + "%", displaySecounds(statClass.getMap().get(key)), System.lineSeparator()));	
		}
		
		sb.append(System.lineSeparator());
		sb.append("=============================================================================" + System.lineSeparator());
	}
	
	static void displayMemoryStats(StatClass physicalStatClass, StatClass swapStatClass) {
		
		sb.append(System.lineSeparator());
		sb.append(String.format("Average Physical Memory usage across analysis period: %2.4f%%%s", physicalStatClass.getAvg(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Top %d usage stats:%s", physicalStatClass.getMap().size(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Usage\t\tDuration" + System.lineSeparator()));
		sb.append(String.format("-------------------------------------------" + System.lineSeparator()));
		
		for (Long key : physicalStatClass.getMap().keySet()) {
			sb.append(String.format("%-4s\t\t%s%s", key + "%", displaySecounds(physicalStatClass.getMap().get(key)), System.lineSeparator()));	
		}
		
		sb.append(String.format("-------------------------------------------------------------" + System.lineSeparator()));
		
		sb.append(System.lineSeparator());
		sb.append(String.format("Average Swap Memory usage across analysis period: %2.4f%%%s", swapStatClass.getAvg(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Top %d usage stats:%s", swapStatClass.getMap().size(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Usage\t\tDuration" + System.lineSeparator()));
		sb.append(String.format("-------------------------------------------" + System.lineSeparator()));
		
		for (Long key : swapStatClass.getMap().keySet()) {
			sb.append(String.format("%-4s\t\t%s", key + "%", displaySecounds(swapStatClass.getMap().get(key)), System.lineSeparator()));	
		}
		
		sb.append(System.lineSeparator());
		sb.append("=============================================================================" + System.lineSeparator());
	}
	
	static void displayNetworkStats(StatClass receiveStatClass, StatClass sendStatClass) {
		
		sb.append(System.lineSeparator());
		sb.append(String.format("Average Receive Bytes/Second usage across analysis period: %s%s", displayBytes(receiveStatClass.getAvg(), true), System.lineSeparator()));
		sb.append(String.format("Sum of received bytes: %s%s", displayBytes(receiveStatClass.getSum(), false), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Top %d usage stats:%s", receiveStatClass.getMap().size(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Usage                         Duration" + System.lineSeparator()));
		sb.append(String.format("-------------------------------------------" + System.lineSeparator()));
		
		for (Long key : receiveStatClass.getMap().keySet()) {
			sb.append(String.format("%-30s%s%s", displayBytes(Double.parseDouble(key.toString()), true), displaySecounds(receiveStatClass.getMap().get(key)), System.lineSeparator()));	
		}
		
		sb.append(String.format("-------------------------------------------------------------" + System.lineSeparator()));
		
		sb.append(System.lineSeparator());
		sb.append(String.format("Average Send Bytes/Second usage across analysis period: %s%s", displayBytes(sendStatClass.getAvg(), true), System.lineSeparator()));
		sb.append(String.format("Sum of sent bytes: %s%s", displayBytes(sendStatClass.getSum(), false), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Top %d usage stats:", sendStatClass.getMap().size(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Usage                         Duration" + System.lineSeparator()));
		sb.append(String.format("-------------------------------------------" + System.lineSeparator()));
		
		for (Long key : sendStatClass.getMap().keySet()) {
			sb.append(String.format("%-30s%s%s", displayBytes(Double.parseDouble(key.toString()), true), displaySecounds(sendStatClass.getMap().get(key)), System.lineSeparator()));
		}
		
		sb.append(System.lineSeparator());
		sb.append("=============================================================================" + System.lineSeparator());
	}
	
	private static Long getLong(String str) {
		try {
			return Long.parseLong(str);

		} catch (NumberFormatException e) {
			return 0L;
		}
	}
}
