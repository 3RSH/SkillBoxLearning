import Const.Commands;
import Const.Notices;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import model.Goods;
import model.Store;
import org.bson.Document;

public class Loader {

  private static boolean isRunning = true;

  public static void main(String[] args) {

    startDB();
    try (Scanner scanner = new Scanner(System.in)) {

      while (isRunning) {
        System.out.println(inputProcessing(scanner.nextLine()));
      }
    }
  }

  private static void startDB() {
    try (MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {

      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> stores = database.getCollection("stores");
      MongoCollection<Document> goods = database.getCollection("goods");

      stores.drop();
      goods.drop();
    }
  }

  private static String inputProcessing(String input) {
    if (input.equalsIgnoreCase(Commands.COMMAND_ADD_EXIT.getCom())) {
      isRunning = false;
      return "";
    }

    if (input.matches(Commands.COMMAND_ADD_STORE.getCom())) {
      return addStore(getCommandArgs(input));
    }

    if (input.matches(Commands.COMMAND_ADD_GOODS.getCom())) {
      return addingGoodsProcess(getCommandArgs(input));
    }

    if (input.matches(Commands.COMMAND_PUT_GOODS.getCom())) {
      return putGoodsToStore(getCommandArgs(input));
    }

    if (input.matches(Commands.COMMAND_STATISTIC.getCom())) {
      return getGoodsStatistic();
    }

    return Notices.ERROR_INPUT.getNote();
  }

  private static String addStore(String name) {
    try (MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {
      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> collection = database.getCollection("stores");

      if (isNotExistItem(collection, name)) {
        Store store = new Store();

        store.setName(name);
        store.setGoods(new ArrayList<>());
        collection.insertOne(mapStoreToDocument(store));

        return Notices.STORE_ADDING.getNote()
            .replace("#NAME", name);
      } else {
        return Notices.ERROR_STORE_ADDING.getNote()
            .replace("#NAME", name);
      }
    }
  }

  private static String getCommandArgs(String command) {
    return command.substring(command.indexOf(' ')).trim();
  }

  private static boolean isNotExistItem(MongoCollection<Document> collection, String name) {
    MongoCursor<Document> cursor = collection.find().cursor();

    while (cursor.hasNext()) {
      Document doc = cursor.next();

      String docName = doc.getString("name");

      if (docName.equals(name)) {
        return false;
      }
    }

    return true;
  }

  private static Document mapStoreToDocument(Store store) {
    BasicDBList nestedDocument = new BasicDBList();

    nestedDocument.addAll(store.getGoods());

    return new Document()
        .append("name", store.getName())
        .append("goods", nestedDocument);
  }

  private static String addingGoodsProcess(String argument) {
    String name = argument.substring(0, argument.indexOf(' '));
    int price = Integer.parseInt(getCommandArgs(argument));

    return addGoods(name, price);
  }

  private static String addGoods(String name, int price) {
    try (MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {
      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> collection = database.getCollection("goods");

      if (isNotExistItem(collection, name)) {
        Goods goods = new Goods();

        goods.setName(name);
        goods.setPrice(price);
        collection.insertOne(mapGoodsToDocument(goods));

        return Notices.GOODS_ADDING.getNote()
            .replace("#NAME", name);
      }

      return Notices.ERROR_GOODS_ADDING.getNote()
          .replace("#NAME", name);
    }
  }

  private static Document mapGoodsToDocument(Goods goods) {
    return new Document()
        .append("name", goods.getName())
        .append("price", goods.getPrice());
  }

  private static String putGoodsToStore(String data) {
    String storeName = getCommandArgs(data);
    String goodsName = data.substring(0, data.indexOf(' '));

    try (MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {
      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> storesCollection = database.getCollection("stores");
      MongoCollection<Document> goodsCollection = database.getCollection("goods");

      if (isNotExistItem(storesCollection, storeName)) {
        return Notices.ERROR_STORE_EXIST.getNote()
            .replace("#NAME", storeName);
      }

      if (isNotExistItem(goodsCollection, goodsName)) {
        return Notices.ERROR_GOODS_EXIST.getNote()
            .replace("#NAME", goodsName);
      }

      BasicDBObject query = new BasicDBObject();
      query.put("name", storeName);

      Document store = storesCollection.find(query).first();

      List<String> goodsList = store.getList("goods", String.class);

      if (goodsList.contains(goodsName)) {
        return Notices.ERROR_GOODS_PUTTING.getNote()
            .replace("#GOODS", goodsName)
            .replace("#STORE", storeName);
      }

      goodsList.add(goodsName);

      BasicDBList goodsListDocument = new BasicDBList();
      goodsListDocument.addAll(goodsList);

      store.put("goods", goodsListDocument);
      storesCollection.findOneAndReplace(query, store);
    }

    return Notices.GOODS_PUTTING.getNote()
        .replace("#GOODS", goodsName)
        .replace("#STORE", storeName);
  }

  private static String getGoodsStatistic() {
    StringBuilder statistic = new StringBuilder();

    try (MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {
      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> stores = database.getCollection("stores");
      MongoCollection<Document> goods = database.getCollection("goods");

      statistic.append("Общее количество товаров: \n")
          .append(getGoodsCount(stores))
          .append("\n")
          .append("Средняя цена товара: \n")
          .append(getAvgPrice(stores))
          .append("\n")
          .append("Самый дорогой товар: \n")
          .append(getMaxMinGoods(goods, -1))
          .append("\n")
          .append("Самый дешёвый товар: \n")
          .append(getMaxMinGoods(goods, 1))
          .append("\n")
          .append("Количество товаров дешевле 100 рублей: \n")
          .append(getGoodsCountLess100(goods));
    }

    return statistic.toString();
  }

  private static String getGoodsCount(MongoCollection<Document> stores) {
    StringBuilder output = new StringBuilder();

    BasicDBObject projectArgs = new BasicDBObject()
        .append("store", "$name")
        .append("goods", "$goods");

    BasicDBObject groupArgs = new BasicDBObject()
        .append("_id", "$store")
        .append("count", new BasicDBObject("$sum", 1));

    MongoCursor<Document> cursor = stores.aggregate(Arrays.asList(
        new BasicDBObject("$unwind", new BasicDBObject("path", "$goods")),
        new BasicDBObject("$project", projectArgs),
        new BasicDBObject("$group", groupArgs),
        new BasicDBObject("$sort", new BasicDBObject("_id", 1)))).cursor();

    while (cursor.hasNext()) {
      Document store = cursor.next();

      output.append("\t\tМагазин \"")
          .append(store.get("_id"))
          .append("\": ")
          .append(store.get("count"))
          .append(";\n");
    }

    return output.toString();
  }

  private static String getAvgPrice(MongoCollection<Document> stores) {
    StringBuilder output = new StringBuilder();

    BasicDBObject lookupArgs = new BasicDBObject()
        .append("from", "goods")
        .append("localField", "goods")
        .append("foreignField", "name")
        .append("as", "goods_list");

    BasicDBObject groupArgs = new BasicDBObject()
        .append("_id", "$name")
        .append("avgprice", new BasicDBObject("$avg", "$goods_list.price"));

    MongoCursor<Document> cursor = stores.aggregate(Arrays.asList(
        new BasicDBObject("$lookup", lookupArgs),
        new BasicDBObject("$unwind", new BasicDBObject("path", "$goods_list")),
        new BasicDBObject("$group", groupArgs),
        new BasicDBObject("$sort", new BasicDBObject("_id", 1)))).cursor();

    while (cursor.hasNext()) {
      Document store = cursor.next();

      output.append("\t\tМагазин \"")
          .append(store.get("_id"))
          .append("\": ")
          .append(store.get("avgprice"))
          .append(";\n");
    }

    return output.toString();
  }

  private static String getMaxMinGoods(MongoCollection<Document> goods, int queryParam) {
    StringBuilder output = new StringBuilder();

    BasicDBObject lookupArgs = new BasicDBObject()
        .append("from", "stores")
        .append("localField", "name")
        .append("foreignField", "goods")
        .append("as", "store");

    BasicDBObject projectArgs = new BasicDBObject()
        .append("store", "$store.name")
        .append("goods", "$name");

    BasicDBObject groupArgs = new BasicDBObject()
        .append("_id", "$store")
        .append("goods", new BasicDBObject("$first", "$goods"));

    MongoCursor<Document> cursor = goods.aggregate(Arrays.asList(
        new BasicDBObject("$lookup", lookupArgs),
        new BasicDBObject("$unwind", new BasicDBObject("path", "$store")),
        new BasicDBObject("$sort", new BasicDBObject("price", queryParam)),
        new BasicDBObject("$project", projectArgs),
        new BasicDBObject("$group", groupArgs),
        new BasicDBObject("$sort", new BasicDBObject("_id", 1)))).cursor();

    while (cursor.hasNext()) {
      Document store = cursor.next();

      output.append("\t\tМагазин \"")
          .append(store.get("_id"))
          .append("\": ")
          .append(store.get("goods"))
          .append(";\n");
    }

    return output.toString();
  }

  private static String getGoodsCountLess100(MongoCollection<Document> goods) {
    StringBuilder output = new StringBuilder();

    BasicDBObject lookupArgs = new BasicDBObject()
        .append("from", "stores")
        .append("localField", "name")
        .append("foreignField", "goods")
        .append("as", "store");

    BasicDBObject projectArgs = new BasicDBObject()
        .append("store", "$store.name")
        .append("price", "$price");

    BasicDBObject matchArgs = new BasicDBObject()
        .append("price", new BasicDBObject("$lt", 100));

    BasicDBObject groupArgs = new BasicDBObject()
        .append("_id", "$store")
        .append("count", new BasicDBObject("$sum", 1));

    MongoCursor<Document> cursor = goods.aggregate(Arrays.asList(
        new BasicDBObject("$lookup", lookupArgs),
        new BasicDBObject("$unwind", new BasicDBObject("path", "$store")),
        new BasicDBObject("$project", projectArgs),
        new BasicDBObject("$match", matchArgs),
        new BasicDBObject("$group", groupArgs),
        new BasicDBObject("$sort", new BasicDBObject("_id", 1)))).cursor();

    while (cursor.hasNext()) {

      Document store = cursor.next();

      output.append("\t\tМагазин \"")
          .append(store.get("_id"))
          .append("\": ")
          .append(store.get("count"))
          .append(";\n");
    }

    output.delete(output.length() - 1, output.length());

    return output.toString();
  }
}