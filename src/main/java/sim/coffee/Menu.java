package sim.coffee;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Menu {

	private Map<String, MenuItem> map = new HashMap<>();

	Menu() {
		map.put("test", new MenuItem());
		map.put("test2", new MenuItem());
		map.put("test3", new MenuItem());
	}

	public void readFile(String fileName) {
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public MenuItem getKey(String key) {
		return map.get(key);
	}
}
