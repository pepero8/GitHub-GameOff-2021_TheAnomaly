package Prototype;

import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	private static final int FRAME_COLS_PLAYER = 4;
	private static final int FRAME_COLS_OBJECT = 3;
	private static final int FRAME_ROWS_PLAYER = 19;
	private static final int FRAME_ROWS_ROBOT = 19;

	public static final int ANIMATION_STILL = 16;
	public static final int ANIMATION_DRAGGED = 17;
	public static final int ANIMATION_DIE = 18;

	TextureRegion baseTexture;

	final Texture mainAreaTexture;
	final Texture pathToEntranceAreaTexture;
	final Texture westPassageAreaTexture;
	final Texture westHallwayAreaTexture;
	final Texture southPassageAreaTexture;
	final Texture southHallwayAreaTexture;
	final Texture testRoomAreaTexture;
	final Texture developmentRoomAreaTexture;
	final Texture directorOfficeAreaTexture;

	final Texture boxTexture;
	final Texture cardKeyTexture;

	final Texture robotSpriteSheet;
	final Texture playerSpriteSheet;
	final Texture gateObjectSpriteSheet;
	final Texture doorObjectSpriteSheet;
	final Texture doorObjectRotatedSpriteSheet;
	//final Texture gateObjectRotatedSpriteSheet;

	final Animation<TextureRegion>[] robotAnimations;
	final Animation<TextureRegion>[] playerAnimations;

	final Animation<TextureRegion> gateAnimation;
	final Animation<TextureRegion> doorAnimation;
	final Animation<TextureRegion> doorAnimationRotated;
	//final Animation<TextureRegion> gateAnimationRotated;

	Assets() {
		//robotAnimations = (Animation<TextureRegion>[]) Array.newInstance(Animation.class, FRAME_ROWS_ROBOT);
		robotAnimations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_ROBOT];
		playerAnimations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_PLAYER];

		//base texture
		Texture baseTileTexture = new Texture(Gdx.files.internal("tile_base.png"));
		baseTileTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		baseTexture = new TextureRegion(baseTileTexture);
		//area textures
		mainAreaTexture = new Texture(Gdx.files.internal("area_main.png"));
		pathToEntranceAreaTexture = new Texture(Gdx.files.internal("area_passage_to_entrance.png"));
		westPassageAreaTexture = new Texture(Gdx.files.internal("area_west_passage.png"));
		westHallwayAreaTexture = new Texture(Gdx.files.internal("area_west_hallway.png"));
		southPassageAreaTexture = new Texture(Gdx.files.internal("area_south_passage.png"));
		southHallwayAreaTexture = new Texture(Gdx.files.internal("area_south_hallway.png"));
		testRoomAreaTexture = new Texture(Gdx.files.internal("area_test_room.png"));
		developmentRoomAreaTexture = new Texture(Gdx.files.internal("area_development_room.png"));
		directorOfficeAreaTexture = new Texture(Gdx.files.internal("area_director_office.png"));
		//box texture
		boxTexture = new Texture(Gdx.files.internal("box_sample.png"));
		//card key texture
		cardKeyTexture = new Texture(Gdx.files.internal("cardKey_sample.png"));

		//sprite sheets
		robotSpriteSheet = new Texture(Gdx.files.internal("player_spritesheet_test.png"));
		playerSpriteSheet = new Texture(Gdx.files.internal("player_spritesheet_test.png"));
		gateObjectSpriteSheet = new Texture(Gdx.files.internal("gate_sample_spritesheet.png"));
		doorObjectSpriteSheet = new Texture(Gdx.files.internal("door_sample_spritesheet.png"));
		doorObjectRotatedSpriteSheet = new Texture(Gdx.files.internal("door_sample_rotated_spritesheet.png"));
		//gateObjectRotatedSpriteSheet = new Texture(Gdx.files.internal("door_sample_rotated_spritesheet.png"));
	
		//robot animations
		TextureRegion[][] tmp = TextureRegion.split(robotSpriteSheet, robotSpriteSheet.getWidth() / FRAME_COLS_PLAYER, robotSpriteSheet.getHeight() / FRAME_ROWS_ROBOT);
		
		for (int i = 0; i < FRAME_ROWS_ROBOT; i++) {
			TextureRegion[] frames = new TextureRegion[FRAME_COLS_PLAYER];
			for (int j = 0; j < FRAME_COLS_PLAYER; j++) {
				frames[j] = tmp[i][j];
			}
			robotAnimations[i] = new Animation<TextureRegion>(0.5f, frames);
		}

		//player animations
		tmp = TextureRegion.split(playerSpriteSheet, playerSpriteSheet.getWidth() / FRAME_COLS_PLAYER, playerSpriteSheet.getHeight() / FRAME_ROWS_PLAYER);
		
		for (int i = 0; i < FRAME_ROWS_PLAYER; i++) {
			TextureRegion[] frames = new TextureRegion[FRAME_COLS_PLAYER];
			for (int j = 0; j < FRAME_COLS_PLAYER; j++) {
				frames[j] = tmp[i][j];
			}
			playerAnimations[i] = new Animation<TextureRegion>(0.5f, frames);
		}

		//gate object animation
		tmp = TextureRegion.split(gateObjectSpriteSheet, gateObjectSpriteSheet.getWidth() / FRAME_COLS_OBJECT, gateObjectSpriteSheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[FRAME_COLS_OBJECT];
		for (int i = 0; i < FRAME_COLS_OBJECT; i++) {
			frames[i] = tmp[0][i];
		}
		gateAnimation = new Animation<TextureRegion>(GateObject.GATEOBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec

		//door object animation
		tmp = TextureRegion.split(doorObjectSpriteSheet, doorObjectSpriteSheet.getWidth() / FRAME_COLS_OBJECT, doorObjectSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[FRAME_COLS_OBJECT];
		for (int i = 0; i < FRAME_COLS_OBJECT; i++) {
			frames[i] = tmp[0][i];
		}
		doorAnimation = new Animation<TextureRegion>(DoorObject.DOOROBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec
		
		//rotated door object animation
		tmp = TextureRegion.split(doorObjectRotatedSpriteSheet, doorObjectRotatedSpriteSheet.getWidth() / FRAME_COLS_OBJECT, doorObjectRotatedSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[FRAME_COLS_OBJECT];
		for (int i = 0; i < FRAME_COLS_OBJECT; i++) {
			frames[i] = tmp[0][i];
		}
		doorAnimationRotated = new Animation<TextureRegion>(DoorObject.DOOROBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec

		//Gdx.app.log("Assets", "" + (robotAnimations[16] == null));
	}

	@Override
	public void dispose() {
		mainAreaTexture.dispose();
		pathToEntranceAreaTexture.dispose();
		westPassageAreaTexture.dispose();
		westHallwayAreaTexture.dispose();
		boxTexture.dispose();
		cardKeyTexture.dispose();
		robotSpriteSheet.dispose();
		playerSpriteSheet.dispose();
		gateObjectSpriteSheet.dispose();
		//gateObjectRotatedSpriteSheet.dispose();
		Gdx.app.log("Textures", "disposed");
	}
}
