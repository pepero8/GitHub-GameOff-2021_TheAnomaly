package Prototype;

import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS_PLAYER = 19;
	private static final int FRAME_ROWS_ROBOT = 19;

	public static final int ANIMATION_STILL = 16;
	public static final int ANIMATION_DRAGGED = 17;
	public static final int ANIMATION_DIE = 18;

	final Texture mainAreaTexture;
	final Texture boxTexture;
	final Texture cardKeyTexture;

	final Texture robotSpriteSheet;
	final Texture playerSpriteSheet;
	final Texture doorObjectSpriteSheet;
	final Texture doorObjectRotatedSpriteSheet;

	final Animation<TextureRegion>[] robotAnimations;
	final Animation<TextureRegion>[] playerAnimations;

	final Animation<TextureRegion> doorAnimation;
	final Animation<TextureRegion> doorAnimationRotated;

	Assets() {
		//robotAnimations = (Animation<TextureRegion>[]) Array.newInstance(Animation.class, FRAME_ROWS_ROBOT);
		robotAnimations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_ROBOT];
		playerAnimations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_PLAYER];

		//mainArea texture
		mainAreaTexture = new Texture(Gdx.files.internal("testmap.png"));
		//box texture
		boxTexture = new Texture(Gdx.files.internal("box_sample.png"));
		//card key texture
		cardKeyTexture = new Texture(Gdx.files.internal("cardKey_sample.png"));

		//sprite sheets
		robotSpriteSheet = new Texture(Gdx.files.internal("player_spritesheet_test.png"));
		playerSpriteSheet = new Texture(Gdx.files.internal("player_spritesheet_test.png"));
		doorObjectSpriteSheet = new Texture(Gdx.files.internal("door_sample_spritesheet.png"));
		doorObjectRotatedSpriteSheet = new Texture(Gdx.files.internal("door_sample_rotated_spritesheet.png"));
	
		//robot animations
		TextureRegion[][] tmp = TextureRegion.split(robotSpriteSheet, robotSpriteSheet.getWidth() / FRAME_COLS, robotSpriteSheet.getHeight() / FRAME_ROWS_ROBOT);
		
		for (int i = 0; i < FRAME_ROWS_ROBOT; i++) {
			TextureRegion[] frames = new TextureRegion[FRAME_COLS];
			for (int j = 0; j < FRAME_COLS; j++) {
				frames[j] = tmp[i][j];
			}
			robotAnimations[i] = new Animation<TextureRegion>(0.5f, frames);
		}

		//player animations
		tmp = TextureRegion.split(playerSpriteSheet, playerSpriteSheet.getWidth() / FRAME_COLS, playerSpriteSheet.getHeight() / FRAME_ROWS_PLAYER);
		
		for (int i = 0; i < FRAME_ROWS_PLAYER; i++) {
			TextureRegion[] frames = new TextureRegion[FRAME_COLS];
			for (int j = 0; j < FRAME_COLS; j++) {
				frames[j] = tmp[i][j];
			}
			playerAnimations[i] = new Animation<TextureRegion>(0.5f, frames);
		}

		//door object animation
		tmp = TextureRegion.split(doorObjectSpriteSheet, doorObjectSpriteSheet.getWidth() / 3, doorObjectSpriteSheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			frames[i] = tmp[0][i];
		}
		doorAnimation = new Animation<TextureRegion>(DoorObject.DOOROBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec
		//rotated door object animation
		tmp = TextureRegion.split(doorObjectRotatedSpriteSheet, doorObjectRotatedSpriteSheet.getWidth() / 3, doorObjectRotatedSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			frames[i] = tmp[0][i];
		}
		doorAnimationRotated = new Animation<TextureRegion>(DoorObject.DOOROBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec

		//Gdx.app.log("Assets", "" + (robotAnimations[16] == null));
	}

	@Override
	public void dispose() {
		mainAreaTexture.dispose();
		robotSpriteSheet.dispose();
		playerSpriteSheet.dispose();
		Gdx.app.log("Textures", "disposed");
	}
}
