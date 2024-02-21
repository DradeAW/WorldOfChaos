package engine.game.objects.map;

import com.Options;
import engine.CoreEngine;
import engine.game.objects.GameObject;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.physics.colliders.AABBCollider;
import engine.util.Time;
import engine.util.Window;
import engine.util.profiling.Profiler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import support.json.JSONObject;
import support.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Map is loaded in the folder media/map/{mapName}
 * Contains "loader.json" which tells which zones to load and how
 * Each zone has a {zoneName}.txt that corresponds.
 */
public class Map extends GameObject {

	/**
	 * Current instance used.
	 */
	private static Map INSTANCE;

	/**
	 * Map's name.
	 */
	final private @NotNull String mapName;

	/**
	 * Map's different zones.
	 */
	final private @NotNull ArrayList<Zone> zones;

	/**
	 * Map's tile set.
	 */
	final private @NotNull TileSet tileSet;

	/**
	 * Map's chunk loader.
	 */
	final private @NotNull ChunkLoader chunkLoader;

	/**
	 * Map's width (in tiles).
	 */
	private int tileWidth = 0;

	/**
	 * Map's height (in tiles).
	 */
	private int tileHeight = 0;

	/**
	 * Reference to the game's Camera's position.
	 */
	private Vector3f cameraPosition;

	/**
	 * Creates a new Map instance.
	 *
	 * @param mapName Map's name
	 * @param tileSet Map's tile set
	 */
	public Map(final @NotNull String mapName, final @NotNull TileSet tileSet) {
		super("Map " + mapName, Float.MAX_VALUE, Float.MAX_VALUE);

		this.mapName = mapName;
		this.zones = new ArrayList<>();
		this.tileSet = tileSet;
		this.chunkLoader = new ChunkLoader(this);
	}

	@Override
	public Map init() {
		final long startTime = Time.getNanoTime();
		final JSONObject loader;

		try {
			loader = new JSONObject(new JSONTokener(support.File.getURL("/media/map/" + this.getMapName() + "/loader.json").openStream()));
		} catch(final IOException | NullPointerException e) {
			System.err.println("Error: could not find map " + this.getMapName() + ".");
			System.err.println("loader.json might be missing.");
			new Exception().printStackTrace();
			System.exit(1);
			return this; // Otherwise there is an exception where loader is not defined.
		}

		for(final String zoneName : loader.getJSONObject("zones").keySet()) {
			final JSONObject zoneParams = loader.getJSONObject("zones").getJSONObject(zoneName);

			// Getting zone's position.
			final String[] pos = zoneParams.getString("pos").split(";");
			if(pos.length != 2) {
				System.err.println("Error: The position expected is not in x;y format.");
				System.err.println("Map: " + this.getMapName());
				System.err.println("Zone: " + zoneName);
				System.err.println("Pos: " + zoneParams.getString("pos"));
			}

			// Getting zone's dimensions.
			final String[] dim = zoneParams.getString("dimensions").split("x");
			if(dim.length != 2) {
				System.err.println("Error: The dimension expected is not in wxh format.");
				System.err.println("Map: " + this.getMapName());
				System.err.println("Zone: " + zoneName);
				System.err.println("Dim: " + zoneParams.getString("dimensions"));
			}

			try {
				this.zones.add(new Zone(zoneName, Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(dim[0]), Integer.parseInt(dim[1]), this));
				if(this.getMapWidth() < Integer.parseInt(pos[0]) + Integer.parseInt(dim[0])) {
					this.tileWidth = Integer.parseInt(pos[0]) + Integer.parseInt(dim[0]);
				}
				if(this.getMapHeight() < Integer.parseInt(pos[1]) + Integer.parseInt(dim[1])) {
					this.tileHeight = Integer.parseInt(pos[1]) + Integer.parseInt(dim[1]);
				}
			} catch (final NumberFormatException e) {
				System.err.println("Error: The position expected is x;y and dimension expected wxh : at least one of them is incorrect.");
				System.err.println("Map: " + this.getMapName());
				System.err.println("Zone: " + zoneName);
				System.err.println("Pos: " + zoneParams.getString("pos"));
				System.err.println("Dim: " + zoneParams.getString("dimensions"));
			}
		}

		if(Options.DEBUG) System.out.println("It took " + (Time.getNanoTime() - startTime) * Time.NANO_TO_MILLI + "ms to load the map " + this.getMapName() + ".");
		return this;
	}

	@Override
	public void update(final double delta) {
		this.getChunkLoader().update(delta);

		for(final Chunk chunk : this.getChunkLoader().getChunks().values()) {
			if(!this.hasChild(chunk)) {
				this.addChildInstantly(chunk);
			}
		}

		super.update(delta);
	}

