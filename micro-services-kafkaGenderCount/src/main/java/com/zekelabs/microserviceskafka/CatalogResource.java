package com.zekelabs.microserviceskafka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import Serializer.CustomSerializer;
import pojo.CustomObject;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	WebClient.Builder webClientBuilder;

	 @Autowired
	 private KafkaTemplate<Object, Object> template;

	/*
	 * @GetMapping(path = "/send") public void sendFoo() { this.template.send("Wed",
	 * "Great World"); }
	 */
	@GetMapping(path = "/send")
	public void sendFoo() {

		String csvFile = "/home/edyoda/EmployeeDetails.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			// step1: File Read
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// Step2: Map each row of CSV to POJO
				// use comma as separator
				String[] listData = line.split(cvsSplitBy);

				CustomObject c = new CustomObject();
				c.setId(listData[0]);
				c.setName(listData[1]);
				c.setGender(listData[2]);
				template.send("EmployeeDetailsIn", c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//    static void runProducer() {
//		Producer<Long, String> producer = ProducerCreator.createProducer();
//
//		for (int index = 0; index < IKafkaConstants.MESSAGE_COUNT; index++) {
//		
//			final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
//					"Hello");
//			try {
//				RecordMetadata metadata = producer.send(record).get();
//				//producer.send(record, new DemoCallback());
//				System.out.println(index);
//				System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
//						+ " with offset " + metadata.offset());
//				
//			} catch (ExecutionException e) {
//				System.out.println("Error in sending record");
//				System.out.println(e);
//			} catch (InterruptedException e) {
//				System.out.println("Error in sending record");
//				System.out.println(e);
//			}
//		}
//	}
//
//    @RequestMapping("/{userId}")
//    public String getCatalog(@PathVariable("userId") String userId) {
//
//        /*
//    	UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
//
//        return userRating.getRatings().stream()
//                .map(rating -> {
//                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
//                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
//                })
//                .collect(Collectors.toList());
//        */
//    	runProducer();
//    	return "Done";
//    }
}

/*
 * Alternative WebClient way Movie movie =
 * webClientBuilder.build().get().uri("http://localhost:8082/movies/"+
 * rating.getMovieId()) .retrieve().bodyToMono(Movie.class).block();
 */