package poc.spark;

import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;

public class DiffOperations {
	public static void main(String[] args) {
		
		SparkSession spark = SparkSession
			      .builder()
			      .appName("JavaTokenizerExample")
			      .config("spark.master", "local")//spark://localhost:7077
			      .getOrCreate();
		
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		
		final JavaRDD<String[]> input = jsc.parallelize(Lists.newArrayList(
	            //           date        time     distance    object     region
	            new String[]{"2016-03-28", "11:00", "1", "onibus1", "SP"},
	            new String[]{"2016-03-28", "11:01", "2", "onibus1", "SP"},
	            new String[]{"2016-03-28", "11:02", "4", "onibus1", "SP"},
	            new String[]{"2016-03-28", "11:03", "3", "onibus1", "SP"},
	            new String[]{"2016-03-28", "11:00", "5", "onibus2", "SP"},
	            new String[]{"2016-03-28", "11:03", "15", "onibus2", "SP"},
	            new String[]{"2016-03-28", "11:06", "9", "onibus2", "SP"},
	            new String[]{"2016-03-29", "13:01", "2", "onibus2", "SP"},
	            new String[]{"2016-03-29", "13:01", "2", "onibus2", "SP"}
	    ));

	    // grouping by key:
	    final JavaPairRDD<String, Iterable<String[]>> byObjectAndDate = input.groupBy(new Function<String[], String>() {
	        
	    	private static final long serialVersionUID = 1L;

			public String call(String[] record) throws Exception {
	            return record[0] + record[3] + record[4]; // date, object, region
	        }
	    });

	    // mapping each "value" (all record matching key) to result
	    final JavaRDD<String[]> result = byObjectAndDate.mapValues(new Function<Iterable<String[]>, String[]>() {
	        
	    	private static final long serialVersionUID = 1L;

			public String[] call(Iterable<String[]> records) throws Exception {
	            final Iterator<String[]> iterator = records.iterator();
	            String[] previousRecord = iterator.next();
	            int diffMinutes = 0;
	            int acumulatedDistance = 0;

	            for (String[] record : records) {
	            	acumulatedDistance += Integer.parseInt(record[2]);
	            	final LocalDateTime prev = getLocalDateTime(previousRecord);
                    final LocalDateTime curr = getLocalDateTime(record);
                    diffMinutes += Period.fieldDifference(prev, curr).toStandardMinutes().getMinutes();
	                previousRecord = record;
	            }

	            return new String[]{
	                    previousRecord[0],
	                    Integer.toString(diffMinutes),
	                    Integer.toString(acumulatedDistance),
	                    previousRecord[3],
	                    previousRecord[4]
	            };
	        }
	    }).values();

	    System.out.println("Print results:");
	    List<String[]> tokenDatumMap = result.collect();
	    for (String[] value : tokenDatumMap) {
	    	for(String s: value){
	    		System.out.print("["+s+"] ");
	    	}
	    	System.out.println();
	    }
	    
	}
	
	// extracts a Joda LocalDateTime from a "record"
	static LocalDateTime getLocalDateTime(String[] record) {
	    return LocalDateTime.parse(record[0] + " " + record[1], formatter);
	}

	static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
}

