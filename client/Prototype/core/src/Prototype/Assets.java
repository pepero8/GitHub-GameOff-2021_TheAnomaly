package Prototype;

import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	private static final int FRAME_COLS_PLAYER = 4;
	private static final int FRAME_COLS_OBJECT = 3;
	private static final int FRAME_ROWS_PLAYER = 19;
	private static final int FRAME_ROWS_ROBOT = 19;

	public static final int ANIMATION_STILL = 16;
	public static final int ANIMATION_DRAGGED = 17;
	public static final int ANIMATION_DIE = 18;

	public static final float SOUND_INTERVAL_RUNNING = 0.250f;
	public static final float SOUND_INTERVAL_DODGE = 1f;
	public static final float SOUND_INTERVAL_ATTACK = 0.6f;
	public static final float SOUND_INTERVAL_RUSH = 0.2f;
	public static final float AUDIBILITY_RANGE = 600f;

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
	final Texture serverRoomAreaTexture;
	final Texture officeAreaTexture;
	final Texture pathToExitAreaTexture;

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

	//final Texture remainTimeBoxTexture;
	final Texture interactableMark;
	final Texture portraitNormalTexture;
	final Texture portraitKeyTexture;
	final Texture portraitKilledTexture;
	final TextureRegionDrawable portraitNormalDrawable;
	final TextureRegionDrawable portraitKeyDrawable;
	final TextureRegionDrawable portraitKilledDrawable;
	Skin skin;

	final Texture resultWindowTexture;

	final Texture introMatchInfoTexture;
	final Texture introBackgroundTexture;
	final Texture introRobotSpriteSheet;
	final Animation<TextureRegion> introRobotAnimation;

	final Sound introRobotMalfunction;
	final Sound introRobotVoice;
	final Sound introAlarm;
	final Sound introBackground;

	final Texture mainMenuBackgroundTexture;

	final Music mainMenuMusic;
	final Sound click;

	final Sound resultWindowDown;
	final Sound resultPop;
	final Sound resultMessage;

	//Sound[] humanSounds;

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
		serverRoomAreaTexture = new Texture(Gdx.files.internal("area_server_room.png"));
		officeAreaTexture = new Texture(Gdx.files.internal("area_office.png"));
		pathToExitAreaTexture = new Texture(Gdx.files.internal("area_passage_to_exit.png"));
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

		//remainTimeBoxTexture = new Texture(Gdx.files.internal("remain_time_box_texture.png"));
		//Gdx.app.log("Assets", "" + (robotAnimations[16] == null));
		interactableMark = new Texture(Gdx.files.internal("interactable_mark.png"));
		portraitNormalTexture = new Texture(Gdx.files.internal("portrait_normal.png"));
		portraitKeyTexture = new Texture(Gdx.files.internal("portrait_cardkey.png"));
		portraitKilledTexture = new Texture(Gdx.files.internal("portrait_killed.png"));
		portraitNormalDrawable = new TextureRegionDrawable(portraitNormalTexture);
		portraitKeyDrawable = new TextureRegionDrawable(portraitKeyTexture);
		portraitKilledDrawable = new TextureRegionDrawable(portraitKilledTexture);
		//portraitNormalTexture = new Texture(Gdx.files.internal("portrait_normal.png"));
		//portraitKeyTexture = new Texture(Gdx.files.internal("portrait_cardkey.png"));
		skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

		resultWindowTexture = new Texture(Gdx.files.internal("result_window.png"));

		introMatchInfoTexture = new Texture(Gdx.files.internal("intro_match_info.png"));
		introBackgroundTexture = new Texture(Gdx.files.internal("intro_development_room.png"));
		introRobotSpriteSheet = new Texture(Gdx.files.internal("intro_robot_spritesheet.png"));
		tmp = TextureRegion.split(introRobotSpriteSheet, introRobotSpriteSheet.getWidth() / 20, introRobotSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[20];
		for (int i = 0; i < 20; i++) {
			frames[i] = tmp[0][i];
		}
		introRobotAnimation = new Animation<TextureRegion>(0.25f, frames);

		introRobotMalfunction = Gdx.audio.newSound(Gdx.files.internal("sound/intro_computer_break.wav"));
		introRobotVoice = Gdx.audio.newSound(Gdx.files.internal("sound/intro_robot_voice.wav"));
		introAlarm = Gdx.audio.newSound(Gdx.files.internal("sound/intro_alarm.wav"));
		introBackground = Gdx.audio.newSound(Gdx.files.internal("sound/intro_soundscape_computer.wav"));

		mainMenuBackgroundTexture = new Texture(Gdx.files.internal("main_menu_background.png"));

		//humanSounds = new Sound[1];
		//humanSounds[0] = Gdx.audio.newSound(Gdx.files.internal("sound/human_running.wav"));
		mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/458368__infinita08__sci-fi-theme.mp3"));
		mainMenuMusic.setLooping(true);
		mainMenuMusic.setVolume(0.3f);

		click = Gdx.audio.newSound(Gdx.files.internal("sound/click.wav"));

		resultWindowDown = Gdx.audio.newSound(Gdx.files.internal("sound/result_shutdown.wav"));
		resultPop = Gdx.audio.newSound(Gdx.files.internal("sound/result_pop.wav"));
		resultMessage = Gdx.audio.newSound(Gdx.files.internal("sound/result_alert.wav"));
	}

	public Sound[] getHumanSounds() {
		Sound[] humanSounds = new Sound[6];
		humanSounds[0] = Gdx.audio.newSound(Gdx.files.internal("sound/human_running.wav"));
		// humanSounds[1] = Gdx.audio.newSound(Gdx.files.internal("sound/human_dodge.wav"));
		humanSounds[1] = Gdx.audio.newSound(Gdx.files.internal("sound/394426__inspectorj__bamboo-swing-b6.wav"));
		humanSounds[2] = Gdx.audio.newSound(Gdx.files.internal("sound/snap.wav"));
		humanSounds[3] = Gdx.audio.newSound(Gdx.files.internal("sound/human_deathscream.wav"));
		humanSounds[4] = Gdx.audio.newSound(Gdx.files.internal("sound/slash.wav"));
		humanSounds[5] = Gdx.audio.newSound(Gdx.files.internal("sound/leafrustle.wav"));

		return humanSounds;
	}

	public Sound[] getRobotSounds() {
		Sound[] robotSounds = new Sound[5];
		robotSounds[0] = Gdx.audio.newSound(Gdx.files.internal("sound/robot_move.wav"));
		robotSounds[1] = Gdx.audio.newSound(Gdx.files.internal("sound/blade_slash.wav"));
		robotSounds[2] = Gdx.audio.newSound(Gdx.files.internal("sound/machine_transform.wav"));
		robotSounds[3] = Gdx.audio.newSound(Gdx.files.internal("sound/machine_noise.wav"));
		robotSounds[4] = Gdx.audio.newSound(Gdx.files.internal("sound/launch.wav"));

		return robotSounds;
	}

	@Override
	public void dispose() {
		mainAreaTexture.dispose();
		pathToEntranceAreaTexture.dispose();
		westPassageAreaTexture.dispose();
		westHallwayAreaTexture.dispose();
		southPassageAreaTexture.dispose();
		southHallwayAreaTexture.dispose();
		testRoomAreaTexture.dispose();
		developmentRoomAreaTexture.dispose();
		directorOfficeAreaTexture.dispose();
		serverRoomAreaTexture.dispose();
		officeAreaTexture.dispose();
		pathToExitAreaTexture.dispose();

		boxTexture.dispose();
		cardKeyTexture.dispose();

		robotSpriteSheet.dispose();
		playerSpriteSheet.dispose();
		gateObjectSpriteSheet.dispose();
		doorObjectSpriteSheet.dispose();
		doorObjectRotatedSpriteSheet.dispose();

		interactableMark.dispose();
		portraitNormalTexture.dispose();
		portraitKeyTexture.dispose();
		portraitKilledTexture.dispose();

		skin.dispose();

		resultWindowTexture.dispose();

		introMatchInfoTexture.dispose();
		introBackgroundTexture.dispose();
		introRobotSpriteSheet.dispose();

		introRobotMalfunction.dispose();
		introRobotVoice.dispose();
		introAlarm.dispose();
		introBackground.dispose();

		mainMenuBackgroundTexture.dispose();

		mainMenuMusic.dispose();
		click.dispose();

		resultWindowDown.dispose();
		resultPop.dispose();
		resultMessage.dispose();
		//gateObjectRotatedSpriteSheet.dispose();
		Gdx.app.log("Textures", "disposed");
	}
}
