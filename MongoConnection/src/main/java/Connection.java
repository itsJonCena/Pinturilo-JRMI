import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import com.mongodb.ServerAddress;
import java.util.Arrays;

class Connection  {
  public static void main(String[] args) {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB db = mongoClient.getDB("chat");
    System.out.println("Connect to database successfully");
    DBCollection coll = db.getCollection("messages");
    System.out.println("Collection mycol selected successfully");

    DBCursor cursor = coll.find();
    int i = 1;

    while (cursor.hasNext()) {
      System.out.println("Inserted Document: "+i);
      System.out.println(cursor.next());
      i++;
    }

  }
}