	@Override
	public void addToEngine(final @NotNull CoreEngine engine) {
		super.addToEngine(engine);

		this.setCameraPosition(engine.getRenderingEngine().getMainCamera().getPositionReference());
	}

	/**
	 * Returns the current map being used.
	 *
	 * @return Map::INSTANCE
	 */
	@Contract(pure = true)
	public static Map getInstance() {
		return Map.INSTANCE;
	}

	/**
	 * Sets the current map being used.
	 *
	 * @param map Map to set
	 */
	public static void setInstance(final Map map) {
		Map.INSTANCE = map;
	}

	/**
	 * Checks if there are zones to load or unload.
	 */
	private void checkZoneLoading() {
		Profiler.startProfileTimer("Update-ZoneLoading");
		final float xStart = this.getCameraPosition().getX() - Window.getRatio() - 2 * ChunkLoader.RADIUS_CHECK;
		final float xEnd = this.getCameraPosition().getX() + Window.getRatio() + 2 * ChunkLoader.RADIUS_CHECK;
		final float yStart = this.getCameraPosition().getY() - 1 - 2 * ChunkLoader.RADIUS_CHECK;
		final float yEnd = this.getCameraPosition().getY() + 1 + 2 * ChunkLoader.RADIUS_CHECK;

		for(final Zone zone : this.zones) {
			if(xStart > zone.getPosition().getX() + zone.getWidth() || xEnd < zone.getPosition().getX() || yStart > zone.getPosition().getY()  + zone.getHeight() || yEnd < zone.getPosition().getY()) continue;
			if(this.hasChild(zone)) continue;

			this.addChildInstantly(zone); // Either add instantly or check if not already added this frame (to not add it multiple times).
			zone.init();
		}
		Profiler.stopProfileTimer("Update-ZoneLoading");
	}

	/**
	 * Returns the Map's name.
	 *
	 * @return Map.mapName
	 */
	@Contract(pure = true)
	final public @NotNull String getMapName() {
		return this.mapName;
	}

	/**
	 * Returns the Map's width (in number of tiles).
	 *
	 * @return Map.tileWidth
	 */
	@Contract(pure = true)
	final public int getMapWidth() {
		return this.tileWidth;
	}

	/**
	 * Returns the Map's height (in number of tiles).
	 *
	 * @return Map.tileHeight
	 */
	@Contract(pure = true)
	final public int getMapHeight() {
		return this.tileHeight;
	}

	/**
	 * Returns the character in the map at position (x ; y).
	 *
	 * @param x X position
	 * @param y Y position
	 * @param alreadyCalled For the end.
	 * @return Map.character.y.x
	 */
	private char getCharacterAt(final int x, final int y, final boolean alreadyCalled) {
		if(x < 0 || y < 0) return ' ';
		char c = ' ';

		for(final Zone zone : this.zones) { // TODO: Don't check every single zone.
			final char character = zone.getCharacterAt(x, y);
			if(character != ' ') {
				if(!Options.DEBUG) return character;
				if(c != ' ') {
					System.err.println("Error: Tile (" + x + ";" + y + ") is defined in 2 different zones!");
					System.err.println("2nd zone is : " + zone.getName());
				}

				c = character;
			}
		}

		if(c != ' ' || alreadyCalled) {
			return c;
		} else {
			this.checkZoneLoading();
			return this.getCharacterAt(x, y, true);
		}
	}

	/**
	 * Returns the character in the map at position (x ; y).
	 *
	 * @param x X position
	 * @param y Y position
	 * @return Map.character.y.x
	 */
	final protected char getCharacterAt(final int x, final int y) {
		return this.getCharacterAt(x, y, false);
	}

	/**
	 * Returns the Tile that corresponds to the character.
	 * Returns null if the character does not exists.
	 *
	 * @param character Tile's character
	 * @return Map.tileSet.getTile(character)
	 */
	final protected Tile getTileFromCharacter(final char character) {
		return this.getTileSet().getTile(character);
	}

	/**
	 * Returns the Tile that is at position (x ; y).
	 *
	 * @param x X position (in number of tiles)
	 * @param y Y position (in number of tiles)
	 * @return Tile
	 */
	final protected Tile getTileAt(final int x, final int y) {
		return this.getTileFromCharacter(this.getCharacterAt(x, y));
	}

	/**
	 * Returns the Tile at a certain position (in openGL pos).
	 *
	 * @param pos Position (not in number of tiles)
	 * @return Tile
	 */
	final public Tile getTileAt(final @NotNull Vector2f pos) {
		return this.getTileAt(pos.getX(), pos.getY());
	}

