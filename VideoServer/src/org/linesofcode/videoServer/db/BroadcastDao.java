package org.linesofcode.videoServer.db;

import java.util.Collection;
import java.util.Map;

import org.linesofcode.videoServer.Broadcast;

public interface BroadcastDao {

	public void persist(Broadcast cast);
	public Broadcast materialize(String id);
	public Collection<Broadcast> find(Map<String, String> args);
	public void update(Broadcast cast);
	Collection<Broadcast> listAll();
	void delete(Broadcast cast);
}
