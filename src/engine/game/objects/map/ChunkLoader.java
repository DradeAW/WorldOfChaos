package engine.game.objects.map;

import com.Options;
import engine.math.Vector2f;
import engine.rendering.shader.RenderToTextureShader;
import engine.rendering.texture.Texture;
import engine.util.Time;
import engine.util.Window;
import engine.util.profiling.Profiler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

final class ChunkLoader {

	/**
	 * How far beyond the screen the Chunk Loader will start loading chunks.
	 */
	final public static float RADIUS_CHECK = 0.4f;

	/**
	 * How far beyond the screen the Chunk Loader will start to unload chunks.
	 */
	final public static float RADIUS_UNLOAD = 2.0f * Chunk.SIZE;

	/**
	 * Won't load longer than delta * LOADING_RATIO.
	 */
	final public static float LOADING_RATIO = 1.0f/3.0f;

	/**
	 * Pointer to the Map parent.
	 */
	final private @NotNull Map map;

	/**
	 * ChunkLoader's chunks.
	 */
	final private @NotNull HashMap<String, Chunk> chunks;

	/**
	 * ChunkLoader's chunk to load.
	 */
	final private @NotNull ArrayList<Chunk> chunksToLoad;

	/**
	 * Chunk's texture that's loading.
	 */
	private Texture chunkTexture;

	/**
	 * Where we are drawing on the chunk on the x axis.
	 */
	private int xTexture;

	/**
	 * Where we are drawing on the chunk on the y axis.
	 */
	private int yTexture;

	/**
	 * Creates a new ChunkLoader instance.
	 *
	 * @param map Map
	 */
	ChunkLoader(final @NotNull Map map) {
		this.map = map;
		this.chunks = new HashMap<>();
		this.chunksToLoad = new ArrayList<>();
		this.clearChunkTexture();
		this.xTexture = 0;
		this.yTexture = 0;
	}

	/**
	 * Update function.
	 *
	 * @param delta Delta
	 */
	final public void update(final double delta) {
		Profiler.startProfileTimer("Update-ChunkLoading");
		final long startTime = Time.getNanoTime(); // TODO: Get time from Profiler.

		int xStart = (int)((this.getMap().getCameraPosition().getX() - Window.getRatio() - ChunkLoader.RADIUS_CHECK) / Chunk.LENGTH);
		if(xStart < 0) xStart = 0;
		int xEnd = (int)((this.getMap().getCameraPosition().getX() + Window.getRatio() + ChunkLoader.RADIUS_CHECK) / Chunk.LENGTH);
		if(xEnd > Math.ceil(this.getMap().getMapWidth() / Chunk.SIZE)) xEnd = (int) Math.ceil(this.getMap().getMapWidth() / Chunk.SIZE);

		int yStart = (int)((this.getMap().getCameraPosition().getY() - 1 - ChunkLoader.RADIUS_CHECK) / Chunk.LENGTH);
		if(yStart < 0) yStart = 0;
		int yEnd = (int)((this.getMap().getCameraPosition().getY() + 1 + ChunkLoader.RADIUS_CHECK) / Chunk.LENGTH);
		if(yEnd > Math.ceil(this.getMap().getMapHeight() / Chunk.SIZE)) yEnd = (int) Math.ceil(this.getMap().getMapHeight() / Chunk.SIZE);

		// Creates chunks if they do not exists and adds them to the "to load" list.
		for(int x = xStart; x <= xEnd; x++) {
			for(int y = yStart; y <= yEnd; y++) {
				if(this.getChunk(x, y) == null) {
					this.getChunks().put(x + "-" + y, new Chunk(x, y));
					this.getChunksToLoad().add(this.getChunk(x, y));
				}
			}
		}

		/* Loading chunks */
		this.getChunkTexture().bindAsRenderTarget();
		RenderToTextureShader.setTextureScale(new Vector2f(2.0f / (Options.TILE_SIZE * Chunk.SIZE), 2.0f / (Options.TILE_SIZE * Chunk.SIZE)));
		RenderToTextureShader.setTexturePositionScale(new Vector2f(1, 1));

		while(!this.getChunksToLoad().isEmpty() && (Time.getNanoTime() - startTime) * Time.NANO_TO_SECOND < delta * ChunkLoader.LOADING_RATIO) {
			final Chunk chunkToLoad = this.getChunksToLoad().get(0);

			if(chunkToLoad.getX() * Chunk.SIZE + this.getTextureX() < this.getMap().getMapWidth() && chunkToLoad.getY() * Chunk.SIZE + this.getTextureY() < this.getMap().getMapHeight()) { // Draw only if the tile exists.
				final Tile tile = this.getMap().getTileAt(chunkToLoad.getX() * Chunk.SIZE + this.getTextureX(), chunkToLoad.getY() * Chunk.SIZE + this.getTextureY());

				if(tile != null) {
					tile.setPosition(new Vector2f(2.0f * this.getTextureX() / Chunk.SIZE, 2.0f * this.getTextureY() / Chunk.SIZE));
					tile.getRenderedComponent().render(RenderToTextureShader.getInstance(), null);
				}
			}

			this.xTexture++;
			if(this.getTextureX() >= Chunk.SIZE) {
				this.xTexture = 0;
				this.yTexture++;
				if(this.getTextureY() >= Chunk.SIZE) {
					this.yTexture = 0;
					chunkToLoad.setTexture(this.getChunkTexture());
					this.clearChunkTexture();
					this.getChunksToLoad().remove(0);
					//if(Options.DEBUG) System.out.println("Chunk (" + chunkToLoad.getX() + " ; " + chunkToLoad.getY() + ") loaded.");
				}
			}
		}

		/* Chunk unloading */
		// TODO: Chunk unloading (that will also require to change the loading.
		// TODO: Chunk destroy with a very wide radius (just to clean memory if someone plays for 5 hours).

		Window.bindAsRenderTarget();
		Profiler.stopProfileTimer("Update-ChunkLoading");
	}