	/**
	 * Returns the Tile that is at position (x ; y).
	 *
	 * @param x X position (in openGL pos)
	 * @param y Y position (in openGL pos)
	 * @return Tile
	 */
	final public Tile getTileAt(final float x, final float y) {
		return this.getTileAt((int)(x / Options.TILE_SIZE), (int)(y / Options.TILE_SIZE));
	}

	/**
	 * Returns all the tiles in a certain rectangle.
	 * Width/Height = 1 means only 1 tile.
	 *
	 * @param x X start position (in number of tiles)
	 * @param y Y start position (in number of tiles)
	 * @param width Width (in number of tiles). width=1 means one column.
	 * @param height Height (in number of tiles). height=1 means one row.
	 * @return new ArrayList
	 */
	final public @NotNull ArrayList<Tile> getTilesOn(final int x, final int y, final int width, final int height) {
		final ArrayList<Tile> tiles = new ArrayList<>();
		for(int i = y; i < y + height; i++) {
			for(int j = x; j < x + width; j++) {
				tiles.add(this.getTileAt(j, i));
			}
		}

		return tiles;
	}

	/**
	 * Returns all the tiles in a certain rectangle.
	 *
	 * @param x X start position (in openGL pos)
	 * @param y Y start position (in openGL pos)
	 * @param width Width (in openGL pos)
	 * @param height Height (in openGL pos)
	 * @return new ArrayList
	 */
	final public @NotNull ArrayList<Tile> getTilesOn(final float x, final float y, final float width, final float height) {
		final int xStart = (int) Math.floor(x / Options.TILE_SIZE);
		final int yStart = (int) Math.floor(y / Options.TILE_SIZE);
		final int w = (int) Math.ceil((x + width) / Options.TILE_SIZE) - xStart;
		final int h = (int) Math.ceil((y + height) / Options.TILE_SIZE) - yStart;

		return this.getTilesOn(xStart, yStart, w, h);
	}

	/**
	 * Returns all the tiles in a certain rectangle.
	 *
	 * @param pos Start position (in openGL pos)
	 * @param width Width (in openGL pos)
	 * @param height Height (in openGL pos)
	 * @return new ArrayList
	 */
	final public @NotNull ArrayList<Tile> getTilesOn(final @NotNull Vector2f pos, final float width, final float height) {
		return this.getTilesOn(pos.getX(), pos.getY(), width, height);
	}

	/**
	 * Returns all the tiles (as an AABBCollider instance) in a certain rectangle.
	 * Width/height = 1 means only 1 column/row.
	 *
	 * @param x X start position (in number of tiles)
	 * @param y Y start position (in number of tiles)
	 * @param width Width (in number of tiles)
	 * @param height Height (in number of tiles)
	 * @param canWalk false = tile will be considered a collider if walkable
	 * @param canSwim false = tile will be considered a collider if swimable
	 * @return new ArrayList
	 */
	final public @NotNull ArrayList<AABBCollider> getTilesOnAsColliders(final int x, final int y, final int width, final int height, final boolean canWalk, final boolean canSwim) {
		final ArrayList<AABBCollider> colliders = new ArrayList<>();
		for(int i = y; i < y + height; i++) {
			for(int j = x; j < x + width; j++) {
				final Tile tile = this.getTileAt(j, i);
				if(tile == null || tile.getSpeedMultiplicator() == 0 || (tile.canWalkOn() && !canWalk) || (tile.canSwimIn() && !canSwim)) { // Add collider if there might be a collision between the object and the tiles.
					final float tileSize = Options.TILE_SIZE;
					colliders.add(new AABBCollider(new Vector2f(j*tileSize, i*tileSize), tileSize, tileSize));
				}
			}
		}

		return colliders;
	}

	/**
	 * Returns all the AABB Colliders of the tiles in a certain rectangle.
	 *
	 * @param pos Start position (in openGL pos)
	 * @param width Width (in openGL pos)
	 * @param height Height (in openGL pos)
	 * @param canWalk false = tile will be considered a collider if walkable
	 * @param canSwim false = tile will be considered a collider if swimable
	 * @return new ArrayList
	 */
	final public @NotNull ArrayList<AABBCollider> getTilesOnAsColliders(final @NotNull Vector2f pos, final float width, final float height, final boolean canWalk, final boolean canSwim) {
		return this.getTilesOnAsColliders(pos.getX(), pos.getY(), width, height, canWalk, canSwim);
	}

