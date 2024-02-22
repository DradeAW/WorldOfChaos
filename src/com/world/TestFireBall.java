package com.world;

import com.Options;
import com.objects.characters.Character;
import engine.audio.AudioObject;
import engine.game.components.RenderedComponent;
import engine.math.Vector2f;
import engine.physics.MovementsAllowed;
import engine.physics.PhysicsObject;
import engine.physics.colliders.CircleCollider;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import org.jetbrains.annotations.NotNull;

public class TestFireBall extends PhysicsObject {

	/**
	 * Tree's material.
	 */
	final public static Material MATERIAL = new Material(new Texture("circle"));

	/**
	 * Create a new TestFireBall instance.
	 */
	public TestFireBall() {
		super("Test fire ball", Options.TILE_SIZE, Options.TILE_SIZE, new CircleCollider(), MovementsAllowed.FLY);

		this.setPosition(new Vector2f(40 * Options.TILE_SIZE, 7.5f * Options.TILE_SIZE));
		this.setDepth(-0.3f);
		this.setLinearVelocity(new Vector2f(-6, 6));

		final RenderedComponent renderedComponent = new RenderedComponent(TestFireBall.MATERIAL, Options.TILE_SIZE, Options.TILE_SIZE);
		this.addComponent(renderedComponent);

		final AudioObject audio = new AudioObject("/fireplace_mono.wav");
		audio.setVolume(0.5f);
		this.addAudioObject(audio);
		audio.play();
	}

	@Override
	protected float getPhysicsHeight() {
		return Options.TILE_SIZE;
	}

	@Override
	protected void onCollision(final @NotNull PhysicsObject object) {
		if(object instanceof Character) {
			System.out.println("PAF !");
		}
	}

}