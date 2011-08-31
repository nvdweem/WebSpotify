package spotify;

import org.json.JSONObject;

public class Media extends Completable {
	protected String id;
	protected String name;
	
	public Media(String id) {
		if (id == null) throw new IllegalArgumentException("Media's must have an id.");
		this.id = id;
	}
	
	public String toString() {
		return toJSON().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public JSONObject toJSON() {
		JSONObject object = new JSONObject();
		object.put("id", id);
		object.put("name", name);
		return object;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Media && getId().equals(((Media) obj).getId());
	}
}
