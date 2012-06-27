package org.linesofcode.videoServer.db;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.Broadcast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class BroadcastDaoMongoImpl implements BroadcastDao {

	private static final Logger LOG = Logger.getLogger(BroadcastDaoMongoImpl.class);
	
	private DBCollection collection;
	private String collectionName = "videos";
	
	public BroadcastDaoMongoImpl(MongoDbConnector p) {
		DB db = p.getDb();
		if(!db.collectionExists(collectionName)) {
			LOG.debug("Collection "  + collectionName + " does not exist. Initializing...");
			db.createCollection(collectionName, null);
		}
		collection = db.getCollection(getCollectionName());
	}
	
	@Override
	public void persist(Broadcast cast) {
		if(findById(cast.getId()) != null) {
			throw new RuntimeException("Error persisting %s. Object exists in database. Use update instead");
		}
		DBObject item = castToDbObject(cast);
		WriteResult result = collection.insert(item);
		if(result.getLastError().getException() != null) {
			throw result.getLastError().getException();
		}
		LOG.debug(String.format("Broadcast ID: %s successfully persisted", cast.getId()));
	}
	
	private Broadcast findById(String id) {
		BasicDBObject item = new BasicDBObject();
		item.put("id", id);
		DBCursor c = collection.find(item);
		if(c.hasNext()) {
			return makeBroadcastObject(c.next());
		}
		return null;
	}

	private Broadcast makeBroadcastObject(DBObject item) {
		return new Broadcast(
				item.get("id").toString(),
				Double.parseDouble(item.get("lat").toString()),
				Double.parseDouble(item.get("lng").toString()),
				item.get("title").toString());
	}

	@Override
	public Broadcast materialize(String id) {
		Broadcast cast = findById(id);
		if(cast == null) {
			throw new RuntimeException(String.format("FATAL: broadcast ID %s could not be found in data base", id));
		}
		return findById(id);
	}

	@Override
	public Collection<Broadcast> find(Map<String, String> args) {
		BasicDBObject item = new BasicDBObject();
		Set<String> keys = args.keySet();
		for(String s : keys) {
			item.put(s, args.get(s));
		}
		DBCursor c = collection.find(item);
		LinkedList<Broadcast> list = new LinkedList<Broadcast>();
		while(c.hasNext()) {
			list.add(makeBroadcastObject(c.next()));
		}
		return list;
	}
	
	@Override
	public Collection<Broadcast> listAll() {
		DBCursor c = collection.find();
		LinkedList<Broadcast> list = new LinkedList<Broadcast>();
		while(c.hasNext()) {
			list.add(makeBroadcastObject(c.next()));
		}
		return list;
	}

	@Override
	public void update(Broadcast cast) {
		BasicDBObject item = new BasicDBObject();
		item.put("id", cast.getId());
		DBCursor c = collection.find(item);
		if(!c.hasNext()) {
			return;
		}
		WriteResult result = collection.update(c.next(), castToDbObject(cast));
		if(result.getLastError().getException() != null) {
			throw result.getLastError().getException();
		}
		LOG.debug(String.format("Broadcast ID: %s successfully updated", cast.getId()));
	}

	private DBObject castToDbObject(Broadcast cast) {
		DBObject item = new BasicDBObject();
		item.put("id", cast.getId());
		item.put("title", cast.getTitle());
		item.put("lat", cast.getLattitude());
		item.put("lng", cast.getLongitude());
		return item;
	}
	
	@Override
	public void delete(Broadcast cast) {
		WriteResult result = collection.remove(castToDbObject(cast));
		if(result.getLastError().getException() != null) {
			throw result.getLastError().getException();
		}
		LOG.debug(String.format("Broadcast ID: %s successfully deleted", cast.getId()));
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
}
