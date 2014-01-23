package com.M101J.homeworks;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class H3_1 {

	/**
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub

		MongoClient client = new MongoClient();
		DB db = client.getDB("school");
		DBCollection collection = db.getCollection("students");
		DBCursor students = collection.find();
		while (students.hasNext()) {
			DBObject student = students.next();
			BasicDBList scores = (BasicDBList) student.get("scores");
			double min = 10000;
			BasicDBList newScores = new BasicDBList();
			Set<Double> HWScores = new HashSet<Double>();
			for (Object obj : scores) {
				String type = (String) ((BasicDBObject) obj).get("type");
				Double score = (Double) ((BasicDBObject) obj).get("score");
				if (type.equals("homework") && score < min) {
					min = score;
					HWScores.add(score);
				} else {
					newScores.add(new BasicDBObject("type", type));
					newScores.add(new BasicDBObject("score", score));
				}
			}
			HWScores.remove(min);
			for (Double score : HWScores) {
				newScores.add(new BasicDBObject("type","homework"));
				newScores.add(new BasicDBObject("score",score));

			}
			collection.update(
					new BasicDBObject().append("_id", student.get("_id")),
					new BasicDBObject().append("name", student.get("name")).append("scores", (newScores)));
		}
		
		students = collection.find();
	}

	public static Double getMinimum(List<Double> grades_array) {
		double min = 10000000;
		for (Double d : grades_array) {
			if (d < min) {
				min = d;
			}
		}
		return min;
	}
}
