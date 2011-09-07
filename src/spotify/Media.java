package spotify;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * Superclass for media types.
 * @author Niels
 */
public abstract class Media extends Completable implements Serializable {
	private static final long serialVersionUID = -6025491409147182598L;
	
	protected String id;
	protected String name;
	protected boolean busy = false; // Used to indicate start en end of browsing.
	
	public Media(String id) {
		if (id == null) throw new IllegalArgumentException("Media's must have an id.");
		this.id = id;
	}
	
	@Override
	public boolean isComplete() {
		return !busy && super.isComplete();
	}

	public String toString() {
		return toJSON(true).toString();
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
	
	public JSONObject toJSON(boolean includeChildren) {
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

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
}