	/**
	 * Returns all the AABB Colliders of the tiles in a certain rectangle.
	 *
	 * @param x X start position (in openGL pos)
	 * @param y Y start position (in openGL pos)
	 * @param width Width (in openGL pos)
	 * @param height Height (in openGL pos)
	 * @param canWalk false = tile will be considered a collider if walkable
	 * @param canSwim false = tile will be considered a collider if "swimable"
	 * @return new ArrayList
	 */
	final public @NotNull ArrayList<AABBCollider> getTilesOnAsColliders(final float x, final float y, final float width, final float height, final boolean canWalk, final boolean canSwim) {
		final int xStart = (int) Math.floor(x / Options.TILE_SIZE);
		final int yStart = (int) Math.floor(y / Options.TILE_SIZE);
		final int w = (int) Math.ceil((x + width) / Options.TILE_SIZE) - xStart;
		final int h = (int) Math.ceil((y + height) / Options.TILE_SIZE) - yStart;

		return this.getTilesOnAsColliders(xStart, yStart, w, h, canWalk, canSwim);
	}

	/**
	 * Returns the tile speed at some position.
	 *
	 * @param x X position (in number of tiles)
	 * @param y Y position (in number of tiles)
	 * @param width Number of tiles on the x-axis to check
	 * @param height Number of tiles on the y-axis to check
	 * @return new float
	 */
	final public float getTileSpeedOn(final int x, final int y, final int width, final int height) {
		float speed = 0; // Sum of speed multiplicators.
		for(final Tile tile : this.getTilesOn(x, y, width, height)) {
			speed += tile.getSpeedMultiplicator();
		}

		return speed / (width * height); // Returning the average speed.
	}

	/**
	 * Returns the tile speed at some position.
	 * It takes into consideration whether the object is more on one tile rather than the others.
	 *
	 * @param pos Position to check at (in openGL pos)
	 * @param width Width to check (in openGL pos)
	 * @param height Height to check (in openGL pos)
	 * @return new float
	 */
	final public float getTileSpeedOn(final @NotNull Vector2f pos, final float width, final float height) {
		return this.getTileSpeedOn(pos.getX(), pos.getY(), width, height);
	}

	/**
	 * Returns the tile speed at some position.
	 * It takes into consideration whether the object is more on one tile rather than the others.
	 *
	 * @param x X start position (in openGL pos)
	 * @param y Y start position (in openGL pos)
	 * @param width Width to check (in openGL pos)
	 * @param height Height to check (in openGL pos)
	 * @return new float
	 */
	final public float getTileSpeedOn(final float x, final float y, final float width, final float height) {
		final int xStart = (int)(x / Options.TILE_SIZE);
		final int yStart = (int)(y / Options.TILE_SIZE);
		final int widthCheck = (int)((x + width) / Options.TILE_SIZE) - (int)(x / Options.TILE_SIZE) + 1;
		final int heightCheck = (int)((y + height) / Options.TILE_SIZE) - (int)(y / Options.TILE_SIZE) + 1;

		float speed = 0; // Sum of speed multiplicators * area covered.
		for(int i = yStart; i < yStart + heightCheck; i++) {
			for(int j = xStart; j < xStart + widthCheck; j++) {
				final Tile tile = this.getTileAt(j, i);
				if(tile == null) continue;

				// Width and height covered by both the object and the tile.
				final float w = Math.min((j + 1) * Options.TILE_SIZE, x + width) - Math.max(j * Options.TILE_SIZE, x);
				final float h = Math.min((i + 1) * Options.TILE_SIZE, y + height) - Math.max(i * Options.TILE_SIZE, y);

				speed += Math.abs(tile.getSpeedMultiplicator()) * w * h;
			}
		}

		return speed / (width * height); // Returning the average speed.
	}

	/**
	 * Returns the Map's TileSet.
	 *
	 * @return Map.tileSet
	 */
	@Contract(pure = true)
	private @NotNull TileSet getTileSet() {
		return this.tileSet;
	}

	/**
	 * Returns the Map's chunk loader.
	 *
	 * @return Map.chunkLoader
	 */
	@Contract(pure = true)
	private @NotNull ChunkLoader getChunkLoader() {
		return this.chunkLoader;
	}

	/**
	 * Returns the Camera's position.
	 *
	 * @return ChunkLoader.cameraPosition
	 */
	final @NotNull Vector2f getCameraPosition() {
		return this.cameraPosition.asVector2f();
	}

	/**
	 * Sets the reference of the game's Camera's position.
	 *
	 * @param cameraPosition Camera position to set
	 */
	private void setCameraPosition(final @NotNull Vector3f cameraPosition) {
		this.cameraPosition = cameraPosition;
	}

}