	/**
	 * Creates a new chunk Texture.
	 */
	private void clearChunkTexture() {
		this.chunkTexture = new Texture(320, 320, GL_COLOR_ATTACHMENT0);
	}

	/**
	 * Returns the ChunkLoader's map parent.
	 *
	 * @return ChunkLoader.map
	 */
	@Contract(pure = true)
	private @NotNull Map getMap() {
		return this.map;
	}

	/**
	 * Returns the ChunkLoader's chunks.
	 *
	 * @return ChunkLoader.chunks
	 */
	@Contract(pure = true)
	final @NotNull HashMap<String, Chunk> getChunks() {
		return this.chunks;
	}

	/**
	 * Returns the Chunk at position (x ; y).
	 *
	 * @param x X position
	 * @param y Y position
	 * @return ChunkLoader.chunks.get(x, y)
	 */
	private Chunk getChunk(final int x, final int y) {
		return this.getChunks().get(x + "-" + y);
	}

	/**
	 * Returns the Chunks to load.
	 *
	 * @return ChunkLoader.chunksToLoad
	 */
	@Contract(pure = true)
	private @NotNull ArrayList<Chunk> getChunksToLoad() {
		return this.chunksToLoad;
	}

	/**
	 * Returns the chunk Texture.
	 *
	 * @return ChunkLoader.chunkTexture
	 */
	@Contract(pure = true)
	private Texture getChunkTexture() {
		return this.chunkTexture;
	}

	/**
	 * Returns the ChunkLoader x position for the Texture loading.
	 *
	 * @return ChunkLoader.xTexture
	 */
	@Contract(pure = true)
	private int getTextureX() {
		return this.xTexture;
	}

	/**
	 * Returns the ChunkLoader y position for the Texture loading.
	 *
	 * @return ChunkLoader.yTexture
	 */
	@Contract(pure = true)
	private int getTextureY() {
		return this.yTexture;
	}

}