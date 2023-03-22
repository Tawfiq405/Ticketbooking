package models;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Screen {

	private String name;
	private boolean RGB;
	private int sound;
	private String resolution;
	private Theatre theatre;
	private int scId;
	private Map<Entry<Integer,Integer>,Map<Integer,Set<Entry<Integer,Boolean>>>> screenseating;
	
	@Override
	public String toString() {
		return "Screen [name=" + name + ", RGB=" + RGB + ", sound=" + sound + ", resolution=" + resolution
				+ ", theatre=" + theatre + ", scId=" + scId + ", screenseating=" + screenseating + "]";
	}

	public Screen(String name, boolean rGB, int sound, String resolution, Theatre theatre, int scId,
			Map<Entry<Integer,Integer>,Map<Integer,Set<Entry<Integer,Boolean>>>> screenseating) {
		super();
		this.name = name;
		RGB = rGB;
		this.sound = sound;
		this.resolution = resolution;
		this.theatre = theatre;
		this.scId = scId;
		this.screenseating = screenseating;
	}

	public String getName() {
		return name;
	}

	public boolean isRGB() {
		return RGB;
	}

	public int getSound() {
		return sound;
	}

	public String getResolution() {
		return resolution;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public int getScId() {
		return scId;
	}

	public Map<Entry<Integer,Integer>,Map<Integer,Set<Entry<Integer,Boolean>>>> getScreenseating() {
		return screenseating;
	}
		

}